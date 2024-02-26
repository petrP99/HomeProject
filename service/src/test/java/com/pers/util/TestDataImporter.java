package com.pers.util;

import com.pers.entity.Card;
import com.pers.entity.Client;
import com.pers.entity.Payment;
import com.pers.entity.Replenishment;
import com.pers.entity.Role;
import com.pers.entity.Status;
import com.pers.entity.Transfer;
import com.pers.entity.User;
import lombok.Cleanup;
import lombok.experimental.UtilityClass;
import org.eclipse.jdt.internal.compiler.ast.UsesStatement;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.sqm.produce.function.StandardFunctionReturnTypeResolvers;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;

@UtilityClass
public class TestDataImporter {

    public static void importData(SessionFactory sessionFactory) {
        @Cleanup Session session = sessionFactory.openSession();
        session.getTransaction().begin();

        User user1 = persistUser(session, "user1@mail.com", "123", Role.USER);
        User user2 = persistUser(session, "user2@mail.com", "123", Role.USER);
        User user3 = persistUser(session, "user3@mail.com", "123", Role.USER);
        User user4 = persistUser(session, "user4@mail.com", "123", Role.USER);
        User user5 = persistUser(session, "user5@mail.com", "123", Role.USER);
        User admin1 = persistUser(session, "admin1@mail.com", "123", Role.ADMIN);
        User admin2 = persistUser(session, "admin2@mail.com", "123", Role.ADMIN);

        Client client1 = persistClient(session, user1, new BigDecimal("1000"), "Leonid", "Agutin", Status.BLOCKED);
        Client client2 = persistClient(session, user2, new BigDecimal("7789"), "Oleg", "LSP", Status.ACTIVE);
        Client client3 = persistClient(session, user3, new BigDecimal("99"), "Valera", "Leontiev", Status.ACTIVE);
        Client client4 = persistClient(session, user4, new BigDecimal("11060"), "Oleg", "Gazmanov", Status.ACTIVE);
        Client client5 = persistClient(session, user5, new BigDecimal("27000"), "Dima", "Bilan", Status.INACTIVE);

        Card card1 = persistCard(session, client1, new BigDecimal("1000"), Status.ACTIVE);
        Card card2 = persistCard(session, client2, new BigDecimal("7789"), Status.BLOCKED);
        Card card3 = persistCard(session, client3, new BigDecimal("99"), Status.ACTIVE);
        Card card4 = persistCard(session, client4, new BigDecimal("11060"), Status.ACTIVE);
        Card card5 = persistCard(session, client5, new BigDecimal("27000"), Status.BLOCKED);

        Payment payment1 = persistPayment(session, "amazon", new BigDecimal("23"), client1, card1, Status.SUCCESS);
        Payment payment2 = persistPayment(session, "dns-shop", new BigDecimal("100"), client2, card2, Status.FAILED);
        Payment payment3 = persistPayment(session, "ozon", new BigDecimal("99"), client3, card3, Status.SUCCESS);
        Payment payment4 = persistPayment(session, "aliexpress", new BigDecimal("1298"), client4, card4, Status.SUCCESS);
        Payment payment5 = persistPayment(session, "reStore", new BigDecimal("263"), client4, card4, Status.SUCCESS);
        Payment payment6 = persistPayment(session, "amazon", new BigDecimal("13"), client1, card1, Status.FAILED);
        Payment payment7 = persistPayment(session, "aliexpress", new BigDecimal("2123"), client5, card5, Status.FAILED);
        Payment payment8 = persistPayment(session, "amazon", new BigDecimal("75"), client3, card3, Status.SUCCESS);
        Payment payment9 = persistPayment(session, "reStore", new BigDecimal("101"), client3, card3, Status.SUCCESS);

        Replenishment replenishment1 = persistReplenishment(session, client1, card1, new BigDecimal("800"), Status.SUCCESS);
        Replenishment replenishment2 = persistReplenishment(session, client4, card4, new BigDecimal("1700"), Status.SUCCESS);
        Replenishment replenishment3 = persistReplenishment(session, client3, card3, new BigDecimal("777"), Status.FAILED);
        Replenishment replenishment4 = persistReplenishment(session, client4, card4, new BigDecimal("1200"), Status.SUCCESS);

        Transfer transfer1 = persistTransfer(session, card1, card2, new BigDecimal("542"), Status.FAILED);
        Transfer transfer2 = persistTransfer(session, card3, card5, new BigDecimal("33"), Status.FAILED);
        Transfer transfer3 = persistTransfer(session, card4, card1, new BigDecimal("861"), Status.SUCCESS);
        Transfer transfer4 = persistTransfer(session, card4, card3, new BigDecimal("2573"), Status.SUCCESS);
        Transfer transfer5 = persistTransfer(session, card3, card4, new BigDecimal("79"), Status.SUCCESS);
        Transfer transfer6 = persistTransfer(session, card2, card5, new BigDecimal("1111"), Status.FAILED);
    }

    public User persistUser(Session session, String login, String password, Role role) {
        User user = User.builder()
                .login(login)
                .password(password)
                .role(role)
                .build();
        session.persist(user);

        return user;
    }

    public Client persistClient(Session session, User user, BigDecimal balance, String firstName,
                                String lastName, Status status) {
        Client client = Client.builder()
                .user(user)
                .balance(balance)
                .firstName(firstName)
                .lastName(lastName)
                .status(status)
                .createdTime(LocalDateTime.now())
                .build();
        session.persist(client);

        return client;
    }

    public Card persistCard(Session session, Client client, BigDecimal balance, Status status) {
        Card card = Card.builder()
                .client(client)
                .cardNo(GenerateNumberCardUtil.generateNumber())
                .balance(balance)
                .createdDate(LocalDate.now())
                .expireDate(ExpireDateUtil.calculateExpireDate())
                .status(status)
                .build();
        session.persist(card);

        return card;
    }

    public Payment persistPayment(Session session, String shopName, BigDecimal amount, Client client, Card card,
                                  Status status) {
        Payment payment = Payment.builder()
                .shopName(shopName)
                .amount(amount)
                .client(client)
                .card(card)
                .timeOfPay(LocalDateTime.now())
                .status(status)
                .build();
        session.persist(payment);

        return payment;
    }

    public Replenishment persistReplenishment(Session session, Client client, Card card, BigDecimal amount, Status status) {
        Replenishment replenishment = Replenishment.builder()
                .clientTo(client)
                .cardNoTo(card)
                .amount(amount)
                .timeOfReplenishment(LocalDateTime.now())
                .status(status)
                .build();
        session.persist(replenishment);

        return replenishment;
    }

    public Transfer persistTransfer(Session session, Card card1, Card card2, BigDecimal amount, Status status) {
        Transfer transfer = Transfer.builder()
                .cardNoFrom(card1)
                .cardNoTo(card2)
                .amount(amount)
                .timeOfTransfer(LocalDateTime.now())
                .status(status)
                .build();
        session.persist(transfer);

        return transfer;
    }
}
