/// <reference path="./SpawnJava.d.ts"/>

/// <reference path="./globals/ConsoleAPI.d.ts"/>
/// <reference path="./globals/TimerAPI.d.ts"/>

/// <reference path="./modules/Core.d.ts"/>
/// <reference path="./modules/Network.d.ts"/>

declare const __version: string;

declare const integration: {
  id: string;
  name: string;
  version: string;
};

declare const integrationConfig: any;

declare function stopIntegration(reason: string): void;

declare namespace TypeUtils {
  export type Prettify<T> = {
    [K in keyof T]: T[K];
  } & {};

  // prettier-ignore
  export type Equals<X, Y> = 
    (<T>() => T extends X ? 1 : 2) extends 
    (<T>() => T extends Y ? 1 : 2) ? true : false;

  export type Filter<T extends object, TTarget> = {
    [K in keyof T as T[K] extends TTarget ? K : never]: never;
  };

  export type If<C extends boolean, T, F = unknown> = C extends true ? T : F;
}
