package com.ezen.valuefinder.config;

import com.ezen.valuefinder.handler.CustomSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final DataSource dataSource;
    private final CustomSuccessHandler customSuccessHandler;

    @Bean
    MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
        return new MvcRequestMatcher.Builder(introspector);
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,MvcRequestMatcher.Builder mvc) throws Exception {


        http.authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(mvc.pattern("/css/**"),mvc.pattern("/js/**"), mvc.pattern("/images/**"),mvc.pattern("/assets/**"),mvc.pattern("/img/**")).permitAll()
                        .requestMatchers(mvc.pattern("/"),mvc.pattern("/member/login"),mvc.pattern("/member/regist"),mvc.pattern("/auction/**")).permitAll()
                        .requestMatchers(mvc.pattern("/favicon.ico"), mvc.pattern("/error"),mvc.pattern("/repair")).permitAll()
                        .requestMatchers(mvc.pattern("/admin/**")).hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(formLogin -> formLogin
                                .successHandler(customSuccessHandler)
                                .loginPage("/member/login")
                                //.defaultSuccessUrl("/")
                                .usernameParameter("email")
                                .failureUrl("/member/login/error")
                        //.permitAll()
                )
                .logout(logout -> logout
                                .logoutRequestMatcher(new AntPathRequestMatcher("/member/logout"))
                                .logoutSuccessUrl("/")
                        //.permitAll()
                )
                .exceptionHandling(handling -> handling
                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                )
                //.rememberMe(Customizer.withDefaults());
                .rememberMe(rememberMe -> rememberMe
                                .rememberMeParameter("remember-me")
                                .tokenValiditySeconds(86400*30)
                                .alwaysRemember(false)
                                .tokenRepository(tokenRepository())
                                .userDetailsService(userDetailsService)
                        //.userDetailsService(remembermeUserDetailService)
                )
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS));


        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public PersistentTokenRepository tokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        return jdbcTokenRepository;
    }
}
