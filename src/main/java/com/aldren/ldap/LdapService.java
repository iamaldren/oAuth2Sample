package com.aldren.ldap;

import com.aldren.properties.LdapProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import java.util.Hashtable;

@Component
@EnableConfigurationProperties(LdapProperties.class)
public class LdapService {

    @Autowired
    private LdapProperties props;

    public DirContext ldapContext(String username, String password) throws NamingException {
        Hashtable<String,String> env = new Hashtable<>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, username);
        env.put(Context.SECURITY_CREDENTIALS, password);
        env.put(Context.PROVIDER_URL, props.getUrl());

        return new InitialDirContext(env);
    }

}
