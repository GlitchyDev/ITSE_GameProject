package Networking;

import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

/**
 * Created by Robert on 8/26/2017.
 * This class is made to:
 * - Receive and Execute incoming Packets from the Server
 * - Send out "Controller Input" to the Server for all players connected to the Client
 * - Run on a separate thread than Rendering so stuff doesn't get held up
 */
public class ServerConnection implements Runnable {

    @Override
    public void run() {
        ServerSocket listener = null;
        try {
            listener = new ServerSocket(9090);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            while (true) {
                Socket socket = listener.accept();
                BufferedReader input =
                        new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                while(true) {
                    out.println(Math.random());
                    System.out.println("Server: " + input.readLine());
                }





            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
