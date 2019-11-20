//package com.arrnaux.userservice.springSecurity.configuration;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//
//@Configuration
//@EnableWebSecurity
//public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
//    @Override
//    public void configure(final AuthenticationManagerBuilder auth)
//            throws Exception {
//        // how Spring authenticates the user
//        auth.inMemoryAuthentication().withUser("user1@example.com")
//                .password("user1").roles("USER");
//    }
//
//    @Override
//    protected void configure(final HttpSecurity http) throws Exception {
//        // HttpSecurity object creates a Servlet Filter, which ensures that the currenly logged-in
//        // user is associated with the appropiate role.
//        http.authorizeRequests()
////                .antMatchers("/**")
//                .antMatchers("/").hasAnyRole("ANONYMOUS", "USER")
//                .antMatchers("/register").hasAnyRole("ANONYMOUS", "USER")
////                .antMatchers("/login/*").hasAnyRole("ANONYMOUS", "USER")
////                .antMatchers("/logout/*").hasRole("USER")
////                .antMatchers("/**").hasRole("USER")
//                .antMatchers("/**").hasRole("ANONYMOUS")
//                // equivalent to <http auto-config="true">
////                .and()
////                .formLogin()
////                .loginPage("/login/form")
////                 @loginPage specifies where Spring Security will redirect the browser if a protected page is accessed and the user is not authenticated
////                .loginProcessingUrl("/login")
//                // @loginProcessingURL specifies that the login form should be submitted to, using an HTTP post
////                .failureUrl("/login/form?error")
//                // @failureURL specifies the page that SPSec will redirect to if the user & pass are invalid
////                .usernameParameter("username")
////                .passwordParameter("password")
//                // @user & @pass -> HTTP parameters that SPSec will use to authenticate the user while processing loginProcessingUrl method
//                .and()
//                .httpBasic()
//                .and()
//                .logout()
////                .logoutUrl("/logout")
////                .logoutSuccessUrl("/login?logout")
//                // CSRF is enabled by default (will discuss later)
//                .and()
//                .csrf().disable();
//    }
//}
