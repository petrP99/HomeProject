package com.pers.entity;

import com.pers.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class ClientTest {

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

    @Test
    void createClient() {
        User user = User.builder()
                .login("user1@gmail.com")
                .password("123")
                .role(Role.USER)
                .build();

        Client client = Client.builder()
                .user(user)
                .balance(BigDecimal.valueOf(50000))
                .firstName("Ivan")
                .lastName("Ivanov")
                .status(Status.ACTIVE)
                .createdTime(LocalDateTime.now())
                .build();

        assertThat(client).isNotNull();
    }

    @Test
    void updateClient() {
        User user = User.builder()
                .login("user1@gmail.com")
                .password("123")
                .role(Role.USER)
                .build();

        Client client = Client.builder()
                .user(user)
                .balance(BigDecimal.valueOf(50000))
                .firstName("Ivan")
                .lastName("Ivanov")
                .status(Status.ACTIVE)
                .createdTime(LocalDateTime.now())
                .build();

        session.persist(user);
        session.persist(client);

        client.setFirstName("Valentin");

        var newName = client.getFirstName();

        assertThat(newName).isEqualTo("Valentin");
    }

    @Test
    void deleteClient() {
        User user = User.builder()
                .login("user1@gmail.com")
                .password("123")
                .role(Role.USER)
                .build();

        Client client = Client.builder()
                .user(user)
                .balance(BigDecimal.valueOf(50000))
                .firstName("Ivan")
                .lastName("Ivanov")
                .status(Status.ACTIVE)
                .createdTime(LocalDateTime.now())
                .build();

        session.persist(user);
        session.persist(client);
        session.remove(client);

        Client clientAfterRemove = session.get(Client.class, client.getId());

        assertThat(clientAfterRemove).isNull();
    }
}