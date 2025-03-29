package net.programmer.igoodie.streamspawn.javascript.coercer;

import net.programmer.igoodie.streamspawn.javascript.JavascriptEngine;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;

import java.util.Arrays;

public interface CoercibleFunction {

    void call(Object... args);

    /**
     * Creates a function, that takes in arguments from Java Realm,
     * and turns them into Javascript Realm before invoking within current Context
     *
     * @param scope    Scope to use while calling the function
     * @param callback Callback to be transformed
     * @return A Coercible Function
     */
    static CoercibleFunction makeCoercible(Scriptable scope, Function callback) {
        return (args) -> {
            Context cx = JavascriptEngine.CONTEXT.get();
            callback.call(cx, scope, null, Arrays.stream(args)
                    .map(JavascriptEngine::coerce)
                    .toArray());
        };
    }

}
