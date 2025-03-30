import { Websocket } from "spawnjs:network";

// console.log(Websocket.State.OPEN.name());

const twitchService = new Websocket("wss://eventsub.wss.twitch.tv/ws");

twitchService.addServiceListener("service-starting", () => {
  console.log("Service starting");
  console.log("State =", twitchService.socket.getState().name() + "");
});

twitchService.on("connected", (handshake) => {
  console.log("Connected", handshake);
});

twitchService.on("message", (data) => {
  console.log(
    "Got msg; State =",
    twitchService.socket.getState().name(),
    JSON.parse(data)
  );
});

twitchService.on("error", (type, message) => {
  console.log("Error..", type, message);
});

twitchService.on("disconnected", console.log);

registerService(twitchService);
