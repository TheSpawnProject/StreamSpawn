package net.programmer.igoodie.streamspawn.javascript.spawnjs.network.hosts;

import net.programmer.igoodie.streamspawn.javascript.base.ServiceObject;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.annotations.JSConstructor;
import org.mozilla.javascript.annotations.JSGetter;
import org.mozilla.javascript.annotations.JSSetter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TcpClientHost extends ServiceObject {

    protected String host;
    protected int port;

    protected Socket socket;
    protected InputStream inputStream;
    protected OutputStream outputStream;
    protected BufferHost buffer;

    protected final ExecutorService executor = Executors.newCachedThreadPool();

    @JSConstructor
    public TcpClientHost(String host, int port) {
        this.host = host;
        this.port = port;
        this.socket = new Socket();
    }

    @Override
    public String getClassName() {
        return "TcpClient";
    }

    public TcpClientHost() {}

    @JSGetter
    public Socket getSocket() {
        return this.socket;
    }

//    @JSGetter
//    public int getBufferSize() {
//        return this.bufferSize;
//    }
//
//    @JSSetter
//    public void setBufferSize(int bufferSize) {
//        this.bufferSize = bufferSize;
//    }

    @Override
    public void begin() {
        executor.submit(() -> {
            try {
                socket.connect(new InetSocketAddress(this.host, this.port));
                inputStream = socket.getInputStream();
                outputStream = socket.getOutputStream();

                if (this.connectListener != null) {
                    this.connectListener.call();
                }


//                NativeUint8Array arr = new NativeUint8Array();

//                byte[] buffer = new byte[this.bufferSize];
//                int readLength;

//                inputStream.read

//                while ((readLength = inputStream.read(buffer)) != -1) {
//                    if (this.chunkListener != null) {
//                        this.chunkListener.call(buffer, readLength);
//                    }
//                }

            } catch (IOException e) {
                if (this.errorListener != null) {
                    this.errorListener.call(e.toString());
                }
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

    /* --------------------------- */

    protected ServiceObject.Listener connectListener;
    protected ServiceObject.Listener errorListener;
    protected ServiceObject.Listener chunkListener;

    @JSGetter
    public ServiceObject.Listener getOnConnect() {
        return this.connectListener;
    }

    @JSGetter
    public ServiceObject.Listener getOnError() {
        return this.errorListener;
    }

    @JSGetter
    public ServiceObject.Listener getOnChunk() {
        return this.chunkListener;
    }

    @JSSetter
    public void setOnConnect(Function listener) {
        this.connectListener = bindListener(listener);
    }

    @JSSetter
    public void setOnError(Function listener) {
        this.errorListener = bindListener(listener);
    }

    @JSSetter
    public void setOnChunk(Function listener) {
        this.chunkListener = bindListener(listener);
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
