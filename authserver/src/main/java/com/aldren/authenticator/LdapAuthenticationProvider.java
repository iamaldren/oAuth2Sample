package com.aldren.authenticator;

import com.aldren.ldap.LdapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import javax.naming.NamingException;

@Component
public class LdapAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private LdapService ldapService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();

        try {
            return ldapService.authenticate(username, password);
        } catch (NamingException e) {
            throw new BadCredentialsException("Incorrect username/password. Please input the correct credentials.");
        }
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(aClass);
    }
}
