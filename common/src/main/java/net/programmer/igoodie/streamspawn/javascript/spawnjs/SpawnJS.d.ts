/// <reference path="globals/ConsoleAPI.d.ts"/>
/// <reference path="globals/TimerAPI.d.ts"/>
/// <reference path="network/Network.d.ts"/>

declare const __version: string;

declare const integrationId: string;
declare const integrationVersion: string;
declare const integrationConfig: any;

declare interface JavaBackedObject {
  [field: string]: any;
}

declare function stopIntegration(reason: string): void;

declare class Bromise<T> extends Promise<T> {}

declare class Service {
  private type(): Symbol[];
}

declare function registerService(service: Service): void;

declare type EventArgs = {
  [key: string]:
    | string
    | boolean
    | number
    | EventArgs
    | (string | boolean | number | EventArgs)[];
};

declare function emit(eventName: string, eventArgs: EventArgs): void;

interface ServiceSymbols {
  readonly beginService: unique symbol;
  readonly terminateService: unique symbol;
}

declare const ServiceSymbols: ServiceSymbols;
