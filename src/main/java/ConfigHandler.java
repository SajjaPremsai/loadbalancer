import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ConfigHandler {
    private Properties configDetails;
    ConfigHandler(String path) throws FileNotFoundException, IOException{
        this.configDetails = new Properties();
        this.configDetails.load(new FileInputStream(path));
    }

    public int getServerCount(){
     return Integer.parseInt(configDetails.getProperty("server.count"));   
    }

    public String getServerHost(int index){
        String property = "server." + index + ".host";
        return configDetails.getProperty(property);
    }

    public int getServerPort(int index){
        String property = "server." + index + ".port";
        return Integer.parseInt(configDetails.getProperty(property));
    }
}
