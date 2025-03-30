package net.programmer.igoodie.streamspawn.javascript.coercer;

import org.mozilla.javascript.ScriptableObject;

import java.util.function.Function;

public class BoxCoercer<T, N extends ScriptableObject> extends Coercer<T, N> {

    protected final Class<T> boxedType;
    protected final Function<T, N> converter;

    public BoxCoercer(Class<T> boxedType, Function<T, N> converter) {
        this.boxedType = boxedType;
        this.converter = converter;
    }

    @Override
    public Class<?> getId() {
        return boxedType;
    }

    @Override
    public N coerceValue(T value) {
        return null;
    }

    public static <T, N extends ScriptableObject> BoxCoercer<T, N> wrap(Class<T> boxType, Function<T, N> wrapper) {
        return new BoxCoercer<T, N>(boxType, wrapper);
    }

}
