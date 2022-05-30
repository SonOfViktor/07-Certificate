package com.epam.esm.dao.impl;

import com.epam.esm.dao.UserOrderDao;
import com.epam.esm.entity.UserOrder;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
public class UserOrderDaoImpl implements UserOrderDao {
    public static final String SELECT_ORDER_BY_USER_ID_HQL = """
            select o from UserOrder o
            join o.user u
            where u.userId = :id
            """;
    public static final String ID = "id";

    private final EntityManager entityManager;

    public UserOrderDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public UserOrder createOrder(UserOrder userOrder) {
        entityManager.persist(userOrder);

        return userOrder;
    }

    @Override
    public Optional<UserOrder> readOrder(int orderId) {

        return Optional.ofNullable(entityManager.find(UserOrder.class, orderId));
    }

    @Override
    public List<UserOrder> readUserOrder(int userId) {
        return entityManager.createQuery(SELECT_ORDER_BY_USER_ID_HQL, UserOrder.class)
                .setParameter(ID, userId)
                .getResultList();
    }
}
