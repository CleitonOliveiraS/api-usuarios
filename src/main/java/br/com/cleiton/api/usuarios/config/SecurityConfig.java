package br.com.cleiton.api.usuarios.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. Desabilita o CSRF (comum em APIs REST baseadas em Tokens/Stateless)
                .csrf(AbstractHttpConfigurer::disable)

                // 2. Configura as regras de permissão dos endpoints
                .authorizeHttpRequests(authorize -> authorize
                        // IMPORTANTE: Libera TODOS os métodos (POST, GET, PUT, DELETE) na rota /usuarios e sub-rotas
                        .requestMatchers("/usuarios/**").permitAll()
                        .requestMatchers("/error").permitAll() // 👈 ADICIONE ISSO: Libera as respostas de erro da validação

                        // Exemplo: Libera qualquer requisição que comece com /publico/
                        // .requestMatchers("/publico/**").permitAll()

                        // Qualquer outra requisição do sistema exigirá autenticação
                        .anyRequest().authenticated()
                );

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
