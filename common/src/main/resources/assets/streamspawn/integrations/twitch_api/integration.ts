import { Websocket } from "spawnjs:network";
// import { registerService, Service } from "streamspawn:services";

// console.log(Websocket.State.OPEN.name());

const eventsubWs = new Websocket("wss://eventsub.wss.twitch.tv/ws");

eventsubWs.addServiceListener("service-starting", () => {
  console.log("Service starting");
  console.log("State =", eventsubWs.socket.getState().name() + "");
});

eventsubWs.on("connected", (handshake) => {
  console.log("Connected", handshake);
});

eventsubWs.on("message", (data) => {
  console.log(
    "Got msg; State =",
    eventsubWs.socket.getState().name(),
    JSON.parse(data)
  );
});

eventsubWs.on("error", (type, message) => {
  console.log("Error..", type, message);
});

eventsubWs.on("disconnected", console.log);

registerService(eventsubWs);

// const twitchService = new Service({
//   begin: () => eventsubWs.connect(),
//   terminate: () => eventsubWs.disconnect(),
// });

// registerService(twitchService);

