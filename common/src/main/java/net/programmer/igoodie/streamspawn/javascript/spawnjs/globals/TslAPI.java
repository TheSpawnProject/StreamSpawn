package net.programmer.igoodie.streamspawn.javascript.spawnjs.globals;

import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.goodies.util.accessor.ArrayAccessor;
import net.programmer.igoodie.streamspawn.javascript.base.ScopeInstallable;
import net.programmer.igoodie.streamspawn.javascript.format.NativeGoodieFormat;
import net.programmer.igoodie.streamspawn.javascript.spawnjs.SpawnJSExceptions;
import org.mozilla.javascript.*;

public class TslAPI implements ScopeInstallable {

    @Override
    public void install(Scriptable scope) {
        ScriptableObject.defineProperty(scope, "emit", new EmitFn(), ScriptableObject.CONST);
    }

    public static class EmitFn extends BaseFunction {

        @Override
        public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
            ArrayAccessor<Object> argsAccessor = ArrayAccessor.of(args);
            Object arg0 = argsAccessor.get(0).orElse(null);
            Object arg1 = argsAccessor.get(1).orElse(null);

            if (arg0 instanceof String eventName) {
                if (arg1 instanceof NativeObject nativeEventArgs) {
                    GoodieObject eventArgs = NativeGoodieFormat.INSTANCE.writeToGoodie(nativeEventArgs);

                    // TODO: Queue an actual TSL event instead of logging
                    System.out.println("TODO: Emit; " + eventName + " -> " + eventArgs);
                    return Undefined.instance;
                }
            }

            throw SpawnJSExceptions.invalidArguments(this, args, ((Function) thisObj));
        }

    }

}
