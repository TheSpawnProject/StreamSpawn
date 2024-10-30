package net.programmer.igoodie.javascript.global;

import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

import java.util.Timer;
import java.util.TimerTask;

public class SetTimeoutFn extends BaseFunction {

    protected Timer timer = new Timer();

    public static class RunnableTask extends TimerTask {

        protected final Runnable runnable;

        public RunnableTask(Runnable runnable) {this.runnable = runnable;}

        @Override
        public void run() {
            runnable.run();
        }

    }

    @Override
    public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        if (args.length != 2) throw new IllegalArgumentException("Expected two arguments");
        if (!(args[0] instanceof BaseFunction)) throw new IllegalArgumentException("Expected a callback");
        if (!(args[1] instanceof Number)) throw new IllegalArgumentException("Expected timeout ms");

        RunnableTask task = new RunnableTask(() -> ((BaseFunction) args[0]).call(cx, scope, thisObj, new Object[0]));
        timer.schedule(task, ((Number) args[1]).intValue());

        return super.call(cx, scope, thisObj, args);
    }

}
