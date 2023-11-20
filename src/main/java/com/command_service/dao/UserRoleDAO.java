package com.command_service.dao;

import com.command_service.DBConnection.HibernateConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

@Component
public class UserRoleDAO {
    private static final Logger LOG = LogManager.getLogger(UserRoleDAO.class);

    public int delete(int userID) {
        LOG.info("Delete user");
        int deleteRow = 0;
        try {
            String hql = "Delete from UserRole where userID= :userID";
            Session session = HibernateConfig.getSession();
            Transaction tr = session.beginTransaction();
            Query query = session.createQuery(hql);
            query.setParameter("userID", userID);
            deleteRow = query.executeUpdate();
            tr.commit();
        }catch (Exception e) {
            LOG.error("Delete user role error: " + e.getMessage());
            throw e;
        }
        return deleteRow;
    };
}
