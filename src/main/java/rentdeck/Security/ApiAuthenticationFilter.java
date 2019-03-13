package rentdeck.Security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

//@CrossOrigin(origins = "http://localhost:9000")
public class ApiAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    public ApiAuthenticationFilter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {

        System.out.println("basically working...");
        LoginCredentials loginCredentials;
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
}
