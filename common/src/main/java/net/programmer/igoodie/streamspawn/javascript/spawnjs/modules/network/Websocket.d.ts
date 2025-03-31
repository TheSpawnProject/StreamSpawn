declare module "spawnjs:network" {
  export namespace Websocket {
    interface Options {
      connectionTimeout?: number;
      socketTimeout?: number;
      handshakeHeaders?: Record<string, string>;
    }

    type State = Java.Enum<
      "CREATED" | "CONNECTING" | "OPEN" | "CLOSING" | "CLOSED"
    >;

    interface EventMap {
      connected: (handshake: any) => void;
      message: (msg: string) => void;
      message_buffer: (msg: import("spawnjs:network").Buffer) => void;
      pong: (payload: import("spawnjs:network").Buffer) => void;
      disconnected: (code: number, reason: string, remote: boolean) => void;
      error: (type: string, message: string) => void;
    }
  }

  export class Websocket {
    constructor(url: string, options?: Websocket.Options);

    static State: Websocket.State;

    connect(): void;

    disconnect(): void;

    get state(): Java.EnumConstant<Websocket.State>;

    on<T extends keyof Websocket.EventMap>(
      eventName: T,
      listener: Websocket.EventMap[T]
    ): void;

    send(bytes: Buffer): void;
    send(text: string): void;
    
    sendPing(): void;
  }
}
