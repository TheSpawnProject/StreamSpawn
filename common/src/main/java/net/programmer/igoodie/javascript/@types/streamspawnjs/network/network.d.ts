/// <reference path="socketio.d.ts"/>

declare namespace Network {
  export interface SocketHost {}

  export class SocketIO implements SocketHost {
    constructor(url: string);

    get socket(): SocketIO.Socket;

    modifyOptions(val: (options: SocketIO.Options) => void): void;

    on(eventName: SocketIO.EventName, listener: (...args: any[]) => void): void;
  }
}
