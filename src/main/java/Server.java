import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

public class Server {
    private AtomicInteger currentCount = new AtomicInteger(0);
    public void start(int port, ConfigHandler configurationDetails) throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            serverSocket.setReuseAddress(true);
            System.out.println("🚀 Load balancer listening on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(() -> {
                    try {
                        handleClient(clientSocket, configurationDetails);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        }
    }

    // private int getRequestSum(InputStream inputStream) {
    //     try {
    //         BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
    //         String line = reader.readLine();
    //         if (line == null) return 0;

    //         int sum = 0;
    //         for (char c : line.toCharArray()) {
    //             sum += c;
    //         }
    //         return sum;
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //         return 0;
    //     }
    // }

private void handleClient(Socket clientSocket, ConfigHandler configurationDetails) throws InterruptedException {
    try {
        int count = configurationDetails.getServerCount();
        int index = (currentCount.getAndIncrement() % count) + 1;
        String serverHost = configurationDetails.getServerHost(index);
        int serverPort = configurationDetails.getServerPort(index);
        System.out.println(currentCount.get() + " is handled by " + serverPort);
        Socket backendSocket = new Socket(serverHost, serverPort);
        Thread t1 = new Thread(()->{
            try {
                forwardStream(clientSocket.getInputStream(), backendSocket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        Thread t2 = new Thread(()->{
            try {
                forwardStream(backendSocket.getInputStream(), clientSocket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        backendSocket.close();
        clientSocket.close();
    } catch (IOException e) {
        e.printStackTrace();
    }
}

private void forwardStream(InputStream in, OutputStream out) throws IOException {
    byte[] buffer = new byte[8192];
    int bytesRead;
    while ((bytesRead = in.read(buffer)) != -1) {
        out.write(buffer, 0, bytesRead);
        out.flush();
    }
}


}
