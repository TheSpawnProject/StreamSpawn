/// <reference path="network/network.d.ts"/>

declare const integrationId: string;
declare const integrationVersion: string;
declare const integrationConfig: any;

declare function stopIntegration(reason: string): void;

declare function print(...args: any[]): void;

declare function setTimeout(cb: () => void, delayMs: number): void;
declare function setInterval(cb: () => void, delayMs: number): void;

declare function registerSocket(socket: Network.SocketHost): void;

declare type EventArgs = {
  [key: string]:
    | string
    | boolean
    | number
    | EventArgs
    | (string | boolean | number | EventArgs)[];
};

declare function emit(eventName: string, eventArgs: EventArgs): void;
