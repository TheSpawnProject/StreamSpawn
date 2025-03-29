import { SocketIO } from "spawnjs:network";

const twitchService = new SocketIO("wss://eventsub.wss.twitch.tv/ws");

twitchService.options.transports = ["websocket", "polling"];

// TODO: Keep implementing from here
twitchService.on("message", (data) => console.log(data));

registerService(twitchService);
