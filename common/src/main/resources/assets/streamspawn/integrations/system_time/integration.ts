/**
 * This integration shall be able to
 * produce "Clock Tick" every 1 second.
 *
 * It shall be able to detach the timer,
 * once the service is detached
 */

import { AbstractService } from "spawnjs:core";

// Perhaps some API like this?

const service = new AbstractService();

let intervalId: number;

service.onStart = () => {
  intervalId = setInterval(() => {
    emit("Clock Tick", {});
  }, 1000);
};

service.onTerminate = () => clearInterval(intervalId);

registerService(service);
