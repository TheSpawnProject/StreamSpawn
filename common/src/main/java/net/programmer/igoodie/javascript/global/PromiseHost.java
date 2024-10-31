package net.programmer.igoodie.javascript.global;

import net.programmer.igoodie.javascript.JavascriptEngine;
import net.programmer.igoodie.javascript.base.HostObject;
import org.mozilla.javascript.*;
import org.mozilla.javascript.annotations.JSConstructor;
import org.mozilla.javascript.annotations.JSFunction;

import java.util.LinkedList;
import java.util.Queue;

public class PromiseHost extends HostObject {

    protected Function executor;
    protected Scriptable scope;

    protected Object result = Undefined.instance;
    protected PromiseState state = PromiseState.PENDING;

    protected Queue<Function> fulfillQueue = new LinkedList<>();
    protected Queue<Function> rejectQueue = new LinkedList<>();

    @Override
    public String getClassName() {
        return "Bromise";
    }

    public PromiseHost() {}

    @JSConstructor
    public PromiseHost(Function executor) {
        this.executor = executor;
        this.scope = executor.getParentScope();

        Context context = JavascriptEngine.CONTEXT.get();
        Object[] args = {new Resolve(), new Reject()};
        executor.call(context, scope, null, args);
    }

    public class Resolve extends BaseFunction {

        @Override
        public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
            PromiseHost.this.state = PromiseState.FULFILLED;
            PromiseHost.this.result = args[0];
            executeFulfill();
            return Undefined.instance;
        }

    }

    public class Reject extends BaseFunction {

        @Override
        public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
            PromiseHost.this.state = PromiseState.REJECTED;
            PromiseHost.this.result = args[0];
            executeReject();
            return Undefined.instance;
        }

    }

    protected void executeFulfill() {
        if (this.state == PromiseState.FULFILLED) {
            while (!fulfillQueue.isEmpty()) {
                try {
                    Function onFulfilled = fulfillQueue.poll();
                    Context context = JavascriptEngine.CONTEXT.get();
                    Object[] args = {this.result};
                    this.result = onFulfilled.call(context, scope, null, args);

                } catch (Exception e) {
                    this.state = PromiseState.REJECTED;
                    this.result = e;
                    executeReject();
                    break;
                }
            }

        }
    }

    protected void executeReject() {
        if (this.state == PromiseState.REJECTED) {
            if (rejectQueue.isEmpty()) {
                throw new RuntimeException("Unhandled promise exception", ((JavaScriptException) this.result));
            }

            while (!rejectQueue.isEmpty()) {
                try {
                    Function onRejected = rejectQueue.poll();
                    Context context = JavascriptEngine.CONTEXT.get();
                    Object[] args = {this.result};
                    this.result = onRejected.call(context, scope, null, args);

                } catch (Exception e) {
                    this.result = e;
                }
            }
        }
    }

    @JSFunction("then")
    public PromiseHost thenFn(Function onFulfilled) {
        this.fulfillQueue.offer(onFulfilled);

        if (this.state == PromiseState.FULFILLED) {
            executeFulfill();
        }

        return this;
    }

    @JSFunction("catch")
    public PromiseHost catchFn(Function onRejected) {
        this.rejectQueue.offer(onRejected);

        if (this.state == PromiseState.REJECTED) {
            executeReject();
        }

        return this;
    }

    enum PromiseState {
        PENDING,
        FULFILLED,
        REJECTED
    }

}
