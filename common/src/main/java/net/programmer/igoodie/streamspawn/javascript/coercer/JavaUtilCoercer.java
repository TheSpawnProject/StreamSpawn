package net.programmer.igoodie.streamspawn.javascript.coercer;

import net.programmer.igoodie.streamspawn.javascript.JavascriptEngine;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

public class JavaUtilCoercer {

    public static class Map extends Coercer<java.util.Map<?, ?>, NativeObject> {

        public static final Map INSTANCE = new Map();

        @Override
        public Class<?> getId() {
            return java.util.Map.class;
        }

        @Override
        public NativeObject coerceValue(java.util.Map<?, ?> javaMap, Scriptable scope) {
            NativeObject nativeObject = new NativeObject();
            nativeObject.setParentScope(scope);

            JavascriptEngine.CONTEXT.get(); // <- Ensure Context is available in this thread

            for (java.util.Map.Entry<?, ?> entry : javaMap.entrySet()) {
                Object key = entry.getKey();
                Object value = entry.getValue();
                Object coercedValue = Context.javaToJS(value, nativeObject);
                nativeObject.defineProperty((String) key, coercedValue, ScriptableObject.CONST);
            }

            return nativeObject;
        }

    }

}
