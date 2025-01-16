package net.programmer.igoodie.streamspawn.javascript.global;

import net.programmer.igoodie.streamspawn.integration.base.Integration;
import net.programmer.igoodie.streamspawn.javascript.base.IntrinsicService;
import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.Undefined;

import java.util.Map;

public class RegisterServiceFn extends BaseFunction {

    protected final Map<String, Integration> integrationRegistry;

    public RegisterServiceFn(Map<String, Integration> integrationRegistry) {
        this.integrationRegistry = integrationRegistry;
    }

    @Override
    public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        if (args.length != 1) throw new IllegalArgumentException("Expected only one argument");
        if (!(args[0] instanceof IntrinsicService)) throw new IllegalArgumentException("Expected a Service");

        String integrationId = (String) scope.get("integrationId", scope);
        Integration integration = integrationRegistry.get(integrationId);
        integration.services.add(((IntrinsicService) args[0]));

        return Undefined.instance;
    }

}
