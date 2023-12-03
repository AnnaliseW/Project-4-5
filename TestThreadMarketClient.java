import javax.swing.*;
import java.io.IOException;
import java.net.Socket;

public class TestThreadMarketClient {
    private JTextField emailField;
    private JPasswordField passwordField;

    public GUICustomerView guiCustomerView;
    public GUISellerView guiSellerView;

    public User userAccount;

    public boolean openSeller = false;
    public boolean logIn = false;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new MarketPlaceClient().setVisible(true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public TestThreadMarketClient() throws IOException {
        JOptionPane.showMessageDialog(null, "Welcome to the marketplace", "Welcome", JOptionPane.INFORMATION_MESSAGE);
        SocketThread socketThread = new SocketThread();
        socketThread.start();
    }

    private class SocketThread extends Thread {
        @Override
        public void run() {
            try {
                Socket socket = new Socket("localhost", 1234);
                //TODO: REST OF CODE
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
