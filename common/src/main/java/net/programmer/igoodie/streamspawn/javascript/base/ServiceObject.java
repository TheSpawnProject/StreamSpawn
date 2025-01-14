package net.programmer.igoodie.streamspawn.javascript.base;

import net.programmer.igoodie.streamspawn.javascript.JavascriptEngine;
import net.programmer.igoodie.streamspawn.javascript.coercer.JavascriptCoercer;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;

import java.util.Arrays;

// TODO: What to do when a ServiceObject throws an Exception?
public abstract class ServiceObject extends HostObject {

    public abstract void begin();

    public abstract void terminate();

    public interface Listener {
        void call(Object... args);
    }

    protected Listener bindListener(Function callback) {
        Scriptable scope = getParentScope();

        return (args) -> {
            Context context = JavascriptEngine.CONTEXT.get();
            Object[] coercedArgs = Arrays.stream(args)
                    .map(JavascriptCoercer::coerce)
                    .toArray();
            callback.call(context, scope, null, coercedArgs);
        };
    }

}
