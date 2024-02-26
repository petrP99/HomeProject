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

class TransferTest {

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
    void createTransfer() {
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
                .user(user)
                .balance(BigDecimal.valueOf(50000))
                .firstName("Ivan")
                .lastName("Ivanov")
                .status(Status.ACTIVE)
                .createdTime(LocalDateTime.now())
                .build();

        Client client2 = Client.builder()
                .user(user2)
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

        Card card2 = Card.builder()
                .client(client2)
                .cardNo(GenerateNumberCardUtil.generateNumber())
                .balance(BigDecimal.valueOf(1000))
                .createdDate(LocalDate.now())
                .expireDate(ExpireDateUtil.calculateExpireDate())
                .status(Status.ACTIVE)
                .build();

        Transfer transfer = Transfer.builder()
                .cardNoFrom(card)
                .cardNoTo(card2)
                .amount(BigDecimal.valueOf(188))
                .timeOfTransfer(LocalDateTime.now())
                .status(Status.SUCCESS)
                .build();

        session.persist(user);
        session.persist(client);
        session.persist(card);
        session.persist(user2);
        session.persist(client2);
        session.persist(card2);

        session.persist(transfer);

        var transferId = session.get(Transfer.class, transfer.getId());

        assertThat(transferId).isNotNull();
    }

    @Test
    void updateTransfer() {
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
                .user(user)
                .balance(BigDecimal.valueOf(50000))
                .firstName("Ivan")
                .lastName("Ivanov")
                .status(Status.ACTIVE)
                .createdTime(LocalDateTime.now())
                .build();

        Client client2 = Client.builder()
                .user(user2)
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

        Card card2 = Card.builder()
                .client(client2)
                .cardNo(GenerateNumberCardUtil.generateNumber())
                .balance(BigDecimal.valueOf(1000))
                .createdDate(LocalDate.now())
                .expireDate(ExpireDateUtil.calculateExpireDate())
                .status(Status.ACTIVE)
                .build();

        Transfer transfer = Transfer.builder()
                .cardNoFrom(card)
                .cardNoTo(card2)
                .amount(BigDecimal.valueOf(188))
                .timeOfTransfer(LocalDateTime.now())
                .status(Status.SUCCESS)
                .build();

        session.persist(user);
        session.persist(client);
        session.persist(card);
        session.persist(user2);
        session.persist(client2);
        session.persist(card2);

        session.persist(transfer);

        transfer.setStatus(Status.FAILED);
        session.merge(transfer);

        var currentStatus = transfer.getStatus();

        assertThat(currentStatus).isEqualTo(Status.FAILED);
    }

    @Test
    void deleteTransfer() {
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
                .user(user)
                .balance(BigDecimal.valueOf(50000))
                .firstName("Ivan")
                .lastName("Ivanov")
                .status(Status.ACTIVE)
                .createdTime(LocalDateTime.now())
                .build();

        Client client2 = Client.builder()
                .user(user2)
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

        Card card2 = Card.builder()
                .client(client2)
                .cardNo(GenerateNumberCardUtil.generateNumber())
                .balance(BigDecimal.valueOf(1000))
                .createdDate(LocalDate.now())
                .expireDate(ExpireDateUtil.calculateExpireDate())
                .status(Status.ACTIVE)
                .build();

        Transfer transfer = Transfer.builder()
                .cardNoFrom(card)
                .cardNoTo(card2)
                .amount(BigDecimal.valueOf(188))
                .timeOfTransfer(LocalDateTime.now())
                .status(Status.SUCCESS)
                .build();

        session.persist(user);
        session.persist(client);
        session.persist(card);
        session.persist(user2);
        session.persist(client2);
        session.persist(card2);
        session.persist(transfer);

        session.remove(transfer);

        var deleteValue = session.get(Transfer.class, transfer.getId());

        assertThat(deleteValue).isNull();
    }
}