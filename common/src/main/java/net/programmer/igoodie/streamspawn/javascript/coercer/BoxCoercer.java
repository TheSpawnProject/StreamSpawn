package net.programmer.igoodie.streamspawn.javascript.coercer;

import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import java.util.function.BiFunction;
import java.util.function.Function;

public class BoxCoercer<T, N extends ScriptableObject> extends Coercer<T, N> {

    protected final Class<T> boxedType;
    protected final BiFunction<T, Scriptable, N> converter;

    public BoxCoercer(Class<T> boxedType, BiFunction<T, Scriptable, N> converter) {
        this.boxedType = boxedType;
        this.converter = converter;
    }

    @Override
    public Class<?> getId() {
        return boxedType;
    }

    @Override
    public N coerceValue(T value, Scriptable scope) {
        return converter.apply(value, scope);
    }

    public static <T, N extends ScriptableObject> BoxCoercer<T, N> wrap(Class<T> boxType, Function<T, N> wrapper) {
        return new BoxCoercer<>(boxType, (value, scope) -> {
            N coercedValue = wrapper.apply(value);
            coercedValue.setParentScope(scope);
            return coercedValue;
        });
    }

}
