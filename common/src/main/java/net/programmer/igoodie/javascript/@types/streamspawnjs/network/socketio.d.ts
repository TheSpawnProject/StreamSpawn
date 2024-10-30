declare namespace SocketIO {
  interface TransportOptions {
    hostname: string;
    path: string;
    timestampParam: string;
    secure: boolean;
    timestampRequests: boolean;
    port: number;
    policyPort: number;
    // webSocketFactory
    // callFactory
  }

  interface Options extends TransportOptions {
    forceNew: boolean;
    reconnection: boolean;
    reconnectionAttempts: number;
    reconnectionDelay: number;
    reconnectionDelayMax: number;
    randomizationFactor: number;
    // encoder
    // decoder
    timeout: number;
    transports: string[];
    upgrade: boolean;
    rememberUpgrade: boolean;
    host: string;
    query: string;
    transportOptions: Record<string, TransportOptions>;
  }

  type Ack = (...args: any[]) => void;

  interface Socket {
    disconnect(): void;
    emit(eventName: string, ...args: any[]): void;
    emit(eventName: string, args: any[], ack: Ack): void;
  }

  type EventName =
    | "connect"
    | "connecting"
    | "disconnect"
    | "error"
    | "message"
    | "connect_error"
    | "connect_timeout"
    | "reconnect"
    | "reconnect_error"
    | "reconnect_failed"
    | "reconnect_attempt"
    | "reconnecting"
    | "ping"
    | "pong"
    | (string & {});
}
