package kz.Meirambekuly.Project.security;

import kz.Meirambekuly.Project.filter.CustomAuthorizationFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.csrf().disable()
                .addFilterAfter(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers("/api/user/login/**", "/api/user/register/**", "/").permitAll()
                .antMatchers("/api/user/updatePassword/**")
                .authenticated()
                .antMatchers("/api/users/**", "/api/role/addToUser/**", "/api/role/save/**", "/api/role/deleteFromUser/**").hasAuthority("ROLE_ADMIN")
                .anyRequest().authenticated();
    }
}
