import { SocketIO } from "spawnjs:network";

const sio = new SocketIO<{
  foo: (a: 1, b: 2, c: 3) => void;
  bar: (a: "A", b: "B") => void;
  baz: (a: "A", b: "B", ack: () => 999) => void;
}>("https://sockets.streamlabs.com");

sio.connect();

sio.emit("foo", 1, 2, 3);

sio.io().on("connect", () => {
  console.log("Connect!");
});

sio.io().on("error", () => {
  sio.options.query = "auth=something_eg";
});

sio
  .emitWithAck("baz", "A", "B")
  //
  .then((res) => console.log("Res:", res));

const sio2 = new SocketIO("");

sio2.emit("", 1, 2, 3, 4, 5, 6, 7);

sio2
  .emitWithAck("foo", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
  .then((val) => console.log(val));

// slService.options.query = "token=" + integrationConfig.token;

// slService.addServiceListener("service-starting", () => {
//   console.log("Streamlabs Service is starting...");
// });

// slService.on("error", (error) => {
//   console.log("Error!", error);
//   stopIntegration("Stopping because of an error: " + error);
// });

// slService.on("connect", () => {
//   slService.socket.emit("ping", [], () => console.log("pong"));
//   console.log("Connected!");
//   setTimeout(() => console.log("Connected 5 seconds before this!"), 5000);
// });

// slService.on("disconnect", () => {
//   console.log("Disconnected!");
//   stopIntegration("Disconnected");
// });

// slService.on("event", (data) => {
//   const eventType = data.type;
//   const eventFor = data.for;
//   const message = data.message[0] ?? data.message;
//   console.log(eventType, eventFor, message);

//   emit("Twitch Follow", {
//     foo: "bar",
//     mods: [1, 2, 3, "admin"],
//   });
// });
