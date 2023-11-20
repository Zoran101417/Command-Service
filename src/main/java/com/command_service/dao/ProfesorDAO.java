package com.command_service.dao;

import com.command_service.DBConnection.HibernateConfig;
import com.command_service.model.Faculty;
import com.command_service.model.Profesor;
import com.command_service.model.Subject;
import com.command_service.model.dto.ProfessorsDTO;
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
public class ProfesorDAO {

    @Autowired
    FacultyDAO facultyDAO;

    @Autowired
    SubjectDAO subjectDAO;

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    private final KafkaTemplate<String, Object> multiTypeKafkaTemplate;

    private static final Logger LOG = LogManager.getLogger(ProfesorDAO.class);

    public ProfesorDAO(KafkaTemplate<String, Object> multiTypeKafkaTemplate) {
        this.multiTypeKafkaTemplate = multiTypeKafkaTemplate;
    }

    public int add(Profesor profesor) {
        LOG.info("Add");
        int professor= 0;
        Session session = HibernateConfig.getSession();
        Transaction tr = session.beginTransaction();
        try{
            professor = (Integer) session.save(profesor);

            this.simpMessagingTemplate.convertAndSend("/topic/professor", professor);
            this.multiTypeKafkaTemplate.send("save", professor);

            tr.commit();
        } catch (Exception e) {
            LOG.error("Profesor add error: " + e.getMessage());
            tr.rollback();
            throw e;
        }
        return professor;
    }

    public int delete(int profesorID) {
        LOG.info("Delete");
        int professorID = 0;
        String hql = "Delete from Profesor where id= :profesorID";
        Session session = HibernateConfig.getSession();
        Transaction tr = session.beginTransaction();
        try {
            Query query = session.createQuery(hql);
            query.setParameter("profesorID", profesorID);
            professorID = query.executeUpdate();

            this.simpMessagingTemplate.convertAndSend("/topic/professor", professorID);

            tr.commit();
        }catch (Exception e) {
            LOG.error("Delete profesor error: " + e.getMessage());
            tr.rollback();
            throw e;
        }
        return professorID;
    }


    public Profesor getProfesorById(int profesorID) {
        LOG.info("getProfesorById");
        Profesor profesor = new Profesor();
        try {
            String hql = "select p from Profesor p where id = ?1";
            Session session = HibernateConfig.getSession();
            session.beginTransaction();
            Query query = session.createQuery(hql);
            query.setParameter(1, profesorID);

            List<Profesor> professorList = query.getResultList();

            if (!professorList.isEmpty()) {
                profesor = professorList.get(0);
            } else {
                throw new ObjectNotFoundException(profesorID, "Professor was not found with id: ");
            }
        } catch (Exception e) {
            LOG.error("getProfesorById error: " + e.getMessage());
            throw e;
        }
        return profesor;
    }


    public List<Profesor> getAllProfesors() {
        LOG.info("getAllProfesors");
        List<Profesor> profesorList = new ArrayList<>();
        try {
            String hql = "select p from Profesor p";
            Session session = HibernateConfig.getSession();
            session.beginTransaction();
            Query query = session.createQuery(hql);
            profesorList = query.getResultList();

        } catch (Exception e) {
            LOG.error("getAllProfesors error: " + e.getMessage());
            throw e;
        }
        return profesorList;
    }

    public int update(Profesor profesor) {
        LOG.info("Update");
        int updateRow = 0;
        String hql = "Update Profesor p set p.name = :name, p.lname = :lname, p.seq = :updateSeq where p.id = :profesorID and p.seq = :seq";
        Session session = HibernateConfig.getSession();
        Transaction tr = session.beginTransaction();
        try {
            Query query = session.createQuery(hql);
            query.setParameter("name", profesor.getName());
            query.setParameter("lname", profesor.getLname());
            query.setParameter("updateSeq", profesor.getSeq() + 1);
            query.setParameter("profesorID", profesor.getId());
            query.setParameter("seq", profesor.getSeq());
            updateRow = query.executeUpdate();

            this.simpMessagingTemplate.convertAndSend("/topic/professor", profesor);

            tr.commit();

            LOG.info("Success");
        }catch (Exception e) {
            LOG.error("Update professor error: " + e.getMessage());
            throw e;
        }
        return updateRow;
    }

    public List<Profesor> getAllProfesorsByFacultyID(int facultyID) {
        LOG.info("getAllProfesorsByFacultyID");
        List<Profesor> profesorList = new ArrayList<>();
        try {
            String hql = "select p from Profesor p where p.faculty.id = ?1";
            Session session = HibernateConfig.getSession();
            session.beginTransaction();
            Query query = session.createQuery(hql);
            query.setParameter(1,facultyID);
            profesorList = query.getResultList();

        } catch (Exception e) {
            LOG.error("getAllProfesorsByFacultyID error: " + e.getMessage());
            throw e;
        }
        return profesorList;
    }

    public ProfessorsDTO getFacultyAndSubjects() {
        ProfessorsDTO professorsDTO = new ProfessorsDTO();
        try {
            List<Faculty> facultyList = this.facultyDAO.getAllFaculties();
            List<Subject> subjectList = this.subjectDAO.getAllSubjects();

            professorsDTO.setFacultyList(facultyList);
            professorsDTO.setSubjectList(subjectList);
        } catch (Exception e) {
            LOG.error("getFacultyAndSubjects error: ", e.getMessage());
            throw e;
        }
        return professorsDTO;
    }

    public ProfessorsDTO getProfessorToEdit(int id) {
        ProfessorsDTO professorsDTO = new ProfessorsDTO();
        try {
            Profesor profesor = getProfesorById(id);
            List<Faculty> facultyList = this.facultyDAO.getAllFaculties();
            List<Subject> subjectList = this.subjectDAO.getAllSubjects();

            professorsDTO.setProfesor(profesor);
            professorsDTO.setFacultyList(facultyList);
            professorsDTO.setSubjectList(subjectList);
        } catch (Exception e){
            LOG.error("    public ProfessorsDTO getProfessorToEdit(int id) {\n error: ", e.getMessage());
            throw e;
        }
        return professorsDTO;

    }
}
