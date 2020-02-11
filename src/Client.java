import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class Client {

    private static final int PORT = 5555;

    public static void main(String[] args) throws IOException {
        String IP;

        try(
            BufferedReader keyboardInput = new BufferedReader(new InputStreamReader(System.in));) {
            System.out.print("Type in IP address of server you wish to reach\n>>>");

            IP=keyboardInput.readLine();
            Socket s = new Socket(IP, PORT);
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter out = new PrintWriter(new OutputStreamWriter(s.getOutputStream()), true);

            String query, sResponse;
            while(true) {
                System.out.print("Connection established!\n>>> ");
                query = keyboardInput.readLine();
                if(query.equalsIgnoreCase("exit")) {
                    System.out.println("Goodbye!");
                    break;
                }

                out.println(query);

                sResponse = in.readLine();

                if(sResponse.startsWith("1")){
                    System.out.println("!!!No email address found on the page!!!");
                } else if(sResponse.startsWith("2")){
                    System.out.println("!!!Server couldn’t find the web page!!!");
                } else if(sResponse.startsWith("0")) {
                    while ((sResponse = in.readLine()) != null && !sResponse.equals("done")) {
                        System.out.println("<<< " + sResponse);
                    }
                }
            }

        } catch(IOException ioe){
            System.err.println("Could not connect to server, are you sure you typed a correct IP or" +
                    " if the server is on?");
        }
    }
}
