package net.programmer.igoodie.streamspawn.javascript.spawnjs.network.hosts;

import net.programmer.igoodie.goodies.util.accessor.ArrayAccessor;
import net.programmer.igoodie.streamspawn.javascript.base.ServiceObject;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.annotations.JSConstructor;
import org.mozilla.javascript.annotations.JSFunction;
import org.mozilla.javascript.annotations.JSGetter;
import org.mozilla.javascript.annotations.JSSetter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TcpConnectionHost extends ServiceObject {

    protected String host;
    protected int port;

    protected Socket socket;
    protected InputStream inputStream;
    protected OutputStream outputStream;
    protected BufferHost buffer;

    protected Map<Event, List<Listener>> listeners = new HashMap<>();

    protected final ExecutorService executor = Executors.newCachedThreadPool();

    public TcpConnectionHost() {}

    public TcpConnectionHost(String host, int port) {
        this.host = host;
        this.port = port;
        this.socket = new Socket();
    }

    @JSConstructor
    public static TcpConnectionHost constructor(Context cx, Object[] args, Function ctor, boolean inNewExpr) {
        ArrayAccessor<Object> argsAccessor = ArrayAccessor.of(args);
        Object arg0 = argsAccessor.get(0).orElse(null);
        Object arg1 = argsAccessor.get(1).orElse(null);

        Scriptable scope = ctor.getParentScope();

        if (arg0 instanceof String host) {
            if (arg1 instanceof Integer port) {
                return bindToScope(new TcpConnectionHost(host, port), scope);
            }
        }

        throw createInvalidArgumentsException(new TcpConnectionHost(), args, ctor);
    }

    @Override
    public String getClassName() {
        return "TcpConnection";
    }

    public List<Listener> getListeners(Event event) {
        return this.listeners.computeIfAbsent(event, k -> new ArrayList<>());
    }

    @JSGetter
    public Socket getUnderlyingSocket() {
        return this.socket;
    }

    @JSGetter
    public BufferHost getBuffer() {
        return this.buffer;
    }

    @JSSetter
    public void setBuffer(BufferHost buffer) {
        this.buffer = buffer;
    }

    @JSFunction
    public static void on(Context cx, Scriptable thisObj, Object[] args, Function function) {
        ArrayAccessor<Object> argsAccessor = ArrayAccessor.of(args);
        Object arg0 = argsAccessor.get(0).orElse(null);
        Object arg1 = argsAccessor.get(1).orElse(null);

        TcpConnectionHost hostObj = (TcpConnectionHost) thisObj;

        if (arg0 instanceof String eventName) {
            if (arg1 instanceof Function listener) {
                try {
                    Event event = Event.valueOf(eventName.toUpperCase());
                    Listener eventListener = hostObj.bindListener(listener);
                    hostObj.getListeners(event).add(eventListener);
                    return;
                } catch (Exception ignored) {
                    throw new IllegalArgumentException("Invalid event name: " + eventName);
                }
            }
        }

        throw createInvalidArgumentsException(thisObj, args, function);
    }

    @Override
    public void begin() {
        executor.submit(() -> {
            try {
                InetSocketAddress socketAddress = new InetSocketAddress(this.host, this.port);

                if (socketAddress.isUnresolved()) {
                    throw new RuntimeException("Failed to resolve given address...");
                }

                InetAddress netAddress = socketAddress.getAddress();
                int type = netAddress instanceof Inet4Address ? 4
                        : netAddress instanceof Inet6Address ? 6
                        : -1;

                this.getListeners(Event.LOOKUP).forEach(listener ->
                        listener.call(type, netAddress.getHostAddress(), this.host));

                socket.connect(socketAddress);
                inputStream = socket.getInputStream();
                outputStream = socket.getOutputStream();

                // TODO: Read inputStream
                // TODO: Dispatch Event.DATA
                // TODO: Dispatch Event.DRAIN

//                if (this.connectListener != null) {
//                    this.connectListener.call();
//                }


//                NativeUint8Array arr = new NativeUint8Array();

//                byte[] buffer = new byte[this.bufferSize];
//                int readLength;

//                inputStream.read

//                while ((readLength = inputStream.read(buffer)) != -1) {
//                    if (this.chunkListener != null) {
//                        this.chunkListener.call(buffer, readLength);
//                    }
//                }

            } catch (Exception e) {
                // TODO: Dispatch Event.TIMEOUT
                this.getListeners(Event.ERROR).forEach(listener ->
                        listener.call(e.getMessage()));
            }
        });
    }

    @Override
    public void terminate() {
        try {
            if (inputStream != null) inputStream.close();
            if (outputStream != null) outputStream.close();
            if (socket != null) socket.close();
        } catch (IOException ignored) {
        } finally {
//            triggerEvent("disconnect");
        }
    }

    public enum Event {
        CONNECT,
        DATA,
        DRAIN,
        END,
        ERROR,
        LOOKUP,
        READY,
        TIMEOUT
    }

}

class BinaryTCPServer {

    private static final Charset CUSTOM_CHARSET = StandardCharsets.ISO_8859_1; // Example charset

    public static void main(String[] args) {
        int port = 8080;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is listening on port " + port);

            while (true) {
                // Accept incoming client connections
                Socket socket = serverSocket.accept();
                System.out.println("New client connected: " + socket.getInetAddress().getHostAddress());

                // Handle client communication in a separate thread
                new Thread(new ClientHandler(socket)).start();
            }
        } catch (IOException e) {
            System.err.println("Server exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static class ClientHandler implements Runnable {
        private final Socket socket;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (InputStream inputStream = socket.getInputStream();
                 OutputStream outputStream = socket.getOutputStream()) {

                while (true) {
                    outputStream.write(new byte[]{0xF, 0xE, 0xA});
                    Thread.sleep(5000);
                }

//                byte[] buffer = new byte[1024];
//                int bytesRead;

                // Read binary data from the client
//                while ((bytesRead = inputStream.read(buffer)) != -1) {
//                    String receivedData = new String(buffer, 0, bytesRead, CUSTOM_CHARSET);
//                    System.out.println("Received: " + receivedData);
//
//                    // Echo the data back to the client
//                    String response = "Server received: " + receivedData;
//                    outputStream.write(response.getBytes(CUSTOM_CHARSET));
//
//                    // Break the loop if "bye" is received
//                    if (receivedData.trim().equalsIgnoreCase("bye")) {
//                        System.out.println("Client disconnected.");
//                        break;
//                    }
//                }

            } catch (IOException e) {
                System.err.println("Client handler exception: " + e.getMessage());
                e.printStackTrace();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    System.err.println("Error closing client socket: " + e.getMessage());
                }
            }
        }
    }
}
