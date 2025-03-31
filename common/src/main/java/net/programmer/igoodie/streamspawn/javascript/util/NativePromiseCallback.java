package net.programmer.igoodie.streamspawn.javascript.util;

import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;

import java.util.function.BiConsumer;

public class NativePromiseCallback<R, E> extends BaseFunction {

    protected final BiConsumer<Resolve<R>, Reject<E>> handler;

    public NativePromiseCallback(BiConsumer<Resolve<R>, Reject<E>> handler) {
        this.handler = handler;
    }

    @Override
    public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        Function nativeResolve = (Function) args[0];
        Function nativeReject = (Function) args[1];

        handler.accept(
                result -> nativeResolve.call(cx, scope, thisObj, new Object[]{result}),
                error -> nativeReject.call(cx, scope, thisObj, new Object[]{error})
        );

        return super.call(cx, scope, thisObj, args);
    }

    public interface Resolve<T> {
        void resolve(T result);
    }

    public interface Reject<T> {
        void reject(T error);
    }

}
