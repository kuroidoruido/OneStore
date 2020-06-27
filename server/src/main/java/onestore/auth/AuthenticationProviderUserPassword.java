package onestore.auth;

import java.util.List;

import javax.inject.Singleton;

import org.reactivestreams.Publisher;

import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.AuthenticationFailed;
import io.micronaut.security.authentication.AuthenticationProvider;
import io.micronaut.security.authentication.AuthenticationRequest;
import io.micronaut.security.authentication.AuthenticationResponse;
import io.micronaut.security.authentication.UserDetails;
import io.reactivex.Flowable;
import onestore.core.PasswordService;
import onestore.core.UserInfosService;

@Singleton
public class AuthenticationProviderUserPassword implements AuthenticationProvider {

    private UserInfosService userInfosService;
    private PasswordService passwordService;

    public AuthenticationProviderUserPassword(UserInfosService userInfosService, PasswordService passwordService) {
        this.userInfosService = userInfosService;
        this.passwordService = passwordService;
    }

    @Override
    public Publisher<AuthenticationResponse> authenticate(HttpRequest<?> httpRequest,
            AuthenticationRequest<?, ?> authenticationRequest) {
        var identity = authenticationRequest.getIdentity();
        var password = authenticationRequest.getSecret();
        System.out.println(identity + " try to login");
        if (identity != null && password != null) {
            var optionalUserInfoForThisIdentity = this.userInfosService.getUserInfo(identity.toString());
            if (optionalUserInfoForThisIdentity.isPresent()) {
                var userInfoForThisIdentity = optionalUserInfoForThisIdentity.get();
                var hashPassword = this.passwordService.toDbPassword(password.toString());
                if (identity.equals(userInfoForThisIdentity.getUsername())
                        && hashPassword.equals(userInfoForThisIdentity.getPassword())) {
                    var isAdminUser = userInfoForThisIdentity.getUsername().equals("admin");
                    var roles = isAdminUser ? List.of(AuthRoles.ROLE_ADMIN) : List.of(AuthRoles.ROLE_USER);
                    var token = new UserDetails(userInfoForThisIdentity.getUsername(), roles);
                    return Flowable.just(token);
                }
            }
        }
        return Flowable.just(new AuthenticationFailed());
    }
}