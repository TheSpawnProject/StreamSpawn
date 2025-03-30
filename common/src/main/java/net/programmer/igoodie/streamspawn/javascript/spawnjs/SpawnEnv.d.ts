declare interface NativeJavaObject {}

declare class JavaEnum<T> {
  name(): T;
  ordinal(): number;
  compareTo(other: JavaEnum<T>): number;
}
