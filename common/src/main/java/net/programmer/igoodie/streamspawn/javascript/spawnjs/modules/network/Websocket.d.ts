declare namespace Websocket {
  interface Options {
    reuseAddr: boolean;
    tcpNoDelay: boolean;
    handshakeHeaders: Record<string, string>;
  }

  interface Socket extends JavaBackedObject {
    // TODO
  }

  type EventName = "open" | "message" | "message_buffer" | "close" | "error";

  interface EventMap {
    open: () => void;
    message: (msg: string) => void;
    message_buffer: (msg: import("spawnjs:network").Buffer) => void;
    close: (code: number, reason: string, remote: boolean) => void;
    error: (type: string, message: string) => void;
  }
}
