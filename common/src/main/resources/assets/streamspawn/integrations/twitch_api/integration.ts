import { Websocket } from "spawnjs:network";

const twitchService = new Websocket("wss://eventsub.wss.twitch.tv/ws");

twitchService.options.tcpNoDelay = true;

twitchService.addServiceListener("service-starting", () => {
  console.log("State =", twitchService.socket.getReadyState().name() + "");
  // console.log("State =", twitchService.socket.getReadyState().ordinal());
  console.log("Service starting");
});

twitchService.on("open", () => {
  console.log("Open");
});

twitchService.on("message", (data) => {
  console.log("State =", twitchService.socket.getReadyState().name());
  // console.log("State =", twitchService.socket.getReadyState().ordinal());
  console.log(JSON.parse(data));
});

twitchService.on("error", (type, message) => {
  console.log(type, message);
});

twitchService.on("close", console.log);

registerService(twitchService);
