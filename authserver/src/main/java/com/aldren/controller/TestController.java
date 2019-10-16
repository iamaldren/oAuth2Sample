package com.aldren.controller;

import com.aldren.ldap.LdapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.NamingException;

@RestController
@RequestMapping("/api")
public class TestController {

    @Autowired
    private LdapService ldap;

    @GetMapping("/test/{username}")
    public void test(@PathVariable("username") String username) throws NamingException {
        ldap.authenticate(username, "password");
    }

}
