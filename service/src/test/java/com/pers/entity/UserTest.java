package com.pers.entity;

import com.pers.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    @Test
    void createUser() {
        persistEntity();

        User userCreate = session.get(User.class, user.getId());

        assertThat(userCreate).isNotNull();

        session.getTransaction().rollback();
    }

    @Test
    void updateUser() {
        persistEntity();

        user.setPassword("1234");

        var newPassword = user.getPassword();

        assertThat(newPassword).isEqualTo("1234");

        session.getTransaction().rollback();
    }

    @Test
    void deleteUser() {
        persistEntity();
        session.remove(user);
        session.getTransaction().commit();

        User userAfterRemove = session.get(User.class, user.getId());

        assertThat(userAfterRemove).isNull();
    }

    SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
    Session session = sessionFactory.openSession();

    User user = User.builder()
            .login("user1@gmail.com")
            .password("123")
            .role(Role.USER)
            .build();

    void persistEntity() {
        session.beginTransaction();
        session.persist(user);
    }
}