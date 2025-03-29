import { SocketIO } from "spawnjs:network";

const sio = new SocketIO("https://sockets.streamlabs.com");

const x = 5;

sio.options.query = "token=" + integrationConfig.token;

sio.onStart = () => {
  console.log("START!")
};

sio.on("error", (error) => {
  console.log("Error!", error);
  stopIntegration("Stopping because of an error: " + error);
});

sio.on("connect", () => {
  sio.socket.emit("ping", [], () => console.log("pong"));
  console.log("Connected!", x);
  setTimeout(() => console.log("Connected 5 seconds before this!"), 5000);
});

sio.on("disconnect", () => {
  console.log("Disconnected!");
  stopIntegration("Disconnected");
});

sio.on("event", (data) => {
  const eventType = data.type;
  const eventFor = data.for;
  const message = data.message[0] ?? data.message;
  console.log(eventType, eventFor, message);

  emit("Twitch Follow", {
    foo: "bar",
    mods: [1, 2, 3, "admin"],
  });
});

registerService(sio);

console.log("Setting up a bromise");

new Bromise((resolve) => {
  setTimeout(() => {
    resolve("DONE!");
  }, 1000);
})
  .then(console.log)
  .catch();

new Bromise((r) => r(1))
  .then(() => {
    throw new Error("Oopsie");
  })
  .then(() => console.log("Order doesn't matter"))
  .catch((err) => {
    throw new Error("Zoop");
  })
  .catch((err) => console.log(err));

console.log();
