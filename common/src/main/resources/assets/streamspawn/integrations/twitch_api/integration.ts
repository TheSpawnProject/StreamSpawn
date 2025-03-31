import { Websocket } from "spawnjs:network";
import { defineIntegration } from "streamspawn:integrations";

console.log("Enums work =", Websocket.State.OPEN.name());

const ws = new Websocket("wss://eventsub.wss.twitch.tv/ws");

console.log(ws.state.name());

ws.on("connected", (handshake) => {
  console.log("Connected", handshake);
});

ws.on("disconnected", (args) => {
  console.log(args);
});

ws.on("message", (data) => {
  console.log("Got msg; State =", ws.state.name(), JSON.parse(data));
});

ws.on("error", (type, message) => {
  console.log("Error..", type, message);
});

export default defineIntegration({
  start: () => ws.connect(),
  stop: () => ws.disconnect(),
});
