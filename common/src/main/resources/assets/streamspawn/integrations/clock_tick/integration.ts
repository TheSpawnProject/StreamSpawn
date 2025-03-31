/**
 * This integration shall be able to
 * produce "Clock Tick" every second.
 *
 * It shall be able to detach the timer,
 * once the service is detached
 */

import { defineIntegration } from "streamspawn:integrations";

let intervalId: number;

export default defineIntegration({
  start() {
    intervalId = setInterval(() => {
      emit("Clock Tick", {});
    }, 1000);
  },

  stop: () => clearInterval(intervalId),
});
