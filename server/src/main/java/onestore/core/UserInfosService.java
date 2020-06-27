package onestore.core;

import java.util.Optional;

import javax.inject.Singleton;

import onestore.core.model.UserInfo;
import onestore.db.UserDB;

@Singleton
public class UserInfosService {

    public static final String USER_COLLECTION = "users";

    private UserDB userDB;
    private PasswordService passwordService;

    public UserInfosService(UserDB userDB, PasswordService passwordService) {
        this.userDB = userDB;
        this.passwordService = passwordService;
    }

    public Optional<UserInfo> getUserInfo(String user) {
        return this.userDB.getUsers().stream()
            .filter(userInfo -> userInfo.getUsername().equals(user))
            .findAny();
    }

    public Optional<UserInfo> getUserInfoForOwner(String user) {
        return getUserInfo(user).map(UserInfosService::dropPassword);
    }

    public Optional<UserInfo> getUserInfoOnOtherUser(String user) {
        return getUserInfo(user)
            .map(userInfo -> new UserInfo().username(userInfo.getUsername()));
    }

    public void createUser(UserInfo userInfo) {
        this.userDB.createUser(userInfo.password(this.passwordService.toDbPassword(userInfo.getPassword())));
    }

    private static UserInfo dropPassword(UserInfo userInfo) {
        return userInfo.password(null);
    }
}