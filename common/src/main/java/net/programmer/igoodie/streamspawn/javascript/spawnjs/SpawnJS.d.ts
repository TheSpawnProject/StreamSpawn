/// <reference path="./globals/TslAPI.d.ts"/>
/// <reference path="./globals/ServiceAPI.d.ts"/>
/// <reference path="./globals/ConsoleAPI.d.ts"/>
/// <reference path="./globals/TimerAPI.d.ts"/>
/// <reference path="./modules/Network.d.ts"/>

declare const __version: string;

declare const integrationId: string;
declare const integrationVersion: string;
declare const integrationConfig: any;

declare interface JavaBackedObject {
  [field: string]: any;
}

declare function stopIntegration(reason: string): void;

declare class Bromise<T> extends Promise<T> {}

