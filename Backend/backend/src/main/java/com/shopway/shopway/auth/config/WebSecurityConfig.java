package com.shopway.shopway.auth.config;



import com.shopway.shopway.auth.exceptions.RESTAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/*
 * Glavna konfiguracija Spring Security-ja za aplikaciju.
 * Definiše pravila autentifikacije, autorizacije i sigurnosne filtere.
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    private JWTTokenHelper jwtTokenHelper;

    @Autowired
    private UserDetailsService userDetailsService;

    // Endpoints koji ne zahtevaju autentifikaciju
    private final String[] publicAPIs = {
            "/api/auth/**",
            "/oauth2/**",
            "/login/oauth2/**"
    };
    /*
     * Konfiguriše glavni security filter chain sa pravilima za:
     * - CSRF zaštitu (isključena)
     * - Autorizaciju endpointa (koji su javni, koji zahtevaju autentifikaciju)
     * - CORS
     * - Session management (stateless zbog JWT-a)
     * - OAuth2 login
     * - Exception handling
     * - JWT authentication filter
     *
     * @return konfigurisani SecurityFilterChain
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorize) -> authorize
                .requestMatchers("/v3/api-docs/**","/api/file", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                .requestMatchers(HttpMethod.GET,"/api/products/**", "/api/products", "/api/category/**", "/api/category").permitAll()
                        .requestMatchers("/oauth2/success").permitAll()
                .anyRequest().authenticated())
                .cors(Customizer.withDefaults())
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .oauth2Login((ouath2login) -> ouath2login.defaultSuccessUrl("/oauth2/success"))
                .exceptionHandling((exception) -> exception.authenticationEntryPoint(new RESTAuthenticationEntryPoint()))
                .addFilterBefore(new JWTAuthenticationFilter(jwtTokenHelper,userDetailsService), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    /* Konifiguriše WebSecurity da ignoriše zahteve ka javnim API-jima
    */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web -> web.ignoring().requestMatchers(publicAPIs));
    }

    /*
        Kreira AuthenticationManager bean koji se koristi za autentifikaciju korisnika.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    /*
     Kreira PasswordEncoder bean za heširanje i proveru lozinki.
     Koristi delegating encoder koji automatski detektuje algoritam heširanja.
     @return PasswordEncoder instanca
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }


}
