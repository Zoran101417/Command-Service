package com.command_service.DBConnection;

import com.command_service.model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class HibernateConfig {

    private static final Logger LOG = LogManager.getLogger(HibernateConfig.class);

    private static SessionFactory sessionFactory;


    public static Session getSession() {
        Session session = buildSessionFactory().getCurrentSession();
//        return session != null ? session : buildSessionFactory().openSession();
        if(session != null) {
            LOG.info("Current session ");
            return session;
        } else {
            LOG.info("New session");
            return buildSessionFactory().openSession();
        }
    }


    private static SessionFactory buildSessionFactory() {
        try {
            // Create the SessionFactory form hibernate.cfg.xml
            Configuration configuration = new Configuration();
            configuration.configure().addAnnotatedClass(University.class)
                                            .addAnnotatedClass(Faculty.class)
                                            .addAnnotatedClass(Profesor.class)
                                            .addAnnotatedClass(Subject.class)
                                            .addAnnotatedClass(Student.class)
                                            .addAnnotatedClass(STDSubject.class)
                                            .addAnnotatedClass(User.class)
                                            .addAnnotatedClass(Role.class)
                                            .addAnnotatedClass(UserRole.class)
                                            .addAnnotatedClass(Permission.class)
                                            .addAnnotatedClass(RolePermission.class);

            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();

            return configuration.buildSessionFactory(serviceRegistry);
        }
        catch (Throwable ex) {
            LOG.error("Initial SessionFactory creation failed." + ex);
            ex.printStackTrace();
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        if(sessionFactory == null) sessionFactory = buildSessionFactory();
        return sessionFactory;
    }
}
