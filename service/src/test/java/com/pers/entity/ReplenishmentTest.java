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

class ReplenishmentTest {

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
    void createReplenishment() {
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

        Replenishment replenishment = Replenishment.builder()
                .clientTo(client)
                .cardNoTo(card)
                .amount(BigDecimal.valueOf(9999))
                .timeOfReplenishment(LocalDateTime.now())
                .status(Status.SUCCESS)
                .build();

        session.persist(user);
        session.persist(client);
        session.persist(card);

        session.persist(replenishment);

        assertThat(replenishment).isNotNull();
    }

    @Test
    void updateReplenishment() {
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

        Replenishment replenishment = Replenishment.builder()
                .clientTo(client)
                .cardNoTo(card)
                .amount(BigDecimal.valueOf(9999))
                .timeOfReplenishment(LocalDateTime.now())
                .status(Status.SUCCESS)
                .build();

        session.persist(user);
        session.persist(client);
        session.persist(card);

        replenishment.setStatus(Status.FAILED);
        session.merge(replenishment);

        var newStatus = replenishment.getStatus();

        assertThat(newStatus).isEqualTo(Status.FAILED);
    }

    @Test
    void deleteReplenishment() {
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

        Replenishment replenishment = Replenishment.builder()
                .clientTo(client)
                .cardNoTo(card)
                .amount(BigDecimal.valueOf(9999))
                .timeOfReplenishment(LocalDateTime.now())
                .status(Status.SUCCESS)
                .build();

        session.persist(user);
        session.persist(client);
        session.persist(card);
        session.persist(replenishment);
        session.remove(replenishment);
        var replenishmentId = session.get(Replenishment.class, replenishment.getId());

        assertThat(replenishmentId).isNull();
    }
}