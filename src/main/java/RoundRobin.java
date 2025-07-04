import java.util.ArrayList;

public class RoundRobin implements LoadBalancer {

    private ArrayList<ServerNode> serverData;

    RoundRobin() {
        serverData = new ArrayList<>();
    }

    @Override
    public boolean addServer(ServerNode Server) {
        serverData.add(Server);
        return true;
    }

    @Override
    public boolean removeServer(ServerNode Server) {
        if (!serverData.isEmpty()) {
            serverData.remove(Server);
            return true;
        }
        return false;
    }

    @Override
    public ServerNode getServer(long hash) {
        if (serverData.isEmpty())
            return null;
        int index = (int) (Math.abs(hash) % serverData.size());
        return serverData.get(index);
    }

}
