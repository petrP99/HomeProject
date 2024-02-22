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
import static org.junit.jupiter.api.Assertions.*;

class ReplenishmentTest {

    void persistEntity() {
        session.persist(user);
        session.persist(client);
        session.persist(card);
    }

    @Test
    void createReplenishment() {
        session.beginTransaction();
        persistEntity();
        session.getTransaction().commit();

        session.persist(replenishment);

        assertThat(replenishment).isNotNull();
    }

    @Test
    void updateReplenishment() {
        session.getTransaction().begin();

        replenishment.setStatus(Status.FAILED);

        var currentStatus = replenishment.getStatus();

        assertThat(currentStatus).isEqualTo(Status.FAILED);

        session.getTransaction().rollback();
    }

    @Test
    void deleteReplenishment() {
        session.beginTransaction();

        session.remove(replenishment);
        session.getTransaction().commit();

        assertThat(replenishment).isNull();
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

    Replenishment replenishment = Replenishment.builder()
            .clientTo(client)
            .cardNoTo(card)
            .amount(BigDecimal.valueOf(9999))
            .timeOfReplenishment(LocalDateTime.now())
            .status(Status.SUCCESS)
            .build();
}