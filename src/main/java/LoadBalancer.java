public interface LoadBalancer {

    boolean addServer(ServerNode Server);

    boolean removeServer(ServerNode Server);

    ServerNode getServer(long hash);
}