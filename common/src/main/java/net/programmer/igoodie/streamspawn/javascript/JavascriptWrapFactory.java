package net.programmer.igoodie.streamspawn.javascript;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.WrapFactory;

public class JavascriptWrapFactory extends WrapFactory {

    // TODO: Move coercers here

    public JavascriptWrapFactory() {
        super();
        this.setJavaPrimitiveWrap(false);
    }

    @Override
    public Object wrap(Context cx, Scriptable scope, Object obj, Class<?> staticType) {
        return super.wrap(cx, scope, obj, staticType);
    }

}
