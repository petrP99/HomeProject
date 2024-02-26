package com.pers.dao;

import com.pers.dto.Filter;
import com.pers.entity.Client;
import com.pers.entity.Payment;
import com.pers.entity.Status;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@TestInstance(PER_CLASS)
class ClientDaoIT {
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

    /**
     * Так и не осилил фильтр в критерии
     */

    @Test
    void criteriaFindAllByFilter() {
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        List<Client> result = clientDao.criteriaFindAllByFilter(session);

        assertThat(result).hasSize(5);
        session.getTransaction().commit();
    }
}