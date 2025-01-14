/// <reference path="./hosts/SocketIOHost.d.ts"/>

declare namespace Network {
  export class SocketIO extends Service {
    constructor(url: string);

    get socket(): SocketIO.Socket;
    get options(): SocketIO.Options;

    on(eventName: SocketIO.EventName, listener: (...args: any[]) => void): void;
    off(eventName: string): void;
  }

  export class TcpClient extends Service {
    constructor(host: string, port: number);

    get socket(): Tcp.Socket;
    get bufferSize(): number;
    set bufferSize(sizeInBytes: number);

    set onConnect(listener: () => void);
    set onError(listener: (reason: string) => void);
    set onChunk(listener: (buffer: number[], readLength: number) => void);
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
  private constructor();

  public static alloc(size: number): Buffer;
  public static alloc(size: number, fill: number): Buffer;
  public static alloc(
    size: number,
    fill: string,
    encoding?: Buffer.Encoding
  ): Buffer;

  public static from(arrayBuffer: ArrayBufferLike): Buffer;
  public static from(arrayBuffer: ArrayBuffer): Buffer;
  public static from(array: (string | number)[]): Buffer;

  public fill(
    value: string,
    offset?: number,
    end?: number,
    encoding?: Buffer.Encoding
  ): Buffer;
  public fill(value: number, offset?: number, end?: number): Buffer;
  public fill(
    value: string | Uint8Array | number,
    offset?: number,
    end?: number,
    encoding?: Buffer.Encoding
  ): Buffer;
}
