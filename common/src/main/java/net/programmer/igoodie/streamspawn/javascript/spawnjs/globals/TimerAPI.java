package net.programmer.igoodie.streamspawn.javascript.spawnjs.globals;

import net.programmer.igoodie.streamspawn.javascript.JavascriptEngine;
import net.programmer.igoodie.streamspawn.javascript.base.ScopeInstallable;
import org.mozilla.javascript.*;

import java.util.*;

public class TimerAPI implements ScopeInstallable {

    public void install(ScriptableObject scope) {
        scope.defineProperty("setTimeout", new SetTimeout(), ScriptableObject.CONST);
        scope.defineProperty("setInterval", new SetInterval(), ScriptableObject.CONST);
        scope.defineProperty("clearTimeout", new ClearTask(), ScriptableObject.CONST);
        scope.defineProperty("clearInterval", new ClearTask(), ScriptableObject.CONST);
    }

    protected final Timer timer = new Timer("JS-TimerAPI-" + UUID.randomUUID());
    protected long nextTaskId = 0L;
    protected Map<Long, Task> tasks = new WeakHashMap<>();

    protected class Task extends TimerTask {

        protected final long taskId;
        protected final boolean isPeriodic;
        protected final BaseFunction function;
        protected final Scriptable scope;
        protected final Scriptable thisObj;

        public Task(long taskId, boolean isPeriodic, BaseFunction function, Scriptable scope, Scriptable thisObj) {
            this.taskId = taskId;
            this.isPeriodic = isPeriodic;
            this.function = function;
            this.scope = scope;
            this.thisObj = thisObj;
        }

        @Override
        public void run() {
            Context cx = JavascriptEngine.CONTEXT.get();
            function.call(cx, scope, thisObj, new Object[0]);
            if (!this.isPeriodic) tasks.remove(taskId);
        }

    }

    /**
     * <h1>Supported Syntax</h1>
     * <pre>{@code
     * ❌ setTimeout(code)
     * ❌ setTimeout(code, delay)
     *
     * ❌ setTimeout(functionRef)
     * ✅ setTimeout(functionRef, delay)
     * ❌ setTimeout(functionRef, delay, param1)
     * ❌ setTimeout(functionRef, delay, param1, param2)
     * ❌ setTimeout(functionRef, delay, param1, param2, …, paramN)
     * }</pre>
     * <p>
     * {@see <a href="https://developer.mozilla.org/en-US/docs/Web/API/Window/setTimeout">MDN Docs</a>}
     */
    protected class SetTimeout extends BaseFunction {

        @Override
        public Long call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
            if (args.length != 2) throw new IllegalArgumentException("Expected two arguments");
            if (!(args[0] instanceof BaseFunction)) throw new IllegalArgumentException("Expected a callback");
            if (!(args[1] instanceof Number)) throw new IllegalArgumentException("Expected timeout ms");

            long taskId = nextTaskId++;
            Task task = new Task(taskId, false, ((BaseFunction) args[0]), scope, thisObj);

            tasks.put(taskId, task);
            timer.schedule(task, ((Number) args[1]).intValue());
            return taskId;
        }

    }

    /**
     * <h1>Supported Syntax</h1>
     * <pre>{@code
     * ❌ setInterval(code)
     * ❌ setInterval(code, delay)
     *
     * ❌ setInterval(func)
     * ✅ setInterval(func, delay)
     * ❌ setInterval(func, delay, arg1)
     * ❌ setInterval(func, delay, arg1, arg2)
     * ❌ setInterval(func, delay, arg1, arg2, ..., argN)
     * }</pre>
     * <p>
     * {@see <a href="https://developer.mozilla.org/en-US/docs/Web/API/Window/setInterval">MDN Docs</a>}
     */
    protected class SetInterval extends BaseFunction {

        @Override
        public Long call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
            if (args.length != 2) throw new IllegalArgumentException("Expected two arguments");
            if (!(args[0] instanceof BaseFunction)) throw new IllegalArgumentException("Expected a callback");
            if (!(args[1] instanceof Number)) throw new IllegalArgumentException("Expected delay ms");

            long taskId = nextTaskId++;
            Task task = new Task(taskId, true, ((BaseFunction) args[0]), scope, thisObj);

            tasks.put(taskId, task);
            timer.scheduleAtFixedRate(task, 0, ((Number) args[1]).intValue());
            return taskId;
        }

    }

    /**
     * <h1>Supported Syntax</h1>
     * <pre>{@code
     * ✅ clearTimeout(timeoutID)
     * ✅ clearInterval(intervalID)
     * }</pre>
     * <p>
     * {@see <a href="https://developer.mozilla.org/en-US/docs/Web/API/Window/clearTimeout">MDN Docs for clearTimeout</a>}
     * {@see <a href="https://developer.mozilla.org/en-US/docs/Web/API/Window/clearInterval">MDN Docs for clearInterval</a>}
     */
    protected class ClearTask extends BaseFunction {

        @Override
        public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
            if (args.length != 1) throw new IllegalArgumentException("Expected one argument");
            if (!(args[0] instanceof Number)) throw new IllegalArgumentException("Expected timeout id");

            long taskId = ((Number) args[0]).longValue();
            tasks.get(taskId).cancel();
            tasks.remove(taskId);
            return Undefined.instance;
        }

    }

}
