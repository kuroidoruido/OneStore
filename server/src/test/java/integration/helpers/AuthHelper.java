package integration.helpers;

import static org.junit.jupiter.api.Assertions.assertLinesMatch;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MutableHttpRequest;
import io.micronaut.http.client.HttpClient;

public class AuthHelper {

    public static final AuthInfo ANONYMOUS = new AuthInfo().username("ANONYMOUS");

    private AuthHelper() {
    };

    public static AuthInfo connectUser(HttpClient client, String username, String hashedPassword) {
        var authRequest = Map.of("username", username, "password", hashedPassword);
        var authInfo = client.toBlocking().retrieve(HttpRequest.POST("/login", authRequest), AuthInfo.class);

        assertNotNull(authInfo);

        assertNotNull(authInfo.username);
        assertTrue(authInfo.username.length() > 0);

        assertNotNull(authInfo.access_token);
        assertTrue(authInfo.access_token.length() > 20);

        assertNotNull(authInfo.roles);
        assertLinesMatch(authInfo.roles, List.of("ROLE_USER"));

        authInfo.accessToken = authInfo.access_token;
        return authInfo;
    }

    private static MutableHttpRequest<?> addAuth(AuthInfo authInfo, MutableHttpRequest<?> req) {
        if (authInfo == ANONYMOUS) {
            return req;
        }
        return req.header("Authorization", "Bearer " + authInfo.accessToken);
    }

    public static MutableHttpRequest<?> GET(AuthInfo authInfo, String url) {
        return addAuth(authInfo, HttpRequest.GET(url));
    }

    public static <B, O> MutableHttpRequest<?> POST(AuthInfo authInfo, String url, B body) {
        return addAuth(authInfo, HttpRequest.POST(url, body)).header("Content-Type", "application/json");
    }

    public static <B, O> MutableHttpRequest<?> DELETE(AuthInfo authInfo, String url) {
        return addAuth(authInfo, HttpRequest.DELETE(url)).header("Content-Type", "application/json");
    }

    public static class AuthInfo {
        public String username;
        public ArrayList<String> roles;
        public String access_token;
        public String accessToken;

        public AuthInfo username(String username) {
            this.username = username;
            return this;
        }
    }
}