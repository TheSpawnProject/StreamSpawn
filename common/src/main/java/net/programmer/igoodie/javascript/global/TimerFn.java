package net.programmer.igoodie.javascript.global;

import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

import java.util.Timer;
import java.util.TimerTask;

public class TimerFn extends BaseFunction {

    protected Timer timer = new Timer();
    protected boolean repeats;

    public TimerFn() {}

    public TimerFn(boolean repeats) {
        this.repeats = repeats;
    }

    public static class CallbackTask extends TimerTask {

        protected final BaseFunction function;
        protected final Context context;
        protected final Scriptable scope;
        protected final Scriptable thisObj;

        public CallbackTask(BaseFunction function, Context context, Scriptable scope, Scriptable thisObj) {
            this.function = function;
            this.context = context;
            this.scope = scope;
            this.thisObj = thisObj;
        }

        @Override
        public void run() {
            function.call(context, scope, thisObj, new Object[0]);
        }

    }

    @Override
    public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        if (args.length != 2) throw new IllegalArgumentException("Expected two arguments");
        if (!(args[0] instanceof BaseFunction)) throw new IllegalArgumentException("Expected a callback");
        if (!(args[1] instanceof Number)) throw new IllegalArgumentException("Expected timeout ms");

        CallbackTask task = new CallbackTask(((BaseFunction) args[0]), cx, scope, thisObj);

        if (repeats)
            timer.scheduleAtFixedRate(task, 0, ((Number) args[1]).intValue());
        else
            timer.schedule(task, ((Number) args[1]).intValue());

        return super.call(cx, scope, thisObj, args);
    }

}
