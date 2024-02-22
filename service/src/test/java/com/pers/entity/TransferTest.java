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

class TransferTest {

    @Test
    void createTransfer() {
        session.beginTransaction();
        session.persist(user);
        session.persist(client);
        session.persist(card);
        session.getTransaction().commit();

        session.persist(transfer);

        assertThat(transfer).isNotNull();
    }

    @Test
    void updateTransfer() {
        session.getTransaction().begin();

        transfer.setStatus(Status.FAILED);

        var currentStatus = transfer.getStatus();

        assertThat(currentStatus).isEqualTo(Status.FAILED);

        session.getTransaction().rollback();
    }

    @Test
    void deleteTransfer() {
        session.beginTransaction();
        session.persist(user2);
        session.persist(client2);
        session.persist(card2);
        session.persist(transfer2);

        session.remove(transfer2);
        session.getTransaction().commit();

        var deleteValue = session.get(Transfer.class, transfer.getId());

        assertThat(deleteValue).isNull();
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

    Transfer transfer = Transfer.builder()
            .cardNoFrom(card)
            .cardNoTo(card2)
            .amount(BigDecimal.valueOf(188))
            .timeOfTransfer(LocalDateTime.now())
            .status(Status.SUCCESS)
            .build();

    Transfer transfer2 = Transfer.builder()
            .cardNoFrom(card2)
            .cardNoTo(card2)
            .amount(BigDecimal.valueOf(188))
            .timeOfTransfer(LocalDateTime.now())
            .status(Status.SUCCESS)
            .build();
}