import net from "node:net";
import { buffer } from "node:stream/consumers";

// Create a TCP client
const client = net.createConnection(
  {
    host: "google.com",
    port: 80,
    onread: {
      buffer: Buffer.alloc(512 * 20),
      callback: (nread, buffer) => {
        console.log(nread, buffer);
      },
    },
    family: 0,
  },
  () => {
    console.log("Connected to the server!");

    // Send a raw HTTP GET request
    client.write("GET / HTTP/1.1\r\n");
    client.write("Host: example.com\r\n");
    client.write("\r\n"); // End of headers
  }
);

client.on("lookup", (...args) => console.log(args));
// client.on("data", (...args) => console.log(args));
client.on("end", (...args) => console.log(args));
