package net.programmer.igoodie.javascript.base;

import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.Undefined;

import java.util.Arrays;

public class PrintFn extends BaseFunction {

    @Override
    public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        System.out.println(Arrays.toString(args));
        return Undefined.instance;
    }

}
