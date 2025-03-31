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
