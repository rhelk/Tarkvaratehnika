package rentdeck.Security;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import rentdeck.util.JsonWebToken;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static rentdeck.Security.SecurityConstants.*;

public class JWTAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    public JWTAuthenticationFilter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response)
            throws AuthenticationException {

        LoginCredentials loginCredentials;

        System.out.println("login request...");

        try {
//            BufferedReader buffer = new BufferedReader(new InputStreamReader(request.getInputStream()));
//            System.out.println(buffer.lines().collect(Collectors.joining("\n")) + "  tryCatch");

            loginCredentials = new ObjectMapper().readValue(request.getInputStream(), LoginCredentials.class);
        } catch (Exception e) {
            throw new BadCredentialsException("");
        }

        // Read info from HttpServletRequest.

        System.out.println(loginCredentials + " -- credentials");

        // Use ObjectMapper to convert Json to LoginCredentials object.

        // Info from LoginCredentials is used below.

        UsernamePasswordAuthenticationToken authRequest =
                new UsernamePasswordAuthenticationToken(
                    loginCredentials.getUsername(),
                        loginCredentials.getPassword());

        return this.getAuthenticationManager().authenticate(authRequest);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {

       String token = JsonWebToken.genJWT(((User) auth.getPrincipal()).getUsername());
        res.addHeader(HEADER_STRING, TOKEN_PREFIX + token);

    }
}
