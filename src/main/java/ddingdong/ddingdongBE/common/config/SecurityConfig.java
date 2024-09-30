package ddingdong.ddingdongBE.common.config;

import static org.springframework.http.HttpMethod.GET;

import ddingdong.ddingdongBE.auth.service.JwtAuthService;
import ddingdong.ddingdongBE.common.filter.JwtAuthenticationFilter;
import ddingdong.ddingdongBE.common.handler.CustomAccessDeniedHandler;
import ddingdong.ddingdongBE.common.handler.RestAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableConfigurationProperties
public class SecurityConfig {

    private static final String API_PREFIX = "/server";

    @Value("management.endpoints.web.base-path")
    private String actuatorPath;

    @Value("actuator")
    private String userName;

    @Value("actuator")
    private String password;

    @Value("actuator")
    private String roleName;

    @Bean
    @Order(0)
    public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthService authService, JwtConfig config)
        throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(API_PREFIX + "/auth/**").permitAll()
                .requestMatchers(API_PREFIX + "/admin/**").hasRole("ADMIN")
                .requestMatchers(API_PREFIX + "/club/**").hasRole("CLUB")
                .requestMatchers(GET,
                    API_PREFIX + "/clubs/**",
                    API_PREFIX + "/notices/**",
                    API_PREFIX + "/banners/**",
                    API_PREFIX + "/documents/**",
                    API_PREFIX + "/questions/**",
                    API_PREFIX + "/feeds/**").permitAll()
                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-resources/**")
                .permitAll()
                .anyRequest().authenticated()
            )
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(AbstractHttpConfigurer::disable)
            .headers(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .rememberMe(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .logout(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .addFilterBefore(authenticationFilter(authService, config),
                UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling(exceptions -> exceptions
                .authenticationEntryPoint(restAuthenticationEntryPoint())
                .accessDeniedHandler(accessDeniedHandler())
            );

        return http.build();
    }

    @Bean
    @Order(1)
    public SecurityFilterChain actuatorSecurity(HttpSecurity http, PasswordEncoder passwordEncoder)
        throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(actuatorPath + "/**").hasRole("ACTUATOR")
                .anyRequest().denyAll()
            )
            .httpBasic(AbstractHttpConfigurer::disable)
            .userDetailsService(userDetailsService(passwordEncoder))
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            );

        return http.build();
    }

    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        User user = (User) User.withUsername(userName)
            .password(passwordEncoder.encode(password))
            .roles(roleName)
            .build();
        return new InMemoryUserDetailsManager(user);
    }

    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOriginPattern("*");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationFilter authenticationFilter(JwtAuthService authService, JwtConfig config) {
        return new JwtAuthenticationFilter(authService, config);
    }

    @Bean
    public RestAuthenticationEntryPoint restAuthenticationEntryPoint() {
        return new RestAuthenticationEntryPoint();
    }

    @Bean
    public CustomAccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }
}
