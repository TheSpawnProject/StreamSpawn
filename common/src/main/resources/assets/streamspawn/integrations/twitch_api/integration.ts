import { SocketIO } from "spawnjs:network";

const sioService = new SocketIO("wss://eventsub.wss.twitch.tv/ws");

sioService.options.transports = ["websocket", "polling"];

sioService.on("message", (data) => console.log(data));

registerService(sioService);
