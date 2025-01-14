/// <reference path="network/NetworkAPI.d.ts"/>
/// <reference path="core/TimerAPI.d.ts"/>

declare const __version: string;

declare const integrationId: string;
declare const integrationVersion: string;
declare const integrationConfig: any;

declare function stopIntegration(reason: string): void;

declare function print(...args: any[]): void;

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
