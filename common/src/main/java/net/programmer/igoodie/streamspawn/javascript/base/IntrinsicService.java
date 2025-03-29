package net.programmer.igoodie.streamspawn.javascript.base;

import net.programmer.igoodie.streamspawn.javascript.JavascriptEngine;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;

import java.util.Arrays;

// TODO: What to do when a ServiceObject throws an Exception?
@Deprecated
public abstract class IntrinsicService extends ScriptHost {

    public abstract void begin();

    public abstract void terminate();

    // XXX: Why the hell is this here?
    public interface Listener {
        void call(Object... args);
    }

    // XXX: Why the hell is this here?
    protected Listener makeCoercible(Function callback) {
        Scriptable scope = this.getParentScope();

        return (args) -> {
            Context cx = JavascriptEngine.CONTEXT.get();
            callback.call(cx, scope, null, Arrays.stream(args)
                    .map(JavascriptEngine::coerce)
                    .toArray());
        };
    }

}
