package net.programmer.igoodie.streamspawn.javascript.spawnjs.__tests__;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class BinaryTCPServer {

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

    private record ClientHandler(Socket socket) implements Runnable {

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
