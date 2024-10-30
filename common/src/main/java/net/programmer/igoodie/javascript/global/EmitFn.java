package net.programmer.igoodie.javascript.global;

import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.javascript.util.NativeGoodieFormat;
import org.mozilla.javascript.*;

public class EmitFn extends BaseFunction {

    @Override
    public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        if (args.length != 2) throw new IllegalArgumentException("Expected two arguments");
        if (!(args[0] instanceof String)) throw new IllegalArgumentException("Expected an Event Name");
        if (!(args[1] instanceof NativeObject)) throw new IllegalArgumentException("Expected an Event Args");

        String eventName = (String) args[0];
        GoodieObject eventArgs = NativeGoodieFormat.INSTANCE.writeToGoodie(((NativeObject) args[1]));

        System.out.println(eventName + " -> " + eventArgs);

        return Undefined.instance;
    }

}
