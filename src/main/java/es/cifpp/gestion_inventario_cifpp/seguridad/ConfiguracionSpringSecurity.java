package es.cifpp.gestion_inventario_cifpp.seguridad;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import es.cifpp.gestion_inventario_cifpp.servicios.ServicioUsuarioDetalles;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class ConfiguracionSpringSecurity {

    private final ServicioUsuarioDetalles servicioUsuarioDetalles;

    public ConfiguracionSpringSecurity(ServicioUsuarioDetalles servicioUsuarioDetalles) {
        this.servicioUsuarioDetalles = servicioUsuarioDetalles;
    }

    @Bean //Esto encripta las contraseñas
    public BCryptPasswordEncoder CodificarContrasenya(){
         return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain configurarFiltros(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
            .requestMatchers("/IniciarSesion", "/procesarLogin", "/css/**").permitAll()
            .anyRequest().authenticated()
            )
            .userDetailsService(servicioUsuarioDetalles)
            .formLogin(form -> form
                .loginPage("/IniciarSesion")                   // IniciarSesion.html
                .loginProcessingUrl("/procesarLogin")          // Spring Security procesa el inico de sesion
                .defaultSuccessUrl("/volverAInicio", true)      // vuelve a index tras iniciar sesión
                .failureUrl("/IniciarSesion?error=true")        // si falla, vuelve a index con error
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/IniciarSesion")                        // vuelve al login tras cerrar sesión
            );

        return http.build();
    }
}
