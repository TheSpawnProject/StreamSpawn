package net.programmer.igoodie.streamspawn.javascript;

import net.programmer.igoodie.goodies.registry.Registry;
import net.programmer.igoodie.streamspawn.javascript.coercer.BoxCoercer;
import net.programmer.igoodie.streamspawn.javascript.coercer.Coercer;
import net.programmer.igoodie.streamspawn.javascript.coercer.JSONCoercer;
import net.programmer.igoodie.streamspawn.javascript.spawnjs.modules.network.BufferHost;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.WrapFactory;

import java.nio.ByteBuffer;

public class JavascriptWrapFactory extends WrapFactory {

    public final Registry<Class<?>, Coercer<?, ?>> COERCERS = new Registry<>(
            JSONCoercer.Object.INSTANCE,
            JSONCoercer.Array.INSTANCE,
            BoxCoercer.wrap(ByteBuffer.class, buf -> new BufferHost(buf.array()))
    );

    public JavascriptWrapFactory() {
        super();
        this.setJavaPrimitiveWrap(false);
    }

    @Override
    public Object wrap(Context cx, Scriptable scope, Object obj, Class<?> staticType) {
        ScriptableObject coercedValue = this.coerce(staticType, obj);
        if (coercedValue != null) return coercedValue;
        return super.wrap(cx, scope, obj, staticType);
    }

    public <T> ScriptableObject coerce(Class<?> type, T value) {
        @SuppressWarnings("unchecked")
        Coercer<T, ?> coercer = (Coercer<T, ?>) COERCERS.get(type);
        if (coercer == null) return null;
        return coercer.coerceValue(value);
    }

}
