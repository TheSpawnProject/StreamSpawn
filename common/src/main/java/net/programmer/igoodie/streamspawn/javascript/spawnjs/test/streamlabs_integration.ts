const sio = new Network.SocketIO("https://sockets.streamlabs.com");

const x = 5;

sio.options.query = "token=" + integrationConfig.token;

sio.on("error", (error) => {
  print("Error!", error);
  stopIntegration("Error");
});

sio.on("connect", () => {
  sio.socket.emit("ping", [], () => print("pong"));
  print("Connected!", x);
  setTimeout(() => print("Connected 5 seconds before this!"), 5000);
});

sio.on("disconnect", () => {
  print("Disconnected!");
  stopIntegration("Disconnected");
});

sio.on("event", (data) => {
  const eventType = data.type;
  const eventFor = data.for;
  const message = data.message[0] ?? data.message;
  print(eventType, eventFor, message);

  emit("Twitch Follow", {
    foo: "bar",
    mods: [1, 2, 3, "admin"],
  });
});

registerService(sio);

print("Setting up a bromise");

new Bromise((resolve) => {
  setTimeout(() => {
    resolve("DONE!");
  }, 1000);
})
  .then(print)
  .catch();

new Bromise((r) => r(1))
  .then(() => {
    throw new Error("Oopsie");
  })
  .then(() => print("Order doesn't matter"))
  .catch((err) => {
    throw new Error("Zoop");
  })
  .catch((err) => print(err));
