declare namespace NativeTcp {
  interface Socket extends JavaBackedObject {
    setKeepalive(on: boolean): void;
    setOOBInline(on: boolean): void;
    // setOption
    setPerformancePreferences(
      connectionTime: number,
      latency: number,
      bandwidth: number
    ): void;
    setReceiveBufferSize(size: number): void;
    setReuseAddress(on: boolean): void;
    setSendBufferSize(size: number): void;
    setSoLinger(on: boolean, linger: number): void;
    setSoTimeout(timeout: number): void;
    setTcpNoDelay(on: boolean): void;
    setTrafficClass(tc: number): void;
  }
}
