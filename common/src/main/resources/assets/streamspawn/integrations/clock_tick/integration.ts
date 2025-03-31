/**
 * This integration shall be able to
 * produce "Clock Tick" every second.
 *
 * It shall be able to detach the timer,
 * once the service is detached
 */

import { createEmitter } from "streamspawn:tsl";
import { defineIntegration } from "streamspawn:integrations";

type ClockTslEvents = typeof import("./manifest.json")["events"];

const emitTslEvent = createEmitter<ClockTslEvents>();

let intervalId: number;

export default defineIntegration({
  start() {
    emitTslEvent("Clock Begin", { time: Date.now() });

    intervalId = setInterval(() => {
      emitTslEvent("Clock Tick", {
        actor: "Clock",
        dt: Math.random() >= 0.5 ? 1000 : 2000,
      });
    }, 1000);
  },

  stop: () => {
    emitTslEvent("Clock End", { time: Date.now() });

    clearInterval(intervalId);
  },
});
