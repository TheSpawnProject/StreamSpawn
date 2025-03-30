declare module "streamspawn:services" {
  export function registerService(logic: () => void | (() => void)): void;

  export class Service {
    constructor(opts: { begin: () => void; terminate?: () => void });
  }
}

// import {  } from "streamspawn:services";
