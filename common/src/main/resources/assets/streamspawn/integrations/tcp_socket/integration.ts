import { TcpSocket, Buffer } from "spawnjs:network";
import { defineIntegration } from "streamspawn:integrations";

const socket = new TcpSocket("127.0.0.1", 8080, Buffer.alloc(1024));

socket.on("lookup", (addressType, resolvedAddress, hostname) => {
  console.log(addressType, resolvedAddress, hostname);
});

socket.on("connect", () => {
  console.log("Connected!");
});

socket.on("data", (buffer, readLength) => {
  console.log(buffer);
  socket.write(Buffer.from([0xff, 0xaa, 0xbb]));
});

socket.on("error", (error) => {
  try {
    console.log(error);
  } catch (e) {
    console.log(e);
  }
});

export default defineIntegration({
  start: () => socket.connect(),
  stop: () => socket.disconnect(),
});
