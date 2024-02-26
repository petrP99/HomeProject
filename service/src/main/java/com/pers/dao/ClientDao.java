package com.pers.dao;

import com.pers.dto.Filter;
import com.pers.entity.Client;
import com.pers.entity.Client_;
import com.pers.entity.Payment;
import com.pers.entity.Payment_;
import com.pers.entity.QClient;
import com.pers.entity.User_;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.Session;
import org.hibernate.graph.GraphSemantic;
import org.hibernate.graph.RootGraph;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientDao {

    @Getter
    private static final ClientDao INSTANCE = new ClientDao();

    public List<Client> queryFindAllByFilter(Session session, Filter filter) {
        var predicate = QPredicate.builder()
                .add(filter.getFirstName(), QClient.client.firstName::eq)
                .add(filter.getLastName(), QClient.client.lastName::eq)
                .add(filter.getStatus(), QClient.client.status::eq)
                .buildAnd();

        return new JPAQuery<Client>(session)
                .select(QClient.client)
                .from(QClient.client)
                .where(predicate)
                .fetch();
    }

    public List<Client> criteriaFindAllByFilter(Session session) {
        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(Client.class);
        var client = criteria.from(Client.class);

//        List<Predicate> predicates = new ArrayList<>();
//        if (filter.getFirstName() != null) {
//            predicates.add(cb.equal(client.get(Client_.firstName)), filter.getFirstName()));
//        }

        RootGraph<Payment> clientGraph = session.createEntityGraph(Payment.class);
        clientGraph.addAttributeNodes("client");
        criteria.select(client);

        return session.createQuery(criteria)
                .setHint(GraphSemantic.LOAD.getJakartaHintName(), clientGraph)
                .list();
    }

    public List<Client> findAllByFirstAndLastNames(Session session) {
//        return new JPAQuery<Client>(session)
//                .select(QClient.client)
//                .from(QClient.client)
//                .fetch();
        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(Client.class);
        var client = criteria.from(Client.class);
        criteria.select(client);
        return session.createQuery(criteria)
                .list();
    }

    public List<Client> findAllByFirstName(Session session, String firstname) {
//        return new JPAQuery<Client>(session)
//                .select(QClient.client)
//                .from(QClient.client)
//                .where(QClient.client.firstName.eq(firstname))
//                .fetch();
        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(Client.class);
        var client = criteria.from(Client.class);
        criteria.select(client).where(cb.equal(client.get(Client_.FIRST_NAME), firstname));
        return session.createQuery(criteria)
                .list();
    }

    public List<Client> findAllByUserId(Session session, Long id) {
//return new JPAQuery<Client>(session)
//        .select(QClient.client)
//        .from(QClient.client)
//        .where(QClient.client.user.id.eq(id))
//        .fetch();
        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(Client.class);
        var client = criteria.from(Client.class);
        criteria.select(client).where(cb.equal(client.get(Client_.USER).get(User_.ID), id));
        return session.createQuery(criteria)
                .list();
    }

    public List<Payment> findAllPaymentsByClientId(Session session, Long id) {
        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(Payment.class);
        var payment = criteria.from(Payment.class);
        var client = payment.join(Payment_.CLIENT);
        criteria.select(payment)
                .where(
                        cb.equal(client.get(Client_.ID), id));
        return session.createQuery(criteria)
                .list();
    }
}
