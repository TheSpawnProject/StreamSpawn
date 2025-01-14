package net.programmer.igoodie.streamspawn.javascript.coercer;

import net.programmer.igoodie.streamspawn.javascript.coercer.transformers.JSONTransformer;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class JavascriptCoercer {

    private static final Map<Class<?>, Function<?, ?>> TRANSFORMER_REGISTRY = new HashMap<>();

    static {
        registerTransformer(JSONObject.class, JSONTransformer::toObject);
        registerTransformer(JSONArray.class, JSONTransformer::toArray);
    }

    public static <F, T> Function<F, T> registerTransformer(Class<F> coerceType, Function<F, T> strategy) {
        TRANSFORMER_REGISTRY.put(coerceType, strategy);
        return strategy;
    }

    public static Object coerce(Object value) {
        @SuppressWarnings("unchecked")
        Function<Object, ?> converter = (Function<Object, ?>) TRANSFORMER_REGISTRY.get(value.getClass());
        if (converter == null) return value;
        return converter.apply(value);
    }

}
