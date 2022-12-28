package com.company.client_server;

import com.company.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread {
    private ServerSocket serverSocket;

    public static GUILogic guiLogic = new GUILogic();


    public Server(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        serverSocket.setSoTimeout(10000);
    }

    public static void main(String[] args) {
        guiLogic.initializeGame();

        try (ServerSocket server = new ServerSocket(8080)) {
            System.out.println("Server started!");
            while (true) {
                try (
                        Socket socket = server.accept();
                        BufferedWriter writer =
                                new BufferedWriter(
                                        new OutputStreamWriter(
                                                socket.getOutputStream()));
                        BufferedReader reader =
                                new BufferedReader(
                                        new InputStreamReader(
                                                socket.getInputStream()));
                ) {
                    String request = reader.readLine();
                    System.out.println("Request: " + request);
                    String response = getResponse(request);
                    System.out.println("Response: " + response);

                    writer.write(response);
                    writer.newLine();
                    writer.flush();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void runServerApp(int port) {
        try {
            Thread thread = new Server(port);
            thread.start();

            guiLogic.initializeGame();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getResponse(String request) {
        if (request.contains("action")) {
            guiLogic.action();
        }
        if (request.contains("next")) {
            guiLogic.next();
        }
        if (request.contains("move")) {
            String[] s = request.split(" ");
            int cardNum = Integer.parseInt(s[1]);
            guiLogic.move(cardNum);
        }
        if (request.contains("start")) {
            guiLogic.start();
        }

        return guiLogic.convertGUIInfoTOString();
    }
}
