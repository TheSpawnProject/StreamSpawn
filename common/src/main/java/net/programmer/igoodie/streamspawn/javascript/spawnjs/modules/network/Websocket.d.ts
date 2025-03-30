declare namespace Websocket {
  interface Options {
    reuseAddr: boolean;
    tcpNoDelay: boolean;
    handshakeHeaders: Record<string, string>;
  }

  class Socket implements Java.NativeObject {
    send(bytes: number[]): void;
    send(text: string): void;
    sendPing(): void;

    getReadyState(): Java.EnumConstant<ReadyState>;
  }

  interface ServerHandshake extends Java.NativeObject {
    getHttpStatus(): number;
    getHttpStatusMessage(): string;
  }

  type ReadyState = Java.Enum<
    "NOT_YET_CONNECTED" | "OPEN" | "CLOSING" | "CLOSED"
  >;

  interface EventMap {
    open: (handshake: ServerHandshake) => void;
    message: (msg: string) => void;
    message_buffer: (msg: import("spawnjs:network").Buffer) => void;
    close: (code: number, reason: string, remote: boolean) => void;
    error: (type: string, message: string) => void;
  }
}
