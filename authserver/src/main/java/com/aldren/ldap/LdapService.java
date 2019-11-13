package com.aldren.ldap;

import com.aldren.model.Users;
import com.aldren.properties.LdapProperties;
import com.aldren.repository.UsersRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import java.util.*;

@Slf4j
@Component
@EnableConfigurationProperties(LdapProperties.class)
public class LdapService {

    private static final String MAIL = "mail";
    private static final String CN = "cn";
    private static final String OBJECT_CLASS = "objectClass";
    @Autowired
    private LdapProperties ldap;
    @Autowired
    private UsersRepository usersRepository;

    public UsernamePasswordAuthenticationToken authenticate(String username, String password) throws NamingException {
        DirContext ctx = null;

        Map<String, String> details = new HashMap<>();
        List<GrantedAuthority> authorities = new ArrayList<>();

        try {
            ctx = getLdapConnection(username, password);
            Users users = usersRepository.findByUserId(username);

            details.put("role", users.getUserRole());
            authorities.add(new SimpleGrantedAuthority(users.getUserRole()));
        } finally {
            if (ctx != null) {
                ctx.close();
            }
        }

        UsernamePasswordAuthenticationToken upat = new UsernamePasswordAuthenticationToken(username, password, authorities);
        upat.setDetails(details);

        return upat;
    }

    private DirContext getLdapConnection(String username, String password) throws NamingException {
        Hashtable<String, String> env = new Hashtable<>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, ldap.getDn());
        env.put(Context.SECURITY_CREDENTIALS, password);
        env.put(Context.PROVIDER_URL, ldap.getUrl());

        return new InitialDirContext(env);
    }

}
