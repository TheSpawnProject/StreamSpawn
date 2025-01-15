package net.programmer.igoodie.streamspawn.javascript;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import java.util.function.Function;

public class JavascriptEngine {

    private static final ThreadLocal<Object> LOCK = ThreadLocal.withInitial(Object::new);

    public static final ThreadLocal<Context> CONTEXT = ThreadLocal.withInitial(() -> {
        Context cx = Context.enter();
//        cx.setInterpretedMode(true);
//        cx.setMaximumInterpreterStackDepth(255);
        cx.setLanguageVersion(Context.VERSION_ES6);
        Object lock = LOCK.get();
        cx.seal(lock);
        return cx;
    });

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

    public static ScriptableObject createScope() {
        return createScope(null);
    }

    public static ScriptableObject createScope(Scriptable parentScope) {
        return unsafe_useContext(cx -> {
            ScriptableObject scope = cx.initSafeStandardObjects();
            if (parentScope != null) scope.setParentScope(parentScope);
            return scope;
        });
    }

    public static <T extends Scriptable> void defineClass(Scriptable scope, Class<T> clazz) {
        try {
            ScriptableObject.defineClass(scope, clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Object eval(ScriptableObject scope, String source) {
        return CONTEXT.get().evaluateString(scope, source, "<root>", 1, null);
    }

}
