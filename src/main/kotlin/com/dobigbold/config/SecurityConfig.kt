import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UserDetailsPasswordService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain
import reactor.core.publisher.Mono

@EnableWebFluxSecurity
@Configuration
class SecurityConfig(private val userDetailsService: ReactiveUserDetailsService) {

    @Bean
    fun securityWebFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        return http
            .csrf { csrfSpec ->
                csrfSpec.disable()  // Disabling CSRF protection
            }
            .authorizeExchange { authorizeExchangeSpec ->
                authorizeExchangeSpec
                    .pathMatchers("/auth/register", "/auth/login").permitAll()
                    .anyExchange().authenticated()
            }
            .httpBasic { httpBasicSpec ->
                httpBasicSpec.disable()  // Disabling HTTP Basic authentication
            }
            .authenticationManager(reactiveAuthenticationManager())
            .build()
    }

    @Bean
    fun reactiveAuthenticationManager(): ReactiveAuthenticationManager {
        return UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService).apply {
            setPasswordEncoder(passwordEncoder())
        }
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
}
