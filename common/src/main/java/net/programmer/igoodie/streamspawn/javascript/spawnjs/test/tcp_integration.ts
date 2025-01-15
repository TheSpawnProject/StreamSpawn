import { TcpClient } from "spawnjs:network";

const tcpClient = new TcpClient("127.0.0.1", 8080);

tcpClient.underlyingSocket.setSoTimeout(1000);

tcpClient.buffer = Buffer.alloc(1024);

tcpClient.on("lookup", (err, addressType, resolvedAddress, hostname) => {
  console.log(err, addressType, resolvedAddress, hostname);
});

tcpClient.on("connect", () => {
  console.log("Connected!");
});

tcpClient.on("error", (error) => {
  try {
    console.log(error);
  } catch (e) {
    console.log(e);
  }
});

registerService(tcpClient);
