const sio = new Network.SocketIO("https://sockets.streamlabs.com");

const x = 5;

sio.modifyOptions((options) => {
  options.query = "token=" + integrationConfig.token;
});

sio.on("error", (arg0) => {
  print("Error!", arg0);
});

sio.on("connect", () => {
  sio.socket.emit("ping", [], () => print("pong"));
  print("Connected!", x);
});

sio.on("disconnect", () => {
  print("Disconnected!");
});

sio.on("event", (data) => {
  const eventType = data.type;
  const eventFor = data.for;
  const message = data.message[0] ?? data.message;
  print(eventType, eventFor, message);
});

registerSocket(sio);

emit("Twitch Follow", {
  foo: "bar",
  mods: [1, 2, 3, "admin"],
});

print("Scheduling after 5 sec");
setTimeout(() => {
  print("Schedule executed!");
}, 5000);
