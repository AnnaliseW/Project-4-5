
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

public class MarketPlaceServer {


    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(1234);

        Socket socket = serverSocket.accept();
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter writer = new PrintWriter(socket.getOutputStream());

        while (true) {

            String emailAndPassword = reader.readLine();
            String email = emailAndPassword.substring(0, emailAndPassword.indexOf(","));
            String password = emailAndPassword.substring(emailAndPassword.indexOf(",") + 1);
            String[] info = new String[4];
            String userAccountString = "";
            String logInInfo = "";


            try {
                BufferedReader bfr = new BufferedReader(new FileReader("data.txt"));
                String line = "";
                ArrayList<String> allUserData = new ArrayList<>();

                while ((line = bfr.readLine()) != null) {
                    allUserData.add(line);
                }

                for (int i = 0; i < allUserData.size(); i++) {
                    String[] oneUserData = allUserData.get(i).split(",");
                    // checking if user and password are the same
                    boolean sellerBoolean = false;
                    if (oneUserData[1].equals(email) && oneUserData[2].equals(password)) {
                        // format ji,jo1234,1234,false;

                        // parse boolean for creating the user for sign-in
                        sellerBoolean = false;
                        if (oneUserData[3].startsWith("true")) {
                            sellerBoolean = true;
                        } else if (oneUserData[3].startsWith("false")) {

                        }
                        //STRING FOR USER INFO
                        userAccountString = oneUserData[0] + "," + oneUserData[1] + "," + oneUserData[2] + "," + sellerBoolean;


                        if (sellerBoolean) {
                            info[0] = "seller,";
                            logInInfo += "seller,";
                        } else {
                            info[0] = "buyer,";
                            logInInfo += "buyer,";
                        }
                        info[1] = email + ",";
                        logInInfo += email + ",";

                        info[2] = password + ",";
                        logInInfo += password + ",";

                        info[3] = "true";
                        logInInfo += "true";


                        break;

                    } else {

                        info[0] = "notLogIn";
                        info[1] = "notLogIn";
                        info[2] = "notLogIn";
                        info[3] = "false";
                        logInInfo = "notLogIn,notLogIn,notLogIn,false";


                        //SIGN IN SHOULD NOT WORK AND SHOULD LOOP BACK
                    }


                }

                bfr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            writer.write(logInInfo);
            writer.println();
            writer.flush();

            //SEND SERVER USER INFO

            writer.write(userAccountString);
            writer.println();
            writer.flush();


            String sellerOrBuyer = reader.readLine();


            if (sellerOrBuyer.equals("seller")) {
                // PROCESSING FOR SELLER




            } else if (sellerOrBuyer.equals("buyer")) {
                //PROCESSING FOR BUYER

            }

            //PROCESSING SIGN IN

        }


    }
}
