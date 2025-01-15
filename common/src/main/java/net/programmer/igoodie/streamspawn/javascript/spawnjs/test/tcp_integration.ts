const tcpClient = new Network.TcpClient("127.0.0.1", 8080);

tcpClient.underlyingSocket.setSoTimeout(1000);

tcpClient.buffer = Buffer.alloc(1024);

tcpClient.on("lookup", (err, addressType, resolvedAddress, hostname) => {
  console.log(err, addressType, resolvedAddress, hostname);
});

tcpClient.on("connect", () => {
  console.log("Connected!");
});

registerService(tcpClient);
