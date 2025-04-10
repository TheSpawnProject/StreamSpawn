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
        cx.setWrapFactory(JavascriptEngine.WRAP_FACTORY);
        Object lock = LOCK.get();
        cx.seal(lock);
        return cx;
    });

    public static final JavascriptWrapFactory WRAP_FACTORY = new JavascriptWrapFactory();

    public static <V> V unsafe_useContext(Function<Context, V> consumer) {
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

    public static ScriptableObject createTopLevelScope() {
        return CONTEXT.get().initSafeStandardObjects();
    }

    public static Scriptable forkScope(Scriptable scope) {
        Scriptable forkedScope = CONTEXT.get().newObject(scope);
        forkedScope.setParentScope(scope);
        return forkedScope;
    }

    public static Object eval(Scriptable scope, String source) {
        return CONTEXT.get().evaluateString(scope, source, "<root>", 1, null);
    }

}
