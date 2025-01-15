/// <reference path="./hosts/SocketIOHost.d.ts"/>

declare module "spawnjs:network" {
  export class SocketIO extends Service {
    constructor(url: string);

    get socket(): SocketIO.Socket;
    get options(): SocketIO.Options;

    on(eventName: SocketIO.EventName, listener: (...args: any[]) => void): void;
    off(eventName: string): void;
  }

  export class TcpClient extends Service {
    constructor(host: string, port: number);

    get underlyingSocket(): NativeTcp.Socket;
    get buffer(): Buffer;
    set buffer(buffer: Buffer);

    on(
      event: "lookup",
      listener: (
        error: undefined | string,
        addressType: 4 | 6,
        resolvedAddress: string,
        hostname: string
      ) => void
    ): void;
    on(event: "connect", listener: () => void): void;
    on(event: "error", listener: (err: string) => void): void;
  }
}

declare namespace Buffer {
  type Encoding =
    | "ascii"
    | "utf8"
    | "utf-8"
    | "utf16le"
    | "ucs2"
    | "ucs-2"
    | "base64"
    | "base64url"
    | "latin1"
    | "binary"
    | "hex";
}

declare class Buffer {
  public constructor(content: number[]);

  public static alloc(size: number): Buffer;
  public static alloc(size: number, fill: number): Buffer;
  public static alloc(
    size: number,
    fill: string,
    encoding?: Buffer.Encoding
  ): Buffer;

  public static from(arrayBuffer: ArrayBufferLike): Buffer;
  public static from(
    arrayBuffer: ArrayBuffer,
    offset?: number,
    end?: number
  ): Buffer;
  public static from(array: (string | number)[]): Buffer;

  public fill(
    value: string,
    offset?: number,
    end?: number,
    encoding?: Buffer.Encoding
  ): Buffer;
  public fill(value: number, offset?: number, end?: number): Buffer;
}
