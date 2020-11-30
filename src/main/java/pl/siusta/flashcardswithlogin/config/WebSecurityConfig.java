package pl.siusta.flashcardswithlogin.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.siusta.flashcardswithlogin.repo.UserRepo;

@Configuration
public class WebSecurityConfig  extends WebSecurityConfigurerAdapter {
    private UserDetailsService userDetailsService;
    private UserRepo userRepo;

    public WebSecurityConfig(@Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService, UserRepo userRepo) {
        this.userDetailsService = userDetailsService;
        this.userRepo = userRepo;
    }
    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/register").permitAll()
                .antMatchers("/user").authenticated()
                .antMatchers("/").permitAll()
                .antMatchers("/add").permitAll()
                .antMatchers("/about").permitAll()
                .and()
                .formLogin().permitAll().defaultSuccessUrl("/user").loginPage("/login")
                .and()
                .logout().logoutSuccessUrl("/");
    }

}
