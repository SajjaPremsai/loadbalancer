import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;

public class ServerHandler implements RequestHandler {

    private final LoadBalancer loadBalancer;
    private final Redis redis;
    private final RMap<Long, ServerNode> serverCache;

    public ServerHandler(LoadBalancer loadBalancer, Redis redis) {
        this.loadBalancer = loadBalancer;
        this.redis = redis;
        this.serverCache = redis.getClient().getMap("ServerCache");
    }

    @Override
    public HttpResponse handleRequest(HttpRequest request) {
        try {
            if (request.getPath().equals("/addServer") && request.getMethod().equals("POST")) {
                return handleAddServer(request);
            } else if (request.getPath().equals("/replaceServer") && request.getMethod().equals("POST")) {
                return handleReplaceServer(request);
            }
        } catch (Exception e) {
            return new HttpResponse(500, "text/plain", "Server Error: " + e.getMessage());
        }

        return null;
    }

    private HttpResponse handleAddServer(HttpRequest request) {
        ServerNode toAdd = parseServerNode(request.getBody());
        loadBalancer.addServer(toAdd);

        System.out.println("✅ Added server: " + toAdd + ", Hash: " + toAdd.getHash());
        return new HttpResponse(200, "text/plain", "Server added successfully");
    }

    private HttpResponse handleReplaceServer(HttpRequest request) {
    String[] parts = request.getBody().split(";");
    if (parts.length != 2) {
        return new HttpResponse(400, "text/plain", "Invalid request body format. Expected: newServer;oldServer");
    }

    try {
        System.out.println("📦 Parsing new server: " + parts[0]);
        System.out.println("📦 Parsing old server: " + parts[1]);

        ServerNode toAdd = parseServerNode(parts[0].trim());
        ServerNode toRemove = parseServerNode(parts[1].trim());

        loadBalancer.addServer(toAdd);
        loadBalancer.removeServer(toRemove);

        for (Long hash : serverCache.keySet()) {
            ServerNode assigned = serverCache.get(hash);
            if (assigned != null && assigned.getHash() == toRemove.getHash()) {
                ServerNode newAssigned = loadBalancer.getServer(hash);
                serverCache.put(hash, newAssigned);
            }
        }

        return new HttpResponse(200, "text/plain", "Server replaced successfully");
    } catch (Exception e) {
        e.printStackTrace(); 
        return new HttpResponse(500, "text/plain", "Error in replaceServer: " + e.getMessage());
    }
}



    private ServerNode parseServerNode(String body) {
        String[] parts = body.split(",");
        if (parts.length != 4) {
            throw new IllegalArgumentException("Invalid body format. Expected: host:port,name,user,pass");
        }

        String[] hostParts = parts[0].split(":");
        if (hostParts.length != 2) {
            throw new IllegalArgumentException("Invalid host:port format. Example: 127.0.0.1:3000");
        }

        String host = hostParts[0];
        int port = Integer.parseInt(hostParts[1]);

        String name = parts[1];
        String user = parts[2];
        String pass = parts[3];

        long hash = Hashing.getHash(host + ":" + port);

        return new ServerNode(host, port, hash, user, pass, name);
    }
}
