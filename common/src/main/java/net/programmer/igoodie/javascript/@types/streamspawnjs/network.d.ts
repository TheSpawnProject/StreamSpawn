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

declare namespace Network {
  export interface Socket {}

  export class SocketIO implements Socket {
    constructor(url: string);

    set prepareOptions(val: (options: SocketIOOptions) => void);
  }
}
