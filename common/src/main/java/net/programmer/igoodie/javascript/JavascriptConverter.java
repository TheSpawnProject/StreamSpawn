package net.programmer.igoodie.javascript;

import net.programmer.igoodie.javascript.util.NativeFormat;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class JavascriptConverter {

    private static final Map<Class<?>, Function<?, ?>> CONVERTER_REGISTRY = new HashMap<>();

    static {
        register(JSONObject.class, NativeFormat.JSON::toObject);
        register(JSONArray.class, NativeFormat.JSON::toArray);
    }

    public static <F, T> Function<F, T> register(Class<F> from, Function<F, T> converter) {
        CONVERTER_REGISTRY.put(from, converter);
        return converter;
    }

    public static Object convertToJSRealm(Object value) {
        @SuppressWarnings("unchecked")
        Function<Object, ?> converter = (Function<Object, ?>) CONVERTER_REGISTRY.get(value.getClass());
        if (converter == null) return value;
        return converter.apply(value);
    }

}
