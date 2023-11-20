package com.command_service.dao;

import com.command_service.DBConnection.HibernateConfig;
import com.command_service.model.Faculty;
import com.command_service.model.Subject;
import com.command_service.model.dto.SubjectDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SubjectDAO {

    private static final Logger LOG = LogManager.getLogger(SubjectDAO.class);

    @Autowired
    FacultyDAO facultyDAO;

    public int add(Subject subject) {
        LOG.info("Add");
        int result= 0;

        try{
            Session session = HibernateConfig.getSession();
            Transaction tr = session.beginTransaction();
            result = (Integer) session.save(subject);

            tr.commit();
        } catch (Exception e) {
            LOG.error("Subject add error: " + e.getMessage());
            throw e;
        }
        return result;
    }

    public int delete(int subjectID) {
        LOG.info("Delete");
        int deleteRow = 0;

        try {
            String hql = "Delete from Subject where id= :subjectID";
            Session session = HibernateConfig.getSession();
            Transaction tr = session.beginTransaction();
            Query query = session.createQuery(hql);
            query.setParameter("subjectID", subjectID);
            deleteRow = query.executeUpdate();

            tr.commit();
        }catch (Exception e) {
            LOG.error("Delete subject error " + e.getMessage());
            throw e;
        }
        return deleteRow;
    }


    public int update(Subject subject) {
        int updateRow = 0;

        try {
            String hql = "Update Subject sbj set sbj.name = :name, sbj.seq = :updateSeq where sbj.id = :subjectID and sbj.seq = :seq";
            Session session = HibernateConfig.getSession();
            Transaction tr = session.beginTransaction();
            Query query = session.createQuery(hql);
            query.setParameter("name", subject.getName());
            query.setParameter("updateSeq", subject.getSeq() + 1);
            query.setParameter("subjectID", subject.getId());
            query.setParameter("seq", subject.getSeq());

            updateRow = query.executeUpdate();

            tr.commit();
            LOG.info("Success");
        }catch (Exception e) {
            LOG.error("update error " + e.getMessage());
            throw e;
        }
        return updateRow;
    }


    public Subject getSubjectById(int id) {
        LOG.info("getSubjectById");
        Subject subject = new Subject();
        try {
            String hql = "select sbj from Subject sbj where id = ?1";
            Session session = HibernateConfig.getSession();
            session.beginTransaction();
            Query query = session.createQuery(hql);
            query.setParameter(1, id);

            List<Subject> subjectList = query.getResultList();

            if (!subjectList.isEmpty()) {
                subject = subjectList.get(0);
            } else {
                throw new ObjectNotFoundException(id, "Subject was not found with id: ");
            }
        } catch (Exception e) {
            LOG.error("Subject getSubjectById error: " + e.getMessage());
            throw e;
        }
        return subject;
    }

    public List<Subject> getAllSubjects() {
        LOG.info("getAllSubjects");
        List<Subject> subjectList = new ArrayList<>();
        try {
            String hql = "select sbj from Subject sbj";
            Session session = HibernateConfig.getSession();
            session.beginTransaction();
            Query query = session.createQuery(hql);
            subjectList = query.getResultList();

        } catch (Exception e) {
            LOG.error("Subject getAllSubjects error: " + e.getMessage());
            throw e;
        }
        return subjectList;
    }

    public List<Subject> getAllSubjectsByFacultyID(int facultyID) {
        LOG.info("getAllSubjectsByFacultyID");
        List<Subject> subjectList = new ArrayList<>();
        try {
//            String hql = "select sbj from Subject sbj where sbj.faculty.id = ?1";
//            Session session = HibernateConfig.getSession();
//            session.beginTransaction();
//            Query query = session.createQuery(hql);
//            query.setParameter(1,facultyID);
//            subjectList = query.getResultList();

        } catch (Exception e) {
            LOG.error("Subject getAllSubjectsByFacultyID error: " + e.getMessage());
            throw e;
        }
        return subjectList;
    }

    public List<Subject> getAllSubjectsByStudentID(int studentID) {
        LOG.info("getAllSubjectsByStudentID");
        List<Subject> subjectList = new ArrayList<>();
        try {
            String hql = "select st from Student st where st.id = ?1";
            Session session = HibernateConfig.getSession();
            session.beginTransaction();
            Query query = session.createQuery(hql);
            query.setParameter(1,studentID);
            subjectList = query.getResultList();

        } catch (Exception e) {
            LOG.error("Subject getAllSubjectsByStudentID error: " + e.getMessage());
            throw e;
        }
        return subjectList;
    }

    public SubjectDTO getSubjectToEdit(int id) {
        SubjectDTO subjectDTO = new SubjectDTO();
        try {
            Subject subject = this.getSubjectById(id);
            List<Faculty> facultyList = facultyDAO.getAllFaculties();

            subjectDTO.setSubject(subject);
            subjectDTO.setFacultyList(facultyList);

        } catch (Exception e) {
            LOG.error("Subject getSubjectToEdit error: " + e.getMessage());
            throw e;
        }
        return subjectDTO;
    }
}
