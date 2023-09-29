package dev.hieplp.library.config;

import dev.hieplp.library.common.enums.user.Role;
import dev.hieplp.library.config.entry.TokenAuthenticationEntryPoint;
import dev.hieplp.library.config.filter.TokenAuthenticationFilter;
import dev.hieplp.library.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final TokenAuthenticationEntryPoint tokenAuthenticationEntryPoint;
    private final UserRepository userRepo;

    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() {
        return new TokenAuthenticationFilter();
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Cross-site Request Forgery

                // Authorization
                .authorizeHttpRequests(requests -> requests
                        // Need authentication
                        .requestMatchers(new AntPathRequestMatcher("/auth/refresh-access-token")).authenticated()
                        .requestMatchers(new AntPathRequestMatcher("/locations/addresses/**")).authenticated()

                        // Public
                        .requestMatchers(new AntPathRequestMatcher("/auth/**")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/catalogs/**")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/locations/**")).permitAll()

                        // Admin
                        .requestMatchers(new AntPathRequestMatcher("/admin/**")).hasAnyAuthority(Role.ADMIN.getRoleAsString(), Role.ROOT.getRoleAsString())
                        .requestMatchers(new AntPathRequestMatcher("/admin/users/**", HttpMethod.POST.name())).hasAuthority(Role.ROOT.getRoleAsString())

                        //
                        .anyRequest().authenticated()
                )

                // Exception handling
                .exceptionHandling(exceptionHandling -> exceptionHandling.authenticationEntryPoint(tokenAuthenticationEntryPoint))

                // Filter
                .addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}