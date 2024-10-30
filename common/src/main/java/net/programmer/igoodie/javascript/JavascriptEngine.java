package net.programmer.igoodie.javascript;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.ScriptableObject;

import java.util.function.Function;

public class JavascriptEngine {

    private static final ThreadLocal<Object> LOCK = ThreadLocal.withInitial(Object::new);

    public static final ThreadLocal<Context> CONTEXT = ThreadLocal.withInitial(() -> {
        Context context = Context.enter();
        context.setOptimizationLevel(-1);
        context.setMaximumInterpreterStackDepth(255);
        context.setLanguageVersion(Context.VERSION_ES6);
        Object lock = LOCK.get();
        context.seal(lock);
        return context;
    });

    private static <V> V unsafe_useContext(Function<Context, V> consumer) {
        Context context = CONTEXT.get();

        if (!context.isSealed()) {
            return consumer.apply(context);
        }

        Object lock = LOCK.get();
        context.unseal(lock);
        V result = consumer.apply(context);
        context.seal(lock);

        return result;
    }

    public static ScriptableObject createScope() {
        return unsafe_useContext(Context::initSafeStandardObjects);
    }

    public static Object eval(ScriptableObject scope, String source) {
        return CONTEXT.get().evaluateString(scope, source, "<root>", 1, null);
    }

}
