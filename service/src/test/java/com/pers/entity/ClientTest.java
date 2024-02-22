package com.pers.entity;

import com.pers.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class ClientTest {

    @Test
    void createClient() {
        persistEntity();

        assertThat(client).isNotNull();

        session.getTransaction().rollback();
    }

    @Test
    void updateClient() {
        persistEntity();
        client.setFirstName("Valentin");

        var newName = client.getFirstName();

        assertThat(newName).isEqualTo("Valentin");

        session.getTransaction().rollback();
    }

    @Test
    void deleteClient() {
        persistEntity();
        session.remove(client);

        Client clientAfterRemove = session.get(Client.class, client.getId());

        assertThat(clientAfterRemove).isNull();

        session.getTransaction().rollback();
    }

    SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
    Session session = sessionFactory.openSession();

    User user = User.builder()
            .login("user1@gmail.com")
            .password("123")
            .role(Role.USER)
            .build();

    Client client = Client.builder()
            .userId(user)
            .balance(BigDecimal.valueOf(50000))
            .firstName("Ivan")
            .lastName("Ivanov")
            .status(Status.ACTIVE)
            .createdTime(LocalDateTime.now())
            .build();

    void persistEntity() {
        session.beginTransaction();
        session.persist(user);
        session.persist(client);
    }
}