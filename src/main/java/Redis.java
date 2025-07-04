import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

public class Redis {
    private Config config;
    private RedissonClient redissonClient;

    public Redis(String redisServer) {
        config = new Config();
        if (!redisServer.startsWith("redis://")) {
            redisServer = "redis://" + redisServer;
        }
        config.useSingleServer().setAddress(redisServer);
        redissonClient = Redisson.create(config);
    }

    public RedissonClient getClient() {
        return redissonClient;
    }

    public void shutdown() {
        if (redissonClient != null && !redissonClient.isShutdown()) {
            redissonClient.shutdown();
        }
    }
}
