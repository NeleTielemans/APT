package fact.it.apigateway.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.context.annotation.Bean;
import org.springframework.security.web.server.SecurityWebFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .authorizeExchange(exchange -> exchange
                        // Allow all GET requests to any path
                        .pathMatchers(HttpMethod.GET, "/**").permitAll()
                        // All POST requests must be authenticated
                        .pathMatchers(HttpMethod.POST, "/**").authenticated()
                        // Any other methods (PUT, DELETE, etc.) also require authentication
                        .anyExchange().authenticated()
                )
                // Enable JWT-based OAuth2 resource server
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(withDefaults()));

        return http.build();
    }
}
