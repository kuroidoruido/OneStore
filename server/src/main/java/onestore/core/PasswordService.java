package onestore.core;

import java.security.NoSuchAlgorithmException;

import javax.inject.Singleton;

import io.micronaut.context.annotation.Value;
import onestore.helpers.Hash;

@Singleton
public class PasswordService {
    @Value("${crypto.user.salt}")
    private String userSalt;

    public String toDbPassword(String password) {
        try {
            return Hash.sha3_256_toHex(password+this.userSalt);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return password;
        }
    }
}