import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class GUITest extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;

    public GUITest() {
        setTitle("Sign In");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel emailLabel = new JLabel("Email:");
        JLabel passwordLabel = new JLabel("Password:");

        emailField = new JTextField(15);
        passwordField = new JPasswordField(15);

        JButton signInButton = new JButton("Sign In");

        setLayout(new GridLayout(3, 2));

        add(emailLabel);
        add(emailField);
        add(passwordLabel);
        add(passwordField);
        add(new JLabel());
        add(signInButton);

        signInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                signIn();
            }
        });

        //TO:DO continue (marketplace home page)


    }

    private void signIn() {
        String email = emailField.getText();
        char[] passwordChars = passwordField.getPassword();
        String password = new String(passwordChars);

        System.out.println("Email: " + email);
        System.out.println("Password: " + password);
        
        passwordField.setText("");

        User userAccount = null;

        boolean signInComplete = false;
        try {
            BufferedReader bfr = new BufferedReader(new FileReader("data.txt"));
            String line = "";
            ArrayList<String> allUserData = new ArrayList<>();


            while ((line = bfr.readLine()) != null) {
                allUserData.add(line);
            }

            for (int i = 0; i < allUserData.size(); i++) {
                String[] oneUserData = allUserData.get(i).split(",");
                //checking if user and password is same
                if (oneUserData[1].equals(email) && oneUserData[2].equals(password)) {
                    //format ji,jo1234,1234,false;

                    //parse boolean for creating the user for sign in
                    boolean sellerBoolean = false;
                    if (oneUserData[3].startsWith("true")) {
                        sellerBoolean = true;
                    } else if (oneUserData[3].startsWith("false")) {
                        sellerBoolean = false;
                    }
                    userAccount = new User(oneUserData[0], oneUserData[1], oneUserData[2], sellerBoolean);
                    signInComplete = true;
                    break;
                } else {
                    signInComplete = false;
                }
            }

            bfr.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        if (signInComplete) {
            JOptionPane.showMessageDialog(null, "Signed in!", "Signed in", JOptionPane.INFORMATION_MESSAGE);
            System.out.println("signed in");
            this.setVisible(false);
        }
    }

    public static void run() {
        JOptionPane.showMessageDialog(null, "Welcome to the marketplace", "Welcome", JOptionPane.INFORMATION_MESSAGE);
        new GUITest().setVisible(true);
    }

    public static void main(String[] args) {
        run();
    }

}
