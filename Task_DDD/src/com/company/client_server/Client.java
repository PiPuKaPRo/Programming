package com.company.client_server;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    static int port = 8080;
    public static void main(String[] args) {
        Scanner scn = new Scanner(System.in);
        while (true) {
            System.out.println("""
                    Enter the command:
                    1 - start
                    2 - next
                    3 - move
                    4 - action
                    0 - exit""");
            int com = scn.nextInt();
            String response = "";
            switch (com) {
                case 1 -> response = start();
                case 2 -> response = next();
                case 3 -> {
                    System.out.println("Select the number of card: ");
                    int cardNum = scn.nextInt();
                    response = move(cardNum);
                }
                case 4 -> response = action();
                case 0 -> response = null;
            }
            if (response == null) break;

            System.out.println("Response: " + response);
        }
    }

    private static String connectToServer(String request) {
        try (Socket socket = new Socket("127.0.0.1", port);
             BufferedWriter writer =
                     new BufferedWriter(
                             new OutputStreamWriter(
                                     socket.getOutputStream()));
             BufferedReader reader =
                     new BufferedReader(
                             new InputStreamReader(
                                     socket.getInputStream()));
        ) {
            System.out.println("Connected to server");
            writer.write(request);
            writer.newLine();
            writer.flush();

            return reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "Error";
    }

    public static String action() {
        return connectToServer("action");
    }

    public static String move(int cardNumber) {
        return connectToServer("move " + cardNumber);
    }

    public static String next() {
        return connectToServer("next");
    }

    public static String start() {
        return connectToServer("start");
    }
}
