package io.github.otavioborgsm.domain.config;

import io.github.otavioborgsm.security.jwt.JwtAuthFilter;
import io.github.otavioborgsm.security.jwt.JwtService;
import io.github.otavioborgsm.service.impl.UsuarioServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UsuarioServiceImpl usuarioService;
    @Autowired
    private JwtService jwtService;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public OncePerRequestFilter jwtFilter(){
        return new JwtAuthFilter(jwtService, usuarioService);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .userDetailsService(usuarioService)
            .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                    .antMatchers(HttpMethod.GET,"/api/publicacoes/**")
                        .permitAll()
                    .antMatchers("/api/publicacoes/pesquisa/**")
                        .permitAll()
                    .antMatchers("/api/publicacoes/**")
                        .hasAnyRole("USER", "ADMIN", "FARMACEUTICO")
                    .antMatchers("/api/comentarios/**")
                        .hasAnyRole("USER", "ADMIN", "FARMACEUTICO")
                    .antMatchers(HttpMethod.POST,"/api/farmaceuticos/**")
                        .hasRole("ADMIN")
                    .antMatchers("/api/farmaceuticos/**")
                        .hasAnyRole("ADMIN", "FARMACEUTICO")
                    .antMatchers(HttpMethod.POST,"/api/usuarios/**")
                        .permitAll()
                    .antMatchers(HttpMethod.GET,"/api/usuarios/**")
                        .permitAll()
                    .antMatchers(HttpMethod.PUT,"/api/usuarios/**")
                        .hasAnyRole("USER", "ADMIN", "FARMACEUTICO")
                    .antMatchers(HttpMethod.DELETE,"/api/usuarios/**")
                        .hasAnyRole("USER", "ADMIN", "FARMACEUTICO")
                    .antMatchers("/api/email/**")
                        .permitAll()
                    .antMatchers(HttpMethod.GET,"/api/farmacos/**")
                        .permitAll()
                    .antMatchers(HttpMethod.PUT,"/api/farmacos/**")
                        .hasRole("ADMIN")
                    .antMatchers(HttpMethod.DELETE,"/api/farmacos/**")
                        .hasRole("ADMIN")
                    .anyRequest().authenticated()
                .and()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                    .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class );
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(
                "/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**");
    }
}
