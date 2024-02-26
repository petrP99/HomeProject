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

class PaymentTest {

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
    void createPayment() {
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

        Payment payment = Payment.builder()
                .shopName("ReStore")
                .amount(BigDecimal.valueOf(154))
                .client(client)
                .card(card)
                .timeOfPay(LocalDateTime.now())
                .status(Status.SUCCESS)
                .build();

        session.persist(user);
        session.persist(client);
        session.persist(card);
        session.persist(payment);

        var createdPayment = session.get(Payment.class, payment.getId());

        assertThat(createdPayment).isNotNull();
    }

    @Test
    void updatePayment() {
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

        Payment payment = Payment.builder()
                .shopName("ReStore")
                .amount(BigDecimal.valueOf(154))
                .client(client)
                .card(card)
                .timeOfPay(LocalDateTime.now())
                .status(Status.SUCCESS)
                .build();

        session.persist(user);
        session.persist(client);
        session.persist(card);
        session.persist(payment);
        payment.setStatus(Status.FAILED);
        session.merge(payment);

        var currentStatus = payment.getStatus();

        assertThat(currentStatus).isEqualTo(Status.FAILED);
    }

    @Test
    void deletePayment() {
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

        Payment payment = Payment.builder()
                .shopName("ReStore")
                .amount(BigDecimal.valueOf(154))
                .client(client)
                .card(card)
                .timeOfPay(LocalDateTime.now())
                .status(Status.SUCCESS)
                .build();

        session.persist(user);
        session.persist(client);
        session.persist(card);
        session.persist(payment);
        session.remove(payment);

        var paymentId = session.get(Payment.class, payment.getId());

        assertThat(paymentId).isNull();
    }
}