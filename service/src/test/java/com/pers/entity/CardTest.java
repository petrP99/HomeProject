package com.pers.entity;

import com.pers.util.ExpireDateUtil;
import com.pers.util.GenerateNumberCardUtil;
import com.pers.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class CardTest {

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

    Client client = Client.builder()
            .user(user)
            .balance(BigDecimal.valueOf(50000))
            .firstName("Ivan")
            .lastName("Ivanov")
            .status(Status.ACTIVE)
            .createdTime(LocalDateTime.now())
            .build();

    Card card = Card.builder()
            .client(client)
            .cardNo(GenerateNumberCardUtil.generateNumber())
            .balance(BigDecimal.valueOf(1000))
            .createdDate(LocalDate.now())
            .expireDate(ExpireDateUtil.calculateExpireDate())
            .status(Status.ACTIVE)
            .build();

    @Test
    void createCard() {
        session.persist(user);
        session.persist(client);
        session.persist(card);

        assertThat(card).isNotNull();
    }

    @Test
    void updateCard() {
        session.persist(user);
        session.persist(client);
        session.persist(card);

        card.setStatus(Status.BLOCKED);

        session.merge(card);
        var updatedStatus = card.getStatus();

        assertThat(updatedStatus).isEqualTo(Status.BLOCKED);
    }

    @Test
    void deleteCard() {
        session.persist(user);
        session.persist(client);
        session.persist(card);
        session.remove(card);
        session.remove(user);

        Card deletedCard = session.get(Card.class, card.getId());

        assertThat(deletedCard).isNull();
    }
}