declare module "spawnjs:network" {
  export namespace SocketIO {
    interface TransportOptions {
      hostname?: string;
      path?: string;
      timestampParam?: string;
      secure?: boolean;
      timestampRequests?: boolean;
      port?: number;
      policyPort?: number;
      // webSocketFactory
      // callFactory
    }

    interface Options extends TransportOptions {
      forceNew?: boolean;
      reconnection?: boolean;
      reconnectionAttempts?: number;
      reconnectionDelay?: number;
      reconnectionDelayMax?: number;
      randomizationFactor?: number;
      // encoder
      // decoder
      timeout?: number;
      transports?: string[];
      upgrade?: boolean;
      rememberUpgrade?: boolean;
      host?: string;
      query?: string;
      transportOptions?: Record<string, TransportOptions>;
    }

    type DefaultEventName =
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
      | "pong";

    interface EventsMap {
      [event: string]: (...args: any[]) => void;
    }

    type DefaultEventsMap = {
      [event in DefaultEventName]: (...args: any[]) => void;
    };

    type AckParam<R = any> = () => R;

    type Ack<T extends (...args: any[]) => void> = Parameters<T> extends [
      ...any[],
      infer Last
    ]
      ? Last extends AckParam
        ? Last
        : never
      : never;

    type EventArgsWithoutAck<T extends (...args: any[]) => void> =
      Parameters<T> extends [...infer Args, any] ? Args : never;

    type AckableEventNames<T extends EventsMap> = TypeUtils.Equals<
      T,
      EventsMap
    > extends true
      ? string
      : keyof TypeUtils.Filter<T, (...args: [...any[], AckParam]) => any>;
  }

  export class IOManager<
    TListenEvents extends SocketIO.EventsMap = SocketIO.EventsMap
  > {
    on<E extends keyof TListenEvents>(
      eventName: E,
      listener: TListenEvents[E]
    ): void;

    once<E extends keyof TListenEvents>(
      eventName: E,
      listener: TListenEvents[E]
    ): void;
  }

  export class SocketIO<
    TListenEvents extends SocketIO.EventsMap = SocketIO.EventsMap,
    TEmitEvents extends SocketIO.EventsMap = TListenEvents
  > extends IOManager<TListenEvents> {
    constructor(url: string, options?: SocketIO.Options);

    get options(): SocketIO.Options;

    connect(): void;

    disconnect(): void;

    io(): IOManager<SocketIO.DefaultEventsMap>;

    emit<
      E extends keyof TEmitEvents,
      $args extends any[] = Parameters<TEmitEvents[E]>
    >(eventName: E, ...args: [...$args]): void;

    emitWithAck<
      E extends SocketIO.AckableEventNames<TEmitEvents>,
      $eventCb extends (...args: any) => void = TEmitEvents[E],
      $ack extends SocketIO.AckParam = SocketIO.Ack<$eventCb>,
      $ackArgs extends any[] = SocketIO.EventArgsWithoutAck<$eventCb>,
      $ackResult = TypeUtils.If<
        TypeUtils.Equals<$ackArgs, never>,
        any,
        ReturnType<$ack>
      >
    >(
      eventName: E,
      ...args: TypeUtils.If<
        TypeUtils.Equals<$ackArgs, never>,
        any[],
        [...$ackArgs]
      >
    ): Promise<$ackResult>;
  }
}
