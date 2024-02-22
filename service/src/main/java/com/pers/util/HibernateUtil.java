package com.pers.util;

import com.pers.entity.Card;
import com.pers.entity.Client;
import com.pers.entity.Payment;
import com.pers.entity.Replenishment;
import com.pers.entity.Role;
import com.pers.entity.Status;
import com.pers.entity.Transfer;
import com.pers.entity.User;
import lombok.experimental.UtilityClass;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.hibernate.cfg.Configuration;

@UtilityClass
public class HibernateUtil {
    public static SessionFactory buildSessionFactory() {
        Configuration configuration = new Configuration();
        configuration.setPhysicalNamingStrategy(new CamelCaseToUnderscoresNamingStrategy());
        configuration.addAnnotatedClass(User.class);
        configuration.addAnnotatedClass(Card.class);
        configuration.addAnnotatedClass(Client.class);
        configuration.addAnnotatedClass(Payment.class);
        configuration.addAnnotatedClass(Replenishment.class);
        configuration.addAnnotatedClass(Role.class);
        configuration.addAnnotatedClass(Status.class);
        configuration.addAnnotatedClass(Transfer.class);
        configuration.configure();

        return configuration.buildSessionFactory();
    }

    public static void startSession(Object object, SessionFactory sessionFactory) {
        try (Session session1 = sessionFactory.openSession()) {
            session1.beginTransaction();
            session1.persist(object);
            session1.getTransaction().commit();
        }
    }
}


