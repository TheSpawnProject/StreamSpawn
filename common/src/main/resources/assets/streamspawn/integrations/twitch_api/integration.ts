import { Websocket } from "spawnjs:network";

console.log(Websocket.ReadyState.OPEN.name());

const twitchService = new Websocket("wss://eventsub.wss.twitch.tv/ws");

console.log(twitchService.options);

twitchService.options.tcpNoDelay = true;

twitchService.addServiceListener("service-starting", () => {
  console.log("State =", twitchService.socket.getReadyState().name() + "");
  console.log("Service starting");
});

twitchService.on("open", (handshake) => {
  console.log(handshake.getHttpStatus(), handshake.getHttpStatusMessage());
  console.log("Open");
});

twitchService.on("message", (data) => {
  console.log("State =", twitchService.socket.getReadyState().name());
  console.log(JSON.parse(data));
});

twitchService.on("error", (type, message) => {
  console.log(type, message);
});

twitchService.on("close", console.log);

registerService(twitchService);
