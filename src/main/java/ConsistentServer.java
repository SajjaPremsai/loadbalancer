import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.TreeSet;


class ServerNode{
    private String host;
    private int port;
    ServerNode(String host, int port){
        this.host = host;
        this.port = port;
    }

    public String getHost(){
        return this.host;
    }

    public int getPort(){
        return this.port;
    }
}

public class ConsistentServer {
    private TreeSet<ServerNode> serverData;
    ConsistentServer(){
        serverData = new TreeSet<>();
    }

    public void start(int port) throws IOException{
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            serverSocket.setReuseAddress(true);
            System.out.println("🚀 Load balancer listening on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(() -> {
                    handleClient(clientSocket);
                }).start();
            }
        }
    }

    private void handleClient(Socket clientSocket){
            
    }

    private void addServer(String host,int port){
        ServerNode newNode = new ServerNode(host, port);
        serverData.add(newNode);
    }
}
