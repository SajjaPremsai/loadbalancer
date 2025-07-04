import java.util.TreeSet;

public class ConsistentServer implements LoadBalancer {
    private final TreeSet<ServerNode> serverData;

    public ConsistentServer() {
        serverData = new TreeSet<>();
    }

    @Override
    public boolean removeServer(ServerNode server) {
        return serverData.remove(server);
    }

    @Override
    public boolean addServer(ServerNode server) {
        return serverData.add(server);
    }

    @Override
    public ServerNode getServer(long hash) {
        if (serverData.isEmpty()) return null;
        ServerNode dummy = new ServerNode("", 0, hash, "", "", "");
        TreeSet<ServerNode> tail = (TreeSet<ServerNode>) serverData.tailSet(dummy);
        return !tail.isEmpty() ? tail.first() : serverData.first();
    }
}
