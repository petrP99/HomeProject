package com.pers.entity;

import com.pers.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    private Session session;

    @BeforeEach
    void openSession() {
        SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
        this.session = sessionFactory.openSession();
        session.getTransaction().begin();
    }

    @AfterEach
    public void closeSession() {
        session.getTransaction().rollback();
    }

    User user = User.builder()
            .login("user1@gmail.com")
            .password("123")
            .role(Role.USER)
            .build();

    @Test
    void createUser() {
        session.persist(user);

        User userCreate = session.get(User.class, user.getId());

        assertThat(userCreate).isNotNull();
    }

    @Test
    void updateUser() {
        session.persist(user);
        user.setPassword("1234");
        session.merge(user);

        var newPassword = user.getPassword();

        assertThat(newPassword).isEqualTo("1234");
    }

    @Test
    void deleteUser() {
        session.persist(user);
        session.remove(user);

        User userAfterRemove = session.get(User.class, user.getId());

        assertThat(userAfterRemove).isNull();
    }
}