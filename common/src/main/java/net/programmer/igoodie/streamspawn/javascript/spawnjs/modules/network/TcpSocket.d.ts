declare module "spawnjs:network" {
  export namespace TcpSocket {
    interface Options {
      // TODO:
      //   keepalive(on: boolean): void;
      //   OOBInline(on: boolean): void;
      //   // setOption
      //   performancePreferences(
      //     connectionTime: number,
      //     latency: number,
      //     bandwidth: number
      //   ): void;
      //   receiveBufferSize(size: number): void;
      //   reuseAddress(on: boolean): void;
      //   sendBufferSize(size: number): void;
      //   soLinger(on: boolean, linger: number): void;
      //   soTimeout(timeout: number): void;
      //   tcpNoDelay(on: boolean): void;
      //   trafficClass(tc: number): void;
    }

    interface EventsMap {
      lookup: (
        addressType: 4 | 6,
        resolvedAddress: string,
        hostname: string
      ) => void;
      connect: () => void;
      data: (buffer: Buffer, readLength: number) => void;
      error: (type: string, message: string) => void;
      end: () => void;
    }
  }

  export class TcpSocket {
    constructor(host: string, port: number, buffer?: Buffer);

    connect(): void;
    disconnect(): void;

    write(buffer: Buffer): void;

    on<E extends keyof TcpSocket.EventsMap>(
      eventName: E,
      listener: TcpSocket.EventsMap[E]
    ): void;
  }
}
