package com.pers.entity;

import com.pers.util.ExpireDateUtil;
import com.pers.util.GenerateNumberCardUtil;
import com.pers.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class CardTest {

    @Test
    void createCard() {
        persistEntity();

        assertThat(card).isNotNull();

        session.getTransaction().rollback();
    }

    @Test
    void deleteCard() {
        persistEntity();
        session.remove(card);
        session.remove(user);
        session.getTransaction().commit();

        Card deletedCard = session.get(Card.class, card.getId());

        assertThat(deletedCard).isNull();
    }

    @Test
    void UpdateCard() {
        card.setStatus(Status.BLOCKED);

        var updatedStatus = card.getStatus();

        assertThat(updatedStatus).isEqualTo(Status.BLOCKED);

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

    Card card = Card.builder()
            .clientId(client)
            .cardNo(GenerateNumberCardUtil.generateNumber())
            .balance(BigDecimal.valueOf(1000))
            .createdDate(LocalDate.now())
            .expireDate(ExpireDateUtil.calculateExpireDate())
            .status(Status.ACTIVE)
            .build();

    void persistEntity() {
        session.beginTransaction();
        session.persist(user);
        session.persist(client);
        session.persist(card);
    }
}