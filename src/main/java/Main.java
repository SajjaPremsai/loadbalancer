// import org.redisson.api.RMap;
// import org.redisson.api.RedissonClient;

import java.io.IOException;

import org.redisson.api.RList;

public class Main {
    public static void main(String[] args) throws IOException {
        // Redis redis = new Redis("127.0.0.1:6379");
        // RedissonClient redisClient = redis.getClient();
        // RMap<Long, ServerNode> map = redisClient.getMap("ServerCache");

        // long hash = Hashing.getHash("127.0.0.1:3000");
        // ServerNode node = new ServerNode("127.0.0.1", 3000, hash);

        // map.put(hash, node);
        // System.out.println("Put: " + node);

        // ServerNode fetched = map.get(hash);
        // System.out.println("Fetched: " + fetched);

        // redis.shutdown();

        LoadBalancer lb = new ConsistentServer();
        Redis  redis = new Redis("127.0.0.1:6379");
        RList<String> serverList = redis.getClient().getList("ServerList");
        for (String data : serverList) {
            ServerNode node = ServerNode.fromDataString(data);
            lb.addServer(node);
            System.out.println("🔁 Rehydrated: " + node);
        }
        Router router = new Router();
        DataHandler dataHandler = new DataHandler(lb, redis);
        ServerHandler serverhandler = new ServerHandler(lb, redis);
        router.addRoute(serverhandler);
        router.addRoute(dataHandler);
        Server server = new Server(router);
        server.start(4000);
    }
}
