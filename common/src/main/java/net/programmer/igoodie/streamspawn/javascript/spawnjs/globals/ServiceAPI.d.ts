declare namespace Service {
  export interface LifecycleEventsMap {
    "service-starting": () => void;
    "service-terminating": () => void;
  }
}

declare class Service {
  public addServiceListener<K extends keyof Service.LifecycleEventsMap>(
    eventName: K,
    listener: Service.LifecycleEventsMap[K]
  );
}

declare function registerService(service: Service): void;
