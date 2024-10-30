const sio = new Network.SocketIO("https://somewhere.com");

sio.prepareOptions = (options) => {
  options.forceNew = false;
  options.reconnection = false;
  options.transports = ["websocket"];
  options.query = "token=foobarbaz";
};

registerSocket(sio);
