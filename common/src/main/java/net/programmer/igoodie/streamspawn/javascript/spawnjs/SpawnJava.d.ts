declare namespace Java {
  export interface NativeObject {}

  type _EnumConstant<
    T extends string,
    V extends object = Record<string, never>
  > = {
    name(): T;
    ordinal(): number;
    compareTo(other: _EnumConstant<T>): number;
  } & V;

  export type Enum<
    T extends string,
    V extends object = Record<string, never>
  > = {
    [enumConstant in T]: _EnumConstant<enumConstant, NoInfer<V>>;
  } & {
    values: T[];
  };

  export type EnumConstant<E extends Enum<any, any>> = E extends Enum<
    infer T,
    infer V
  >
    ? _EnumConstant<T, V>
    : never;
}
