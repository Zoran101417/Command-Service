package com.command_service.dao;

import com.command_service.DBConnection.HibernateConfig;
import com.command_service.exception.CustomException;
import com.command_service.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserDAO {

    @Autowired
    UserRoleDAO userRoleDAO;

    private static final Logger LOG = LogManager.getLogger(UserDAO.class);

    public User add(User user) {
        LOG.info("Add user");
        Session session = HibernateConfig.getSession();
        Transaction tr = session.beginTransaction();
        session.save(user);
//        tr.commit();

        return user;
    }

    public User update(User user) {
        LOG.info("Update user");

        try {
            String hql = "Update User u set u.name = :name, u.lname = :lname," +
                    " u.seq = :updateSeq where u.id = :userID and u.seq = :seq";
            Session session = HibernateConfig.getSession();
            Transaction tr = session.beginTransaction();
            Query query = session.createQuery(hql);
            query.setParameter("name", user.getName());
            query.setParameter("lname", user.getLname());
            query.setParameter("updateSeq", user.getSeq() + 1);
            query.setParameter("userID", user.getId());
            query.setParameter("seq", user.getSeq());
            query.executeUpdate();

//            session.refresh(user);

            LOG.info("Success");
            return user;
        } catch (Exception e) {
            LOG.error("Update user error: " + e.getMessage());
            throw e;
        }
    }

    public void delete(int userID) {
        LOG.info("Delete user");

        try {
            String hql = "Delete from User where id= :userID";
            Session session = HibernateConfig.getSession();
            Transaction tr = session.beginTransaction();
            int deleteUserRole = userRoleDAO.delete(userID);

            Query query = session.createQuery(hql);
            query.setParameter("userID", userID);
            query.executeUpdate();

            tr.commit();
        } catch (Exception e) {
            LOG.error("Delete user error: " + e.getMessage());
            throw e;
        }
    }

    public static User getUserById(int id) throws CustomException {
        LOG.info("getUserById");
        User user;
        try {
            String hql = "select u from User u where u.id = ?1";
            Session session = HibernateConfig.getSession();
            session.beginTransaction();
            Query query = session.createQuery(hql);
            query.setParameter(1, id);

            List<User> userList = query.getResultList();

            if (!userList.isEmpty()) {
                user = userList.get(0);
            } else {
                throw new CustomException("User was not found");
            }
        } catch (Exception e) {
            LOG.error("getUserById error" + e.getMessage());
            throw e;
        }
        return user;
    }

    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        try {
            String hql = "Select u from User u";
            Session session = HibernateConfig.getSession();
            session.beginTransaction();
            Query query = session.createQuery(hql);
            userList = query.getResultList();

        } catch (Exception e) {
            LOG.error("getAllUsers error: " + e.getMessage());
            throw e;
        }
        return userList;
    }

    public static User getUserByNameAndPassword(String username, String password) {
        LOG.info("getUserByNameAndPassword");
        User user = null;
        try {
            String hql = "select u from User u where u.username = :username and u.password = :password";
            Session session = HibernateConfig.getSession();
            session.beginTransaction();
            Query query = session.createQuery(hql);
            query.setParameter("username", username);
            query.setParameter("password", password);

            List<User> userList = query.getResultList();

            if (!userList.isEmpty()) {
                user = userList.get(0);
            }
        } catch (Exception e) {
            LOG.error("getUserByNameAndPassword error: " + e.getMessage());
            throw e;
        }
        return user;
    }

    public static User getUserByEmail(String email) {
        LOG.info("getUserByEmail");
        User user = null;
        try {
            String hql = "select u from User u where u.email = :email";
            Session session = HibernateConfig.getSession();
            session.beginTransaction();
            Query query = session.createQuery(hql);
            query.setParameter("email", email);
            List<User> userList = query.getResultList();

            if (!userList.isEmpty()) {
                user = userList.get(0);
            }
        } catch (Exception e) {
            LOG.error("getUserByEmail error: " + e.getMessage());
            throw e;
        }
        return user;
    }

    public static List<String> getUserPermissionsByEmail(String email) {
        LOG.info("getUserPermissionsByEmail");
        List<String> permissions = null;
        try {
            String hql = "select p.permissionName from Permission p \n" +
                    "inner join RolePermission rp on rp.permissionID = p.id \n" +
                    "inner join Role r on r.id = rp.roleID\n" +
                    "inner join UserRole ur on ur.roleID = r.id \n" +
                    "inner join User u on u.id = ur.userID \n" +
                    "where u.email = :email \n" +
                    "group by p.permissionName";
            Session session = HibernateConfig.getSession();
            session.beginTransaction();
            Query query = session.createQuery(hql);
            query.setParameter("email", email);
            permissions = query.getResultList();

        } catch (Exception e) {
            LOG.error("getUserPermissionsByEmail error:  " + e.getMessage());
            throw e;
        }
        return permissions;
    }

}
