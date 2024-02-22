package com.pers;

import com.pers.entity.Card;
import com.pers.entity.Client;
import com.pers.entity.Payment;
import com.pers.entity.Replenishment;
import com.pers.entity.Role;
import com.pers.entity.Status;
import com.pers.entity.Transfer;
import com.pers.entity.User;
import com.pers.util.ExpireDateUtil;
import com.pers.util.GenerateNumberCardUtil;
import com.pers.util.HibernateUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
public class HibernateRunner {

    public static void main(String[] args) throws SQLException {
        User user1 = User.builder()
                .login("user1@gmail.com")
                .password("123")
                .role(Role.USER)
                .build();

        User admin = User.builder()
                .login("admin1@gmail.com")
                .password("123")
                .role(Role.ADMIN)
                .build();

        User user2 = User.builder()
                .login("user2@gmail.com")
                .password("123")
                .role(Role.USER)
                .build();

        Client client1 = Client.builder()
                .userId(user1)
                .balance(BigDecimal.valueOf(50000))
                .firstName("Ivan")
                .lastName("Ivanov")
                .status(Status.ACTIVE)
                .createdTime(LocalDateTime.now())
                .build();

        Client client2 = Client.builder()
                .userId(user2)
                .balance(BigDecimal.valueOf(7000))
                .firstName("Petr")
                .lastName("Petrov")
                .status(Status.ACTIVE)
                .createdTime(LocalDateTime.now())
                .build();

        Card card1 = Card.builder()
                .clientId(client1)
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
                .cardNoFrom(card1)
                .cardNoTo(card2)
                .amount(BigDecimal.valueOf(188))
                .timeOfTransfer(LocalDateTime.now())
                .status(Status.SUCCESS)
                .build();

        Payment payment = Payment.builder()
                .shopName("McDonalds")
                .amount(BigDecimal.valueOf(154))
                .payByClientId(client2)
                .payByCardNo(card2)
                .timeOfPay(LocalDateTime.now())
                .status(Status.SUCCESS)
                .build();

        Replenishment replenishment = Replenishment.builder()
                .clientTo(client1)
                .cardNoTo(card1)
                .amount(BigDecimal.valueOf(9999))
                .timeOfReplenishment(LocalDateTime.now())
                .status(Status.SUCCESS)
                .build();

        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            HibernateUtil.startSession(user1, sessionFactory);
            HibernateUtil.startSession(admin, sessionFactory);
            HibernateUtil.startSession(client1, sessionFactory);
            HibernateUtil.startSession(user2, sessionFactory);
            HibernateUtil.startSession(client2, sessionFactory);
            HibernateUtil.startSession(card1, sessionFactory);
            HibernateUtil.startSession(card2, sessionFactory);
            HibernateUtil.startSession(transfer, sessionFactory);
            HibernateUtil.startSession(payment, sessionFactory);
            HibernateUtil.startSession(replenishment, sessionFactory);
        }

    }
}

