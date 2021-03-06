package bg.softuni.musicdb.config;

import bg.softuni.musicdb.service.impl.MusicDBUserService;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

  private final MusicDBUserService musicDBUserService;
  private final PasswordEncoder passwordEncoder;

  public ApplicationSecurityConfig(MusicDBUserService musicDBUserService,
      PasswordEncoder passwordEncoder) {
    this.musicDBUserService = musicDBUserService;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {

    //TODO: Lucho PathRequest.toStaticResources().atCommonLocations() <- what is the problem
    http.
        authorizeRequests().
          // allow access to static resources to anyone
          antMatchers("/js/**", "/css/**", "/img/**").permitAll().
          // allow access to index, user login and registration to anyone
          antMatchers("/", "/users/login", "/users/register").permitAll().
          // protect all other pages
          antMatchers("/**").authenticated().
        and().
          // configure login with HTML form
          formLogin().
            // our login page will be served by the controller with mapping /users/login
            loginPage("/users/login").
            // the name of the user name input field in OUR login form is username (optional)
            usernameParameter("username").
            // the name of the user password input field in OUR login form is password (optional)
            passwordParameter("password").
            // on login success redirect here
            defaultSuccessUrl("/home").
            // on login failure redirect here
            failureForwardUrl("/users/login-error");//todo
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.
        userDetailsService(musicDBUserService).
        passwordEncoder(passwordEncoder);
  }
}
