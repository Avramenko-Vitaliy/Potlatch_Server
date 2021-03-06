package com.deadpeace.potlatch.security;

import com.deadpeace.potlatch.client.PotlatchSvcApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.savedrequest.NullRequestCache;

import javax.servlet.http.HttpServletResponse;

/**
 * Created with IntelliJ IDEA.
 * User: DeadPeace
 * Date: 17.12.2014
 * Time: 9:59
 * To change this template use File | Settings | File Templates.
 */
@Configuration
@EnableWebMvcSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter
{
    @Autowired
    @Qualifier("userService")
    private UserDetailsService service;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception
    {
        auth.userDetailsService(service);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        http.csrf().disable();
        http.requestCache().requestCache(new NullRequestCache());
        http.formLogin().loginPage("/login").permitAll();
        http.formLogin().loginProcessingUrl(PotlatchSvcApi.SVC_LOGIN).successHandler((request, response, authentication)->response.setStatus(HttpServletResponse.SC_OK))//.defaultSuccessUrl(PotlatchSvcApi.GIFT_SVC_PATH)
                .permitAll();
        http.logout().logoutUrl(PotlatchSvcApi.SVC_LOGOUT).logoutSuccessHandler((request, response, authentication)->response.setStatus(HttpServletResponse.SC_OK)).permitAll();
        http.authorizeRequests().anyRequest().authenticated();
    }
}
