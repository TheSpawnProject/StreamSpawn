import { SocketIO } from "spawnjs:network";

const slService = new SocketIO("https://sockets.streamlabs.com");

slService.options.query = "token=" + integrationConfig.token;

slService.addServiceListener("service-starting", () => {
  console.log("Streamlabs Service is starting...");
});

slService.on("error", (error) => {
  console.log("Error!", error);
  stopIntegration("Stopping because of an error: " + error);
});

slService.on("connect", () => {
  slService.socket.emit("ping", [], () => console.log("pong"));
  console.log("Connected!");
  setTimeout(() => console.log("Connected 5 seconds before this!"), 5000);
});

slService.on("disconnect", () => {
  console.log("Disconnected!");
  stopIntegration("Disconnected");
});

slService.on("event", (data) => {
  const eventType = data.type;
  const eventFor = data.for;
  const message = data.message[0] ?? data.message;
  console.log(eventType, eventFor, message);

  emit("Twitch Follow", {
    foo: "bar",
    mods: [1, 2, 3, "admin"],
  });
});

registerService(slService);
