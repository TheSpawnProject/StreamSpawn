declare namespace Websocket {
  interface Options {
    connectionTimeout: number;
    socketTimeout: number;
    handshakeHeaders: Record<string, string>;
  }

  class Socket implements Java.NativeObject {
    send(bytes: number[]): void;
    send(text: string): void;
    sendPing(): void;

    getState(): Java.EnumConstant<State>;
  }

  type State = Java.Enum<
    "CREATED" | "CONNECTING" | "OPEN" | "CLOSING" | "CLOSED"
  >;

  interface EventMap {
    connected: (handshake: any) => void;
    message: (msg: string) => void;
    message_buffer: (msg: import("spawnjs:network").Buffer) => void;
    disconnected: (code: number, reason: string, remote: boolean) => void;
    error: (type: string, message: string) => void;
  }
}
