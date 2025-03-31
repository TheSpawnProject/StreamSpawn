package net.programmer.igoodie.streamspawn.javascript.spawnjs.modules.network;

import net.programmer.igoodie.goodies.util.accessor.ArrayAccessor;
import net.programmer.igoodie.streamspawn.javascript.base.ScriptHost;
import net.programmer.igoodie.streamspawn.javascript.spawnjs.SpawnJSExceptions;
import net.programmer.igoodie.streamspawn.javascript.util.Listeners;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.annotations.JSConstructor;
import org.mozilla.javascript.annotations.JSFunction;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;

public class TcpSocket extends ScriptHost {

    protected String host;
    protected int port;

    protected Socket socket;
    protected BufferHost buffer;
    protected InputStream inputStream;
    protected OutputStream outputStream;
    protected ReadLoopThread thread;

    protected Listeners<Event, Listeners.GenericListener> listeners = new Listeners<>();

    public TcpSocket() {}

    public TcpSocket(String host, int port, BufferHost buffer) {
        this.host = host;
        this.port = port;
        this.socket = new Socket();
        this.buffer = buffer;
    }

    @JSConstructor
    public static TcpSocket constructor(Context cx, Object[] args, Function ctor, boolean inNewExpr) {
        ArrayAccessor<Object> argsAccessor = ArrayAccessor.of(args);
        Object arg0 = argsAccessor.get(0).orElse(null);
        Object arg1 = argsAccessor.get(1).orElse(null);
        Object arg2 = argsAccessor.get(2).orElse(new BufferHost(new byte[1024]));

        Scriptable scope = ctor.getParentScope();

        if (arg0 instanceof String host) {
            if (arg1 instanceof Integer port) {
                if (arg2 instanceof BufferHost buffer) {
                    return bindToScope(new TcpSocket(host, port, buffer), scope);
                }
            }
        }

        throw SpawnJSExceptions.invalidArguments(new TcpSocket(), args, ctor);
    }

    @Override
    public String getClassName() {
        return "TcpSocket";
    }

    @JSFunction
    public void connect() {
        if (this.thread != null) return;
        this.thread = new ReadLoopThread();
        this.thread.start();
    }

    @JSFunction
    public void disconnect() {
        if (this.thread == null) return;
        this.thread.interrupt();

        try {
            if (inputStream != null) inputStream.close();
            if (outputStream != null) outputStream.close();
            if (socket != null) socket.close();

            // TODO: Maybe invoke an event here?

        } catch (Exception e) {
            listeners.invoke(Event.ERROR, l -> l.call(
                    e.getClass().getSimpleName(), e.getMessage()
            ));
        }
    }


    @JSFunction
    public void write(BufferHost buffer) {
        try {
            // TODO: Make this queue/flush-able, otherwise it'll block lol
            this.outputStream.write(buffer.buffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @JSFunction
    public static void on(Context cx, Scriptable thisObj, Object[] args, Function function) {
        ArrayAccessor<Object> argsAccessor = ArrayAccessor.of(args);
        Object arg0 = argsAccessor.get(0).orElse(null);
        Object arg1 = argsAccessor.get(1).orElse(null);

        TcpSocket hostObj = (TcpSocket) thisObj;

        if (arg0 instanceof String eventName) {
            if (arg1 instanceof Function listener) {
                try {
                    Event event = Event.valueOf(eventName.toUpperCase());
                    hostObj.listeners.subscribe(event, eventArgs
                            -> listener.call(cx, function.getParentScope(), thisObj, eventArgs));
                    return;
                } catch (Exception ignored) {
                    throw new IllegalArgumentException("Invalid event name: " + eventName);
                }
            }
        }

        throw SpawnJSExceptions.invalidArguments(thisObj, args, function);
    }

    public class ReadLoopThread extends Thread {

        @Override
        public void run() {
            try {
                InetSocketAddress socketAddress = new InetSocketAddress(host, port);

                if (socketAddress.isUnresolved()) {
                    throw new RuntimeException("Failed to resolve given address...");
                }

                InetAddress netAddress = socketAddress.getAddress();
                int type = netAddress instanceof Inet4Address ? 4
                        : netAddress instanceof Inet6Address ? 6
                        : -1;

                // TODO: Handle options here

                listeners.invoke(Event.LOOKUP, l -> l.call(
                        type, netAddress.getHostAddress(), TcpSocket.this.host
                ));

                socket.connect(socketAddress);
                inputStream = socket.getInputStream();
                outputStream = socket.getOutputStream();

                listeners.invoke(Event.CONNECT, l -> l.call(
                        type, netAddress.getHostAddress(), TcpSocket.this.host
                ));

                int readLength;

                while ((readLength = inputStream.read(buffer.buffer)) != -1) {
                    int readLengthArg = readLength;
                    listeners.invoke(Event.DATA, l -> l.call(
                            buffer, readLengthArg
                    ));
                }

                listeners.invoke(Event.END, Listeners.GenericListener::call);

            } catch (Exception e) {
                listeners.invoke(Event.ERROR, l -> l.call(
                        e.getMessage()
                ));
            }
        }

    }

    public enum Event {
        CONNECT,
        DATA,
        END,
        ERROR,
        LOOKUP
    }

}
