/// <reference path="./network/TcpConnection.d.ts"/>
/// <reference path="./network/SocketIO.d.ts"/>
/// <reference path="./network/Websocket.d.ts"/>

// TODO: Yeet this, and narrow down to the d.ts files
declare module "spawnjs:network" {
  export class TcpConnection extends Service {
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

  /* ---------------------- */

  export namespace Buffer {
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

  export class Buffer {
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
}
