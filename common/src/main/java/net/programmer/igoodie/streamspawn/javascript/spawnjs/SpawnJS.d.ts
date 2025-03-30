/// <reference path="./SpawnEnv.d.ts"/>

/// <reference path="./globals/TslAPI.d.ts"/>
/// <reference path="./globals/ServiceAPI.d.ts"/>
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
