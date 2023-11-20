package com.command_service.dao;

import com.command_service.DBConnection.HibernateConfig;
import com.command_service.model.Faculty;
import com.command_service.model.University;
import com.command_service.model.dto.FacultyToEditDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class FacultyDAO {

    @Autowired
    UniversityDAO universityDAO;

    private static final Logger LOG = LogManager.getLogger(FacultyDAO.class);

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    private final KafkaTemplate<String, Object> multiTypeKafkaTemplate;

    public FacultyDAO(KafkaTemplate<String, Object> multiTypeKafkaTemplate) {
        this.multiTypeKafkaTemplate = multiTypeKafkaTemplate;
    }

    public int add(Faculty faculty) {
        LOG.info("Add");
        int result= 0;

        try{
            Session session = HibernateConfig.getSession();
            Transaction tr = session.beginTransaction();
            result = (Integer) session.save(faculty);

            tr.commit();
        } catch (Exception e) {
            LOG.error("Faculty add error: " + e.getMessage());
            throw e;
        }
        return result;
    }


    public int update(Faculty faculty) {
        LOG.info("Update");
        int updateRow = 0;
        try {
            String hql = "Update Faculty f set f.name = :name, f.seq = :updateSeq where f.id = :facultyID and f.seq = :seq";
            Session session = HibernateConfig.getSession();
            Transaction tr = session.beginTransaction();
            Query query = session.createQuery(hql);
            query.setParameter("name", faculty.getName());
            query.setParameter("updateSeq", faculty.getSeq() + 1);
            query.setParameter("facultyID", faculty.getId());
            query.setParameter("seq", faculty.getSeq());
            updateRow = query.executeUpdate();

            tr.commit();
            LOG.info("Success");
        }catch (Exception e) {
            LOG.error("Update faculty error: " + e.getMessage());
            throw e;
        }
        return updateRow;
    }

    public int delete(int facultyID) {
        LOG.info("Delete");
        int deletedRow = 0;

        try {
            String hql = "Delete from Faculty where id= :facultyID";
            Session session = HibernateConfig.getSession();
            Transaction tr = session.beginTransaction();
            Query query = session.createQuery(hql);
            query.setParameter("facultyID", facultyID);
            deletedRow = query.executeUpdate();

            tr.commit();
        }catch (Exception e) {
            LOG.error("Delete faculty error: " + e.getMessage());
            throw e;
        }
        return deletedRow;
    }

    public Faculty getFacultyById(int facultyID) {
        LOG.info("getFacultyById");
        Faculty faculty = new Faculty();
        try {
            String hql = "select f from Faculty f where id = ?1";
            Session session = HibernateConfig.getSession();
            session.beginTransaction();
            Query query = session.createQuery(hql);
            query.setParameter(1, facultyID);

            List<Faculty> facultyList = query.getResultList();

            if (!facultyList.isEmpty()) {
                faculty = facultyList.get(0);
            } else {
                throw new ObjectNotFoundException(facultyID, "The faculty was not found with id: ");
            }
        } catch (Exception e) {
            LOG.error("getFacultyById error: " + e.getMessage());
            throw e;
        }
        return faculty;
    }

    public List<Faculty> getAllFaculties() {
        LOG.info("getAllFaculties");
        List<Faculty> facultyList = new ArrayList<>();
        try {
            String hql = "select f from Faculty f";
            Session session = HibernateConfig.getSession();
            session.beginTransaction();
            Query query = session.createQuery(hql);
            facultyList = query.getResultList();

        } catch (Exception e) {
            LOG.error("getAllFaculties error: " + e.getMessage());
            throw e;
        }
        return facultyList;
    }

    public List<Faculty> getAllFacultiesByUniversityId(int universityID) {
        LOG.info("getAllFacultiesByUniversityId");
        List<Faculty> facultyList = new ArrayList<>();
        try {
            String hql = "select f from Faculty f where f.university.id =?1";
            Session session = HibernateConfig.getSession();
            session.beginTransaction();
            Query query = session.createQuery(hql);
            query.setParameter(1, universityID);
            facultyList = query.getResultList();

        }catch (Exception e) {
            LOG.error("getAllFacultiesByUniversityId error: " + e.getMessage());
            throw e;
        }
        return facultyList;
    }

    public FacultyToEditDTO getFacultyToEdit(int id) {
        FacultyToEditDTO facultyToEditDTO = new FacultyToEditDTO();

        try {

            Faculty faculty = this.getFacultyById(id);
            List<University> universities = universityDAO.getAllUniversity();

            facultyToEditDTO.setFaculty(faculty);
            facultyToEditDTO.setUniversities(universities);

        } catch (Exception e) {
            LOG.error("Faculty To edit error: " + e.getMessage());
            throw e;
        }

        return facultyToEditDTO;
    }
}
