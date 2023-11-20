package com.command_service.dao;

import com.command_service.DBConnection.HibernateConfig;
import com.command_service.model.STDSubject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class STDSubjectDAO {

    private static final Logger LOG = LogManager.getLogger(STDSubjectDAO.class);

    public List<STDSubject> getAllSTDSubjects() {
        LOG.info("getAllSTDSubjects");
        List<STDSubject> stdSubjectList = new ArrayList<>();
        try {
            String hql = "select std from STDSubject std";
            Session session = HibernateConfig.getSession();
            session.beginTransaction();
            Query query = session.createQuery(hql);
            stdSubjectList = query.getResultList();

        } catch (Exception e) {
            LOG.error("getAllSTDSubjects error: " + e.getMessage());
            throw e;
        }
        return stdSubjectList;
    }
}
