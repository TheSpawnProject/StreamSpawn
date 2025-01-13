package net.programmer.igoodie.streamspawn.javascript.global;

import net.programmer.igoodie.streamspawn.javascript.util.NativeFormat;
import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.Undefined;

import java.util.Arrays;
import java.util.stream.Collectors;

public class PrintFn extends BaseFunction {

    @Override
    public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        String msg = Arrays.stream(args)
                .map(NativeFormat::toString)
                .collect(Collectors.joining(", ", "[", "]"));
        System.out.println(msg);
        return Undefined.instance;
    }

}
