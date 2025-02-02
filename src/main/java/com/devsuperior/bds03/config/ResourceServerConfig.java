package com.devsuperior.bds03.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.util.Arrays;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Autowired
    private Environment env;

    @Autowired
    private JwtTokenStore jwtTokenStore;

    //Configurando um vetor com as rotas e quais roles poderão ser acesso.
    public static final String[] PUBLIC = {"/oauth/token", "/h2-console/**"}; //Login -> Liberado a todos.
    public static final String[] OPERATOR_OR_ADMIN = {"/departments/**", "/employees/**"};

    //Método que valida o token recebido.
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.tokenStore(jwtTokenStore);
    }

    //Definindo/Configurando as rotas, permissões e roles.
    @Override
    public void configure(HttpSecurity http) throws Exception {

        //Liberando o acesso ao H2 para o ambiente de test
        if (Arrays.asList(env.getActiveProfiles()).contains("test")) {
            http.headers().frameOptions().disable();
        }

        http.authorizeRequests()
                .antMatchers(PUBLIC).permitAll()
                //Liberando as requisições do tipo GET para products e categories. -> Mas se fizer o login as outras funcionam... ?
                .antMatchers(HttpMethod.GET, OPERATOR_OR_ADMIN).hasAnyRole("OPERATOR", "ADMIN")
                //Definindo que qualquer outra rota não específicada terá acesso total.
                .anyRequest().hasAnyRole("ADMIN");
    }
}