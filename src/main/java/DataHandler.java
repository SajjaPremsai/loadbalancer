import org.redisson.api.RMap;
import java.util.Map;

public class DataHandler implements RequestHandler {

    private final LoadBalancer loadBalancer;
    private final Redis redis;
    private final RMap<Long, ServerNode> serverCache;

    public DataHandler(LoadBalancer loadBalancer, Redis redis) {
        this.loadBalancer = loadBalancer;
        this.redis = redis;
        this.serverCache = redis.getClient().getMap("ServerCache");
    }

    @Override
    public HttpResponse handleRequest(HttpRequest request) {
        try {
            if (request.getPath().equals("/registerUser") && request.getMethod().equals("POST")) {
                return handleRegisterUser(request);
            }

            if (request.getPath().equals("/cachereport") && request.getMethod().equals("GET")) {
                return handleCacheReport();
            }

        } catch (Exception e) {
            return new HttpResponse(500, "text/plain", "Server Error: " + e.getMessage());
        }

        return null;
    }

    private HttpResponse handleRegisterUser(HttpRequest request) {
        String userIdHeader = request.getHeaders().get("userId");
        if (userIdHeader == null || userIdHeader.isEmpty()) {
            return new HttpResponse(400, "text/plain", "Missing userId header");
        }

        long userHash = Hashing.getHash(userIdHeader);
        ServerNode assignedServer = loadBalancer.getServer(userHash);

        if (assignedServer == null) {
            return new HttpResponse(500, "text/plain", "No server available for assignment");
        }

        serverCache.put(userHash, assignedServer);
        System.out.println("User Hash : " + userHash);

        return new HttpResponse(
            200,
            "text/plain",
            "User assigned to Server: " + assignedServer.getIp() + ":" + assignedServer.getPort()
        );
    }

    private HttpResponse handleCacheReport() {
        if (serverCache.isEmpty()) {
            return new HttpResponse(200, "text/plain", "No user-server mappings found");
        }


        StringBuilder report = new StringBuilder();
        for (Map.Entry<Long, ServerNode> entry : serverCache.entrySet()) {
            Long userHash = entry.getKey();
            ServerNode node = entry.getValue();

            report.append("User Hash: ").append(userHash)
                  .append(" → Server: ").append(node.getName())
                  .append(" (").append(node.getIp()).append(":").append(node.getPort()).append(")")
                  .append("\n");
        }

        return new HttpResponse(200, "text/plain", report.toString());
    }

}
