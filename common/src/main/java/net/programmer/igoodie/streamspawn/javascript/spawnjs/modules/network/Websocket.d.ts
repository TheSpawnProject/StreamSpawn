declare namespace Websocket {
  interface Options {
    reuseAddr: boolean;
    tcpNoDelay: boolean;
    handshakeHeaders: Record<string, string>;
  }

  class Socket implements NativeJavaObject {
    send(bytes: number[]): void;
    send(text: string): void;
    sendPing(): void;

    getReadyState(): ReadyState;
  }

  class ReadyState extends JavaEnum<
    "NOT_YET_CONNECTED" | "OPEN" | "CLOSING" | "CLOSED"
  > {}

  interface EventMap {
    open: () => void;
    message: (msg: string) => void;
    message_buffer: (msg: import("spawnjs:network").Buffer) => void;
    close: (code: number, reason: string, remote: boolean) => void;
    error: (type: string, message: string) => void;
  }
}
