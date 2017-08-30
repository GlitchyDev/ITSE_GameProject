package Networking;

import sample.Main;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by Robert on 8/26/2017.
 * This class is made to:
 * - Receive and Execute incoming Packets from the Server
 * - Send out "Controller Input" to the Server for all players connected to the Client
 * - Run on a seperate thread than Rendering so shit doesn't get held up
 */
public class ClientConnection implements Runnable {

    @Override
    public void run() {
        try {
            Socket s = new Socket("localhost", 9090);
            BufferedReader input =
                    new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter out = new PrintWriter(s.getOutputStream(), true);

            while(true) {
                System.out.println("Client: " + input.readLine());
                out.println(Math.random());
            }




        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
