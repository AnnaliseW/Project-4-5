import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/*
If IntelliJ or platform says that "cannot run in parallel":
1. Right click on client
2. Select (two windows button) Edit "..."
3. Select "allow Parallel Run"
 */

public class ThreadedMarketPlaceServer {

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(1234);

        while (true) {
            Socket socket = serverSocket.accept();
            Thread clientHandlerThread = new Thread(new ClientHandler(socket));
            clientHandlerThread.start();
        }
    }

    private static class ClientHandler implements Runnable {
        private final Socket socket;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter writer = new PrintWriter(socket.getOutputStream());

                while (true) {



                    
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
