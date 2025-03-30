import { SocketIO } from "spawnjs:network";

const twitchService = new SocketIO("wss://eventsub.wss.twitch.tv/ws");

twitchService.options.transports = ["websocket", "polling"];
twitchService.options.query = `token=${integrationConfig.jwt}`

twitchService.on("message", (data) => console.log(data));

registerService(twitchService);
