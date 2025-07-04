import java.io.Serializable;

public class ServerNode implements Serializable, Comparable<ServerNode> {
    private String ip;
    private int port;
    private String user;
    private String pass;
    private long hash;
    private String name;

    @Override
    public int compareTo(ServerNode other) {
        return Long.compare(this.hash, other.hash);
    }

    public ServerNode(String ip, int port, long hash, String user, String pass,String name) {
        this.ip = ip;
        this.port = port;
        this.hash = hash;
        this.user = user;
        this.pass = pass;
        this.name = name;
    }
public String toDataString() {
    return ip + ":" + port + "," + name + "," + user + "," + pass;
}
    public static ServerNode fromDataString(String data) {
        String[] parts = data.split(",");
        String[] hostPort = parts[0].split(":");
        String ip = hostPort[0];
        int port = Integer.parseInt(hostPort[1]);
        String name = parts[1];
        String user = parts[2];
        String pass = parts[3];
        long hash = Hashing.getHash(ip + ":" + port);
        return new ServerNode(ip, port, hash, user, pass, name);
    }

    public String getName(){
        return this.name;
    }
    public String getIp() {
        return this.ip;
    }

    public int getPort() {
        return this.port;
    }

    public String getUser(){
        return this.user;
    }

    public String getPass(){
        return this.pass;
    }

    public long getHash() {
        return this.hash;
    }

    @Override
    public String toString() {
        return "ServerNode{" +
                "ip='" + ip + '\'' +
                ", port=" + port +
                ", hash=" + hash +
                '}';
    }
}
