package entreprisecorp;

import entreprisecorp.restservices.models.user.UserPrincipalDetailsService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter
{
    @Autowired
    private UserPrincipalDetailsService userPrincipalDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
       auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type"));
        corsConfiguration.setAllowedOrigins(List.of("*"));
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PUT", "OPTIONS", "PATCH", "DELETE"));
        corsConfiguration.setAllowCredentials(false);
        corsConfiguration.setExposedHeaders(List.of("Authorization"));

        http.authorizeRequests()
            .antMatchers("/login").authenticated()
            .antMatchers("/register").permitAll()
            .antMatchers("/features/create").permitAll()
            .antMatchers("/features/getMatch").permitAll()
            .antMatchers("/features/returnResultMatch").permitAll()
            .antMatchers("/**").authenticated()

            .and()
            .httpBasic()
            .and()
            .csrf().disable()
            .formLogin().disable()
            .cors().configurationSource(request -> corsConfiguration);
    }

    @Bean
    DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userPrincipalDetailsService);
        return daoAuthenticationProvider;
    }
}