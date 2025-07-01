import java.io.IOException;
public class LoadBalancer {
    public static void main(String[] args) throws IOException {
        ConfigHandler configure = new ConfigHandler("./server.properties");
        Server server = new Server();
        server.start(4000,configure);
               
    }
}
