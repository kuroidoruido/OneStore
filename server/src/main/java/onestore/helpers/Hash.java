package onestore.helpers;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hash {
    private Hash() {
    }

    public static String sha3_256_toHex(String s) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA3-256");
        final byte[] hashbytes = digest.digest(s.getBytes(StandardCharsets.UTF_8));
        return Hex.toHexString(hashbytes);
    }
}