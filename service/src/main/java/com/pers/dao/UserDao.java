package com.pers.dao;

import com.pers.entity.QUser;
import com.pers.entity.Role;
import com.pers.entity.User;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.Session;

import java.util.Collections;
import java.util.List;

import static com.pers.entity.QUser.user;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserDao {

    @Getter
    private static final UserDao INSTANCE = new UserDao();

    public List<User> findAll(Session session) {
        return new JPAQuery<User>(session)
                .select(user)
                .from(user)
                .fetch();
    }

    public List<User> findByLogin(Session session, String login) {
        return new JPAQuery<User>(session)
                .select(user)
                .from(user)
                .where(user.login.eq(login))
                .fetch();
    }

    public List<User> findAllAdmins(Session session, Role role) {
        return new JPAQuery<User>(session)
                .select(user)
                .from(user)
                .where(user.role.eq(role))
                .fetch();
    }

}
