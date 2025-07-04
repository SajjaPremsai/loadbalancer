import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

     private final Router router;

    public Server(Router router) {
        this.router = router;
    }

    public void start(int port) throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            serverSocket.setReuseAddress(true);
            System.out.println("🚀 Server Listening on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(() -> {
                    handleClient(clientSocket);
                }).start();
            }
        }
    }


private void handleClient(Socket clientSocket){
    try(
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
    ){
        HttpRequest request = HttpRequest.parse(in);
            if (request == null) return;

            System.out.println("📥 " + request.getMethod() + " " + request.getPath());

            HttpResponse response = router.route(request);
            response.send(out);

    }catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException ignored) {}
        }
}
}
