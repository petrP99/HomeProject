package com.pers.dao;

import com.pers.dto.Filter;
import com.pers.entity.Client;
import com.pers.entity.Payment;
import com.pers.entity.Status;
import com.pers.entity.User;
import com.pers.util.HibernateUtil;
import com.pers.util.TestDataImporter;
import lombok.Cleanup;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@TestInstance(PER_CLASS)
class ClientDaoTest {
    private final ClientDao clientDao = ClientDao.getINSTANCE();
    private final SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();

    @BeforeAll
    void initDb() {
        TestDataImporter.importData(sessionFactory);
    }

    @AfterAll
    void closeSession() {
        sessionFactory.close();
    }

    @Test
    void queryFindAllByFilter() {
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        var status = Status.ACTIVE;
        Filter filter = Filter.builder()
                .status(status)
                .build();

        List<Client> result = clientDao.queryFindAllByFilter(session, filter);
        assertThat(result).hasSize(3);
        session.getTransaction().commit();

    }

    @Test
    void criteriaFindAllByFilter() {
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        List<Client> result = clientDao.criteriaFindAllByFilter(session);

        assertThat(result).hasSize(5);
        session.getTransaction().commit();
    }

    /* Старые тесты*/


    @Test
    void findAllByFirstAndLastNames() {
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        List<Client> result = clientDao.findAllByFirstAndLastNames(session);
        assertThat(result).hasSize(5);

        List<String> firstAndLastNames = result.stream()
                .map(user -> user.getFirstName() + " " + user.getLastName())
                .toList();
        assertThat(firstAndLastNames).containsAnyOf(
                "Leonid Agutin",
                "Oleg LSP",
                "Valera Leontiev",
                "Oleg Gazmanov",
                "Dima Bilan");
        session.getTransaction().commit();
    }

    @Test
    void findAllByFirstName() {
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        List<Client> result = clientDao.findAllByFirstName(session, "Oleg");
        assertThat(result).hasSize(2);

        assertThat(result.get(0).getFirstName()).isEqualTo("Oleg");

        session.getTransaction().commit();
    }

    @Test
    void findAllByUserId() {
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();
        List<Client> result = clientDao.findAllByUserId(session, 1l);
        assertThat(result.get(0).getUser().getId()).isEqualTo(1l);

        session.getTransaction().commit();
    }

    @Test
    void findAllPaymentsByClientId() {
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();
        List<Payment> result = clientDao.findAllPaymentsByClientId(session, 1L);

        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(2);

        session.getTransaction().commit();
    }
}