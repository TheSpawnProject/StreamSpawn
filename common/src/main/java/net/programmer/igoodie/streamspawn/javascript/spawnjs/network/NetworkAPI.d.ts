/// <reference path="SocketIOHost.d.ts"/>

declare namespace Network {
  export class SocketIO extends Service {
    constructor(url: string);

    get socket(): SocketIO.Socket;
    get options(): SocketIO.Options;

    on(eventName: SocketIO.EventName, listener: (...args: any[]) => void): void;
  }
}
