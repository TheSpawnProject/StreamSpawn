package net.programmer.igoodie.javascript.network;

import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.Undefined;

import java.util.List;

public class RegisterSocketFn extends BaseFunction {

    private final List<SocketHost> registryRef;

    public RegisterSocketFn(List<SocketHost> registryRef) {
        this.registryRef = registryRef;
    }

    @Override
    public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        if (args.length != 1) throw new IllegalArgumentException("Expected only one argument");
        if (!(args[0] instanceof SocketHost)) throw new IllegalArgumentException("Expected a Socket");

        registryRef.add(((SocketHost) args[0]));

        return Undefined.instance;
    }

}
