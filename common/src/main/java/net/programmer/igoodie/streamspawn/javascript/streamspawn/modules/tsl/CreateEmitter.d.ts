declare module "streamspawn:tsl" {
  type EventsConfig = Record<string, EventArgsConfig>;

  type EventArgsConfig = Record<string, EventArgConfig>;

  type EventArgConfig =
    | { number: boolean; min?: number; max?: number }
    | { string: boolean }
    | { boolean: boolean };

  type EventArgsSchema<TConfig extends EventArgsConfig> = {
    [P in keyof TConfig]: "number" extends keyof TConfig[P]
      ? number
      : "string" extends keyof TConfig[P]
      ? string
      : "boolean" extends keyof TConfig[P]
      ? boolean
      : never;
  };

  function createEmitter<T extends EventsConfig>(): <K extends keyof T>(
    eventName: K,
    args: TypeUtils.Prettify<EventArgsSchema<T[K]>>
  ) => void;
}
