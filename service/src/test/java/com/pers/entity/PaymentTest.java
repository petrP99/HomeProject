package com.pers.entity;

import com.pers.util.ExpireDateUtil;
import com.pers.util.GenerateNumberCardUtil;
import com.pers.util.HibernateUtil;
import lombok.Cleanup;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.setMaxStackTraceElementsDisplayed;
import static org.junit.jupiter.api.Assertions.*;

class PaymentTest {

    @Test
    void createPayment() {
        session.beginTransaction();
        session.persist(user);
        session.persist(client);
        session.persist(card);
        session.getTransaction().commit();

        session.persist(payment);

        assertThat(payment).isNotNull();
    }

    @Test
    void updatePayment() {
        session.getTransaction().begin();

        payment.setStatus(Status.FAILED);

        var currentStatus = payment.getStatus();

        assertThat(currentStatus).isEqualTo(Status.FAILED);

        session.getTransaction().rollback();
    }

    @Test
    void deletePayment() {
        session.beginTransaction();

        session.remove(payment);
        session.getTransaction().commit();

        assertThat(payment).isNull();
    }

    SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
    Session session = sessionFactory.openSession();

    User user = User.builder()
            .login("user1@gmail.com")
            .password("123")
            .role(Role.USER)
            .build();
    User user2 = User.builder()
            .login("user2@gmail.com")
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

    Client client2 = Client.builder()
            .userId(user2)
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

    Card card2 = Card.builder()
            .clientId(client2)
            .cardNo(GenerateNumberCardUtil.generateNumber())
            .balance(BigDecimal.valueOf(1000))
            .createdDate(LocalDate.now())
            .expireDate(ExpireDateUtil.calculateExpireDate())
            .status(Status.ACTIVE)
            .build();

    Payment payment = Payment.builder()
            .shopName("ReStore")
            .amount(BigDecimal.valueOf(154))
            .payByClientId(client)
            .payByCardNo(card)
            .timeOfPay(LocalDateTime.now())
            .status(Status.SUCCESS)
            .build();

    Payment payment2 = Payment.builder()
            .shopName("ReStore")
            .amount(BigDecimal.valueOf(154))
            .payByClientId(client2)
            .payByCardNo(card2)
            .timeOfPay(LocalDateTime.now())
            .status(Status.SUCCESS)
            .build();
}