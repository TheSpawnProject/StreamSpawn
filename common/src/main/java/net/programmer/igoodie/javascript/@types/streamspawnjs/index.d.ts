/// <reference path="network.d.ts"/>

declare const integrationConfig: any;

declare function print(...args: any[]): void;

declare function registerSocket(socket: Network.Socket): void;

declare type EventArgs = {
  [key: string]:
    | string
    | boolean
    | number
    | EventArgs
    | (string | boolean | number | EventArgs)[];
};

declare function emit(eventName: string, eventArgs: EventArgs): void;
