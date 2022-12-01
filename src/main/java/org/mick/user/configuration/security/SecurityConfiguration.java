package org.mick.user.configuration.security;

import org.mick.user.configuration.JsonWebTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true
)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private JsonWebTokenUtils tokenUtils;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        String[] allowedEndPoints = Arrays.stream(AllowedEndPoints.values())
                .map(AllowedEndPoints::getUrls).toArray(String[]::new);
        http.cors().and().csrf().disable();
        http.httpBasic().and().authorizeRequests().antMatchers(allowedEndPoints).permitAll()
                .anyRequest().authenticated();
        http.addFilterAfter(new TokenAuthorizationFilter(tokenUtils)
                        , UsernamePasswordAuthenticationFilter.class);
        http.headers().frameOptions().disable();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        String[] allowedEndPoints = Arrays.stream(AllowedEndPoints.values())
                .map(AllowedEndPoints::getUrls).toArray(String[]::new);
        web.ignoring().antMatchers(allowedEndPoints);
    }
}
