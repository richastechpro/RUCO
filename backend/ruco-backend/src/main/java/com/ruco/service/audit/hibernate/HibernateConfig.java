package com.ruco.service.audit.hibernate;

import javax.annotation.PostConstruct;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HibernateConfig {

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private AuditInterceptor auditInterceptor;

    @PostConstruct
    public void registerInterceptor() {
        sessionFactory
                .withOptions()
                .interceptor(auditInterceptor)
                .openSession();
    }
}