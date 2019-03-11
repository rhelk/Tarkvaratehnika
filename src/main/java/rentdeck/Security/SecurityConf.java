package rentdeck.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import rentdeck.Security.handlers.*;

import javax.servlet.Filter;
import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConf extends WebSecurityConfigurerAdapter {

    @Autowired
    DataSource dataSource;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();

        http.authorizeRequests()
                .antMatchers("/api/login").permitAll()
                .antMatchers("/api/logout").permitAll()
                .antMatchers("/api/properties/**").permitAll()
                .antMatchers("/api/**").hasAnyRole("USER", "ADMIN");

        http.exceptionHandling().authenticationEntryPoint(new ApiEntryPoint());
        http.exceptionHandling().accessDeniedHandler(new ApiAccessDeniedHandler());

        http.logout().logoutUrl("/api/logout").logoutSuccessHandler(new ApiLogoutSuccessHandler());

        http.addFilterAfter(apiLoginFilter("/api/login"), LogoutFilter.class);
    }

    public Filter apiLoginFilter(String url) throws Exception {
        ApiAuthenticationFilter filter = new ApiAuthenticationFilter(url);

        filter.setAuthenticationManager(authenticationManager());

        filter.setAuthenticationSuccessHandler(new ApiAuthSuccessHandler());

        filter.setAuthenticationFailureHandler(new ApiAuthFailureHandler());

        return filter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder builder) throws Exception {
        builder.jdbcAuthentication()
                .passwordEncoder(new BCryptPasswordEncoder())
                .dataSource(dataSource);
//                .usersByUsernameQuery("SELECT (email, password, enabled) FROM users WHERE email = ?")
//                .authoritiesByUsernameQuery("SELECT (email, authority) FROM authorities WHERE email = ?");

    }
}

//join use_user on use_user.use_id = usr_user_role.usr_use_id where use_user.use_username=?