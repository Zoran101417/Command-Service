package com.command_service.dao;

import com.command_service.DBConnection.HibernateConfig;
import com.command_service.model.University;
import com.command_service.model.dto.UniversityCustomDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UniversityDAO {

    private static final Logger LOG = LogManager.getLogger(UniversityDAO.class);

    public List<University> getAllUniversity() {
        LOG.info("UniversityDAO");
        List<University> universityList = new ArrayList<>();
        try {
            String hql = "select u from University u";
            Session session = HibernateConfig.getSession();
            session.beginTransaction();
            Query query = session.createQuery(hql);
            universityList = query.getResultList();
        } catch (Exception e) {
            LOG.error("getAllUniversity error: " + e.getMessage());
            throw e;
        }
        return universityList;
    }

    public University findById(int universityID){
        LOG.info("FindById");
        University university = new University();
        try {
            String hql = "select u from University u where id = ?1";
            Session session = HibernateConfig.getSession();
            session.beginTransaction();
            Query query = session.createQuery(hql);
            query.setParameter(1, universityID);

            List<University> universityList = query.getResultList();

            if (!universityList.isEmpty()) {
                university = universityList.get(0);
            } else {
                throw new ObjectNotFoundException(universityID, "University not found with id: ");
            }
        } catch (Exception e) {
            LOG.error("findById error: " + e.getMessage());
            throw e;
        }
        return university;
    }

    public int add(University university) {
        LOG.info("Add");
        int saveUniversity = 0;

        try {
            Session session = HibernateConfig.getSession();
            Transaction tr = session.beginTransaction();
            saveUniversity = (Integer) session.save(university);
            tr.commit();
            LOG.info("Success");
        } catch (Exception e) {
            LOG.error("Add university error: " + e.getMessage());
            throw e;
        }
        return saveUniversity;
    }

    public int delete(int universityID) {
        LOG.info("Delete University");
        int deleteRow = 0;

        try {
            String hql = "Delete from University where id= :universityID";
            Session session = HibernateConfig.getSession();
            Transaction tr = session.beginTransaction();
            Query query = session.createQuery(hql);
            query.setParameter("universityID", universityID);
            deleteRow = query.executeUpdate();

            tr.commit();
            LOG.info("Success");
        }catch (Exception e) {
           LOG.error("Delete error: " + e.getMessage());
           throw e;
        }
        return deleteRow;
    }

    public int update(University university) {
        LOG.info("Update university");
        int rowUpdate = 0;

        try {
            String hql = "Update University u set u.name = :name, u.location = :location, u.seq = :updateSeq where u.id = :universityID and u.seq = :seq";
            Session session = HibernateConfig.getSession();
            Transaction tr = session.beginTransaction();
            Query query = session.createQuery(hql);
            query.setParameter("name", university.getName());
            query.setParameter("location", university.getLocation());
            query.setParameter("updateSeq", university.getSeq() + 1);
            query.setParameter("universityID", university.getId());
            query.setParameter("seq", university.getSeq());
            rowUpdate = query.executeUpdate();

            tr.commit();
            LOG.info("Success");
        }catch (Exception e) {
            LOG.error("Update error: " + e.getMessage());
            throw e;
        }
        return rowUpdate;
    }

    //TODO:
    public String saveUniversityCustomDTO(UniversityCustomDTO universityCustomDTO) {
        String message = "Uspesno se zacuvani podatocite";
        try {
            int universityID = add(universityCustomDTO.getUniversity());

            if(universityID > 0) {
//                for(FacultyDTO f : universityCustomDTO.getFacultyList()) {
//                    f.getFaculty().setUniversity(universityCustomDTO.getUniversity());
//                    int facultyID = FacultyDAO.add(f.getFaculty());
//                    if(facultyID > 0) {
//                        for(Profesor profesor : f.getProfesorList()) {
//                            profesor.setFaculty(universityCustomDTO.getFacultyList());
//                            int profesorID = ProfesorDAO.add(profesor);
////                                if(profesorID <= 0) {
////                                    con.rollback();
////                                }
//                        }
//                    } else {
////                            con.rollback();
//                    }
//                }
            }
        } catch (Exception e) {
            message = "Neuspesno zacuvuvanje na univerzitetskite podatoci";
//            con.rollback();
        }
//        con.commit();
        LOG.info(message);
        return message;
    }
}
