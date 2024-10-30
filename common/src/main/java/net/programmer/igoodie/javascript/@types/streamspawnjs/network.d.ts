interface SocketIOTransportOptions {
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

interface SocketIOOptions extends SocketIOTransportOptions {
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
  transportOptions: Record<string, SocketIOTransportOptions>;
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

declare namespace Network {
  export interface Socket {}

  export class SocketIO implements Socket {
    constructor(url: string);

    modifyOptions(val: (options: SocketIOOptions) => void): void;

    on(eventName: EventName, listener: (...args: any[]) => void): void;
  }
}
