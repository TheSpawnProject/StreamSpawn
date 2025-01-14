const tcpClient = new Network.TcpClient("xxx127.0.0.1", 8080);

tcpClient.socket.setSoTimeout(1000);

tcpClient.bufferSize = 10000;

tcpClient.onConnect = () => {
  console.log("Connected");
};

tcpClient.onError = (reason) => {
  console.log("Error:", reason);
};

tcpClient.onChunk = (buffer, readLength) => {
  console.log(readLength, buffer);
};

registerService(tcpClient);

console.log(Buffer.alloc(9));
console.log(Buffer.alloc(5, 1700));
console.log(Buffer.from([257, 257.5, -255, "1"]));

const arr = new Uint16Array(2);
arr[0] = 5000;
arr[1] = 4000;
console.log(Buffer.from(arr));
arr[1] = 6000;
console.log(Buffer.from(arr));
