declare module "streamspawn:integrations" {
  function defineIntegration(opts: { start(): void; stop(): void }): void;
}
