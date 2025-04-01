package net.programmer.igoodie.streamspawn.javascript.streamspawn.modules.tsl;

import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.goodies.util.accessor.ArrayAccessor;
import net.programmer.igoodie.streamspawn.init.ModTSL;
import net.programmer.igoodie.streamspawn.javascript.format.NativeGoodieFormat;
import net.programmer.igoodie.streamspawn.javascript.spawnjs.SpawnJSExceptions;
import net.programmer.igoodie.tsl.runtime.event.TSLEventContext;
import org.mozilla.javascript.*;

public class CreateEmitterFn extends BaseFunction {

    @Override
    public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        return new CreateEmitterFn.EmitFn();
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

                    TSLEventContext ctx = new TSLEventContext(ModTSL.TSL, eventName);
                    ctx.setTarget("Minecraft:DummyPlayer");
                    ctx.getEventArgs().putAll(eventArgs);
                    ModTSL.triggerEvent(ctx);

                    return Undefined.instance;
                }
            }

            throw SpawnJSExceptions.invalidArguments(this, args, ((Function) thisObj));
        }

    }

}
