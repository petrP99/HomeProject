package com.pers.dao;

import com.pers.entity.Role;
import com.pers.entity.User;
import com.pers.util.HibernateUtil;
import com.pers.util.TestDataImporter;
import lombok.Cleanup;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.spi.AbstractDelegatingMetadata;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@TestInstance(PER_CLASS)
class UserDaoTest {

    private final UserDao userDao = UserDao.getINSTANCE();
    private final SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();

    @BeforeAll
    void initDb() {
        TestDataImporter.importData(sessionFactory);
    }

    @AfterAll
    void closeSession() {
        sessionFactory.close();
    }

    @Test
    void findAll() {
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        List<User> result = userDao.findAll(session);
        assertThat(result).hasSize(7);

        List<String> users = result.stream()
                .map(User::getLogin).toList();
        assertThat(users).containsAnyOf("user1@mail.com",
                "user2@mail.com", "user3@mail.com", "user4@mail.com",
                "user5@mail.com", "admin1@mail.com", "admin2@mail.com");
        session.getTransaction().commit();
    }

    @Test
    void findByLogin() {
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        List<User> result = userDao.findByLogin(session, "user3@mail.com");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getLogin()).isEqualTo("user3@mail.com");

        session.getTransaction().commit();
    }

    @Test
    void findAllAdmins() {
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        List<User> result = userDao.findAllAdmins(session, Role.ADMIN);
        assertThat(result).hasSize(2);
        List<String> admins = result.stream()
                .map(User::getLogin).toList();

        assertThat(admins).containsAnyOf("admin1@mail.com", "admin2@mail.com");

        session.getTransaction().commit();
    }
}