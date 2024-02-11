package ddingdong.ddingdongBE.common.config;

import static org.springframework.http.HttpMethod.*;

import ddingdong.ddingdongBE.auth.service.JwtAuthService;
import ddingdong.ddingdongBE.common.filter.JwtAuthenticationFilter;
import ddingdong.ddingdongBE.common.handler.CustomAccessDeniedHandler;
import ddingdong.ddingdongBE.common.handler.RestAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
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
                .authorizeHttpRequests()
                .antMatchers(API_PREFIX + "/auth/**",
                        API_PREFIX + "/qr-stamps/**")
                .permitAll()
                .antMatchers(GET,
                        API_PREFIX + "/clubs/**",
                        API_PREFIX + "/notices/**",
                        API_PREFIX + "/banners/**")
                .permitAll()
                .antMatchers(API_PREFIX + "/admin/**").hasRole("ADMIN")
                .antMatchers(API_PREFIX + "/club/**").hasRole("CLUB")
                .anyRequest()
                .authenticated()
                .and()
                .cors()
                .configurationSource(corsConfigurationSource())
                .and()
                /*
                csrf, headers, http-basic, rememberMe, formLogin 비활성화
                */
                .formLogin()
                .disable()
                .logout()
                .disable()
                .csrf()
                .disable()
                .headers()
                .disable()
                .httpBasic()
                .disable()
                .rememberMe()
                .disable()
                /*
                Session 설정
                 */
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                /*
                Jwt 필터
                 */
                .addFilterBefore(authenticationFilter(authService, config), UsernamePasswordAuthenticationFilter.class)
                /*
                exceptionHandling
                 */
                .exceptionHandling()
                .authenticationEntryPoint(restAuthenticationEntryPoint())
                .accessDeniedHandler(accessDeniedHandler());

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
