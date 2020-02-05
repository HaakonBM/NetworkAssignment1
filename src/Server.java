import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Server {

    public static void main(String[] args) {

        try (ServerSocket ss = new ServerSocket(5555)) {
            ExecutorService pool = Executors.newFixedThreadPool(5);
            InetAddress inetAddress = InetAddress.getLocalHost();
            System.out.println("Email extraction server is up on IP "+inetAddress);
            while (true) {
                pool.execute(new ClientThread(ss.accept()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static class ClientThread implements Runnable {
        Socket cs;
        ClientThread(Socket cs) {
            this.cs = cs;
        }
        @Override
        public void run() {
            try(BufferedReader in = new BufferedReader(new InputStreamReader(cs.getInputStream()));
                PrintWriter out = new PrintWriter(new OutputStreamWriter(cs.getOutputStream()), true)) {
                System.out.println("Connection established to "+cs.getInetAddress());
                String request;
                String response;
                while ((request = in.readLine()) != null) {
                    EmailExtractor emailExtractor = new EmailExtractor(request);
                    emailExtractor.extract();
                    response = emailExtractor.response;
                    switch(response) {
                        case "CODE 0":
                            out.println("0\n"+emailExtractor.toString());
                            break;
                        case "CODE 1":
                            out.println("1");
                            break;
                        case "CODE 2":
                            out.println("2");
                    }
                    out.println("done");
                    emailExtractor.flushEmails();

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
