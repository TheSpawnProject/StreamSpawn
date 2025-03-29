/**
 * This integration shall be able to
 * produce "Clock Tick" every second.
 *
 * It shall be able to detach the timer,
 * once the service is detached
 */

import { AbstractService } from "spawnjs:core";

const service = new AbstractService();

let intervalId: number;

service.addServiceListener("service-starting", () => {
  intervalId = setInterval(() => {
    emit("Clock Tick", {});
  }, 1000);
});

service.addServiceListener("service-terminating", () => {
  clearInterval(intervalId);
});

registerService(service);
