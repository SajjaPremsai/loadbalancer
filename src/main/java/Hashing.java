import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hashing {

    public static long getHash(String key) {
        return hash(key);
    }

    private static long hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(input.getBytes());
            long hash = ((bytes[0] & 0xFF) << 24) | ((bytes[1] & 0xFF) << 12) | ((bytes[2] & 0xFF) << 8) | ((bytes[4] & 0xFF) << 2);
            return hash & 0x7FFFFFF;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Hashing algorithm not found", e);
        }
    }
}
