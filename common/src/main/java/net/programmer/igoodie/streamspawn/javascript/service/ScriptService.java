package net.programmer.igoodie.streamspawn.javascript.service;

import net.programmer.igoodie.goodies.util.accessor.ArrayAccessor;
import net.programmer.igoodie.streamspawn.javascript.base.ScriptHost;
import net.programmer.igoodie.streamspawn.javascript.coercer.CoercibleFunction;
import net.programmer.igoodie.streamspawn.javascript.spawnjs.SpawnJSExceptions;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.annotations.JSFunction;

import java.util.ArrayList;
import java.util.List;

@Deprecated
public class ScriptService extends ScriptHost implements ScriptServiceImpl {

    protected List<CoercibleFunction> onStartCallback = new ArrayList<>();
    protected List<CoercibleFunction> onTerminateCallback = new ArrayList<>();

    @Override
    public String getClassName() {
        return "AbstractService";
    }

    public final void begin() {
        this.beginImpl();

        if (this.onStartCallback != null) {
            this.onStartCallback.forEach(CoercibleFunction::call);
        }
    }

    public final void terminate() {
        this.terminateImpl();

        if (this.onTerminateCallback != null) {
            this.onTerminateCallback.forEach(CoercibleFunction::call);
        }
    }

    @JSFunction("addServiceListener")
    public static boolean _addServiceListener(Context cx, Scriptable thisObj, Object[] args, Function function) {
        ArrayAccessor<Object> argsAccessor = ArrayAccessor.of(args);
        Object arg0 = argsAccessor.get(0).orElse(null);
        Object arg1 = argsAccessor.get(1).orElse(null);

        ScriptService serviceObj = (ScriptService) thisObj;

        if (arg0 instanceof String eventName) {
            if (arg1 instanceof Function cb) {
                CoercibleFunction listener = CoercibleFunction.makeCoercible(thisObj.getParentScope(), cb);

                if (eventName.equals("service-starting")) {
                    return serviceObj.onStartCallback.add(listener);
                } else if (eventName.equals("service-terminating")) {
                    return serviceObj.onTerminateCallback.add(listener);
                }
            }
        }

        throw SpawnJSExceptions.invalidArguments(thisObj, args, function);
    }

}
