package com.aldren.ldap;

import com.aldren.properties.LdapProperties;
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

    @Autowired
    private LdapProperties ldap;

    private static final String MAIL = "mail";
    private static final String CN = "cn";
    private static final String OBJECT_CLASS = "objectClass";

    public UsernamePasswordAuthenticationToken authenticate(String username, String password) throws NamingException {
        DirContext ctx = null;

        Map<String,String> details = new HashMap<>();
        List<GrantedAuthority> authorities = new ArrayList<>();

        try {
            ctx = getLdapConnection(username, password);

            String searchFilter = "(&(objectClass=*))";
            SearchControls searchControls = new SearchControls();
            searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);

            String[] returnedAttr = new String[]{MAIL, CN, OBJECT_CLASS};

            StringBuilder builder = new StringBuilder("uid=");
            builder.append(username)
                    .append(",dc=example,dc=com");

            NamingEnumeration<SearchResult> ne = ctx.search(builder.toString(), searchFilter, searchControls);
            while (ne.hasMoreElements())
            {
                SearchResult match = ne.nextElement();

                Attributes attrs = match.getAttributes();

                NamingEnumeration e = attrs.getAll();

                while (e.hasMoreElements())
                {
                    Attribute attr = (Attribute) e.nextElement();
                    if(attr.getID().equals(OBJECT_CLASS)) {
                        for (int i=0; i < attr.size(); i++)
                        {
                            authorities.add(new SimpleGrantedAuthority((String) attr.get(i)));
                        }
                    } else {
                        details.put(attr.getID(), (String) attr.get(0));
                    }
                }
            }
        } finally {
            if(ctx != null) {
                ctx.close();
            }
        }

        UsernamePasswordAuthenticationToken upat = new UsernamePasswordAuthenticationToken(username, password, authorities);
        upat.setDetails(details);

        return upat;
    }

    private DirContext getLdapConnection(String username, String password) throws NamingException {
        Hashtable<String,String> env = new Hashtable<>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, ldap.getDn());
        env.put(Context.SECURITY_CREDENTIALS, password);
        env.put(Context.PROVIDER_URL, ldap.getUrl());

        return new InitialDirContext(env);
    }

}
