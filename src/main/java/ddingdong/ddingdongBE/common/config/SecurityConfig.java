package ddingdong.ddingdongBE.common.config;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

import ddingdong.ddingdongBE.auth.service.JwtAuthService;
import ddingdong.ddingdongBE.common.filter.JwtAuthenticationFilter;
import ddingdong.ddingdongBE.common.handler.CustomAccessDeniedHandler;
import ddingdong.ddingdongBE.common.handler.RestAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final String API_PREFIX = "/server";

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthService authService, JwtConfig config)
            throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(API_PREFIX + "/auth/**",
                                API_PREFIX + "/events/**")
                        .permitAll()
                        .requestMatchers(API_PREFIX + "/admin/**").hasRole("ADMIN")
                        .requestMatchers(API_PREFIX + "/central/**").hasRole("CLUB")
                        .requestMatchers("/server/actuator/**").permitAll()
                        .requestMatchers(GET,
                                API_PREFIX + "/clubs/**",
                                API_PREFIX + "/notices/**",
                                API_PREFIX + "/banners/**",
                                API_PREFIX + "/documents/**",
                                API_PREFIX + "/questions/**",
                                API_PREFIX + "/feeds/**",
                                API_PREFIX + "/forms/**",
                                API_PREFIX + "/file/upload-url/form-application"
                                )
                        .permitAll()
                        .requestMatchers(POST,
                                API_PREFIX + "/forms/{formId}/applications"
                                )
                        .permitAll()
                        .requestMatchers(API_PREFIX + "/internal/**")
                        .permitAll()
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-resources/**")
                        .permitAll()
                        .anyRequest()
                        .authenticated()
                )
                .cors(cors -> cors
                        .configurationSource(corsConfigurationSource())
                )
                /*
                csrf, headers, http-basic, rememberMe, formLogin 비활성화
                */
                .csrf(AbstractHttpConfigurer::disable)
                .headers(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .rememberMe(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                /*
                Session 설정
                 */
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                /*
                Jwt 필터
                 */
                .addFilterBefore(authenticationFilter(authService, config),
                        UsernamePasswordAuthenticationFilter.class)
                /*
                exceptionHandling
                 */
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint(restAuthenticationEntryPoint())
                        .accessDeniedHandler(accessDeniedHandler())
                );

        return http.build();
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
