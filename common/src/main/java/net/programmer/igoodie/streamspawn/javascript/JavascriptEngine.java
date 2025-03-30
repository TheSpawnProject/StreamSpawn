package net.programmer.igoodie.streamspawn.javascript;

import net.programmer.igoodie.goodies.registry.Registry;
import net.programmer.igoodie.streamspawn.javascript.coercer.BoxCoercer;
import net.programmer.igoodie.streamspawn.javascript.coercer.Coercer;
import net.programmer.igoodie.streamspawn.javascript.coercer.JSONCoercer;
import net.programmer.igoodie.streamspawn.javascript.spawnjs.modules.network.BufferHost;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import java.nio.ByteBuffer;
import java.util.function.Function;

public class JavascriptEngine {

    private static final ThreadLocal<Object> LOCK = ThreadLocal.withInitial(Object::new);

    public static final ThreadLocal<Context> CONTEXT = ThreadLocal.withInitial(() -> {
        Context cx = Context.enter();
//        cx.setInterpretedMode(true);
//        cx.setMaximumInterpreterStackDepth(255);
        cx.setLanguageVersion(Context.VERSION_ES6);
        cx.setWrapFactory(new JavascriptWrapFactory());
        Object lock = LOCK.get();
        cx.seal(lock);
        return cx;
    });

    public static final Registry<Class<?>, Coercer<?, ?>> COERCERS = new Registry<>(
            JSONCoercer.Object.INSTANCE,
            JSONCoercer.Array.INSTANCE,
            BoxCoercer.wrap(ByteBuffer.class, buf -> new BufferHost(buf.array()))
    );

    private static <V> V unsafe_useContext(Function<Context, V> consumer) {
        Context cx = CONTEXT.get();

        if (!cx.isSealed()) {
            return consumer.apply(cx);
        }

        Object lock = LOCK.get();
        cx.unseal(lock);
        V result = consumer.apply(cx);
        cx.seal(lock);

        return result;
    }

    public static ScriptableObject createObject() {
        return createObject(null);
    }

    public static ScriptableObject createObject(Scriptable parentScope) {
        return unsafe_useContext(cx -> {
            ScriptableObject scope = cx.initSafeStandardObjects();
            if (parentScope != null) scope.setParentScope(parentScope);
            return scope;
        });
    }

    public static <T> Object coerce(T value) {
        @SuppressWarnings("unchecked")
        Coercer<T, ?> coercer = (Coercer<T, ?>) COERCERS.get(value.getClass());
        if (coercer == null) return value;
        return coercer.coerceValue(value);
    }

    public static Object eval(ScriptableObject scope, String source) {
        return CONTEXT.get().evaluateString(scope, source, "<root>", 1, null);
    }

}
