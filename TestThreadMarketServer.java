import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

//CLIENT SHOULD NOT HAVE RUN AND THREAD. IT DOES NOT NEED ANY OF THAT

public class TestThreadMarketServer {

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
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);


                String clientRequest = reader.readLine();
                if (clientRequest != null) {
                    if (clientRequest.equals("createAccount")) {
                        // Call create account method
                        createAccount();
                    } else {



                    }
                }

                // Continue reading and responding to client requests within this thread

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private static void createAccount() {
            // Implement create account logic
        }
    }
}
