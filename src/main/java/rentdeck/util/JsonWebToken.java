package rentdeck.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;

import java.security.Principal;
import java.util.Date;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static rentdeck.Security.SecurityConstants.EXPIRATION_TIME;
import static rentdeck.Security.SecurityConstants.SECRET;

public class JsonWebToken {
    public static  String genJWT (String username) {
        return com.auth0.jwt.JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(HMAC512(SECRET.getBytes()));
    }
}
