package com.pers;

import com.pers.entity.Card;
import com.pers.entity.Client;
import com.pers.entity.Payment;
import com.pers.entity.Replenishment;
import com.pers.entity.Role;
import com.pers.entity.Status;
import com.pers.entity.Transfer;
import com.pers.entity.User;
import com.pers.util.HibernateUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.pers.util.ExpireDateUtil.*;
import static com.pers.util.GenerateNumberCardUtil.*;
import static com.pers.util.HibernateUtil.startSession;

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

        User user3 = User.builder()
                .login("user2@gmail.com")
                .password("123")
                .role(Role.USER)
                .build();

        Client client1 = Client.builder()
                .userId(1l)
                .balance(BigDecimal.valueOf(50000))
                .firstName("Ivan")
                .lastName("Ivanov")
                .status(Status.ACTIVE)
                .createdTime(LocalDateTime.now())
                .build();

        Client client2 = Client.builder()
                .userId(2l)
                .balance(BigDecimal.valueOf(7000))
                .firstName("Petr")
                .lastName("Petrov")
                .status(Status.ACTIVE)
                .createdTime(LocalDateTime.now())
                .build();

        Card card1 = Card.builder()
                .clientId(client1)
                .cardNo(generateNumber())
                .balance(BigDecimal.valueOf(100))
                .createdDate(LocalDate.now())
                .expireDate(calculateExpireDate())
                .status(Status.ACTIVE)
                .build();

        Card card2 = Card.builder()
                .clientId(client2)
                .cardNo(generateNumber())
                .balance(BigDecimal.valueOf(1000))
                .createdDate(LocalDate.now())
                .expireDate(calculateExpireDate())
                .status(Status.ACTIVE)
                .build();

        Transfer transfer = Transfer.builder()
                .cardNoFrom(card1.getCardNo())
                .cardNoTo(card2.getCardNo())
                .amount(BigDecimal.valueOf(188))
                .timeOfTransfer(LocalDateTime.now())
                .status(Status.SUCCESS)
                .build();

        Payment payment = Payment.builder()
                .shopName("McDonalds")
                .amount(BigDecimal.valueOf(154))
                .payByClient(2l)
                .payByCardNo(card2.getCardNo())
                .timeOfPay(LocalDateTime.now())
                .status(Status.SUCCESS)
                .build();

        Replenishment replenishment = Replenishment.builder()
                .clientTo(1l)
                .cardNoTo(card1.getCardNo())
                .amount(BigDecimal.valueOf(9999))
                .timeOfReplenishment(LocalDateTime.now())
                .status(Status.SUCCESS)
                .build();


        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            startSession(user1, sessionFactory);
            startSession(admin, sessionFactory);
            startSession(client1, sessionFactory);
            startSession(user3, sessionFactory);
            startSession(client2, sessionFactory);
            startSession(card1, sessionFactory);
            startSession(card2, sessionFactory);
            startSession(transfer, sessionFactory);
            startSession(payment, sessionFactory);
            startSession(replenishment, sessionFactory);
        }
    }
}

