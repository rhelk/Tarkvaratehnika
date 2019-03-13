package rentdeck.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import rentdeck.Security.ApiAuthenticationFilter;
import rentdeck.Security.handlers.*;

import javax.servlet.Filter;
import javax.sql.DataSource;
import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConf extends WebSecurityConfigurerAdapter {

    @Autowired
    DataSource dataSource;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();


        http
                .cors().and()
                .authorizeRequests()
//                .antMatchers("/api/**").permitAll();
                .antMatchers("/api/logout").permitAll()
                .antMatchers("/api/login").permitAll()
                .antMatchers("/api/properties/**").permitAll()
                .antMatchers("/api/property/**").permitAll()
                .antMatchers("/api/**").hasAnyRole("USER", "ADMIN");


        http.exceptionHandling().authenticationEntryPoint(new ApiEntryPoint());
        http.exceptionHandling().accessDeniedHandler(new ApiAccessDeniedHandler());

        http.logout().logoutUrl("/api/logout").logoutSuccessHandler(new ApiLogoutSuccessHandler());

        http.addFilterAfter(apiLoginFilter("/api/login"), LogoutFilter.class);
    }

//    @Bean
//    CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOrigins(Arrays.asList("http://localhost:9000"));
//        configuration.setAllowedMethods(Arrays.asList("GET","POST"));
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }

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
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("*"));
        // setAllowCredentials(true) is important, otherwise:
        // The value of the 'Access-Control-Allow-Origin' header in the response must not be the wildcard '*' when the request's credentials mode is 'include'.
        configuration.setAllowCredentials(true);
        // setAllowedHeaders is important! Without it, OPTIONS preflight request
        // will fail with 403 Invalid CORS request
        configuration.setAllowedHeaders(Arrays.asList("*"));
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
