const tcpClient = new Network.TcpClient("127.0.0.1", 8080);

tcpClient.socket.setSoTimeout(1000);

tcpClient.bufferSize = 10000;

tcpClient.onConnect = () => {
  console.log("Connected");
};

tcpClient.onError = (reason) => {
  console.log("Error:", reason);
};

tcpClient.onChunk = (buffer, readLength) => {
  // Here, buffer is a Buffer type
};

registerService(tcpClient);

console.log(Buffer.alloc(11, 'aGVsbG8gd29ybGQ=', 'base64'));
console.log(Buffer.alloc(9));
console.log(Buffer.alloc(5, 1700));
console.log(Buffer.alloc(5, "a"));
console.log(Buffer.alloc(5, 0xff));
