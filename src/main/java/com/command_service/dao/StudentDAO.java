package com.command_service.dao;

import com.command_service.DBConnection.HibernateConfig;
import com.command_service.model.Faculty;
import com.command_service.model.Student;
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
public class StudentDAO {

    private static final Logger LOG = LogManager.getLogger(StudentDAO.class);

    public int add(Student student){
        LOG.info("Add Student");
        int result= 0;

        try{
            Session session = HibernateConfig.getSession();
            Transaction tr = session.beginTransaction();
            result = (Integer) session.save(student);

            tr.commit();
        } catch (Exception e) {
            LOG.error("Student add error: " + e.getMessage());
            throw e;
        }
        return result;
    }

    public int delete(int studentID){
        LOG.info("Delete");
        int deleteRow = 0;
        String hql = "Delete from Student where id= :studentID";
        Session session = HibernateConfig.getSession();
        Transaction tr = session.beginTransaction();

        try {
            Query query = session.createQuery(hql);
            query.setParameter("studentID", studentID);
            deleteRow = query.executeUpdate();

            tr.commit();
        }catch (Exception e) {
           LOG.error("Student delete error: " + e.getMessage());
           throw e;
        }
        return deleteRow;
    }

    public Student getStudentById(int id){
        LOG.info("getStudentById");
        Student student = new Student();
        try {
            String hql = "select std from Student std where id = ?1";
            Session session = HibernateConfig.getSession();
            session.beginTransaction();
            Query query = session.createQuery(hql);
            query.setParameter(1, id);

            List<Student> studentList = query.getResultList();

            if (!studentList.isEmpty()) {
                student = studentList.get(0);
            } else {
                throw new ObjectNotFoundException(id, "Student was not found with id: ");
            }
        } catch (Exception e) {
            LOG.error("Student getStudentById error: " + e.getMessage());
            throw e;
        }
        return student;
    }

    public List<Student> getAllStudents() {
        LOG.info("getAllStudents");
        List<Student> studentList;
        try {
            String hql = "select std from Student std";
            Session session = HibernateConfig.getSession();
            session.beginTransaction();
            Query query = session.createQuery(hql);
            studentList = query.getResultList();

            return studentList;

        } catch (Exception e) {
            LOG.error("getAllStudents error: " + e.getMessage());
            throw e;
        }
    }

    public int update(Student student) {
        LOG.info("Update");
        int result = 0;

        try {
            String hql = "Update Student std set std.name = :name, std.lname = :lname, std.years = :years, std.seq = :updateSeq where std.id = :studentID and std.seq = :seq";
            Session session = HibernateConfig.getSession();
            Transaction tr = session.beginTransaction();

            Query query = session.createQuery(hql);
            query.setParameter("name", student.getName());
            query.setParameter("lname", student.getLname());
            query.setParameter("years", student.getYears());
            query.setParameter("updateSeq", student.getSeq() + 1);
            query.setParameter("studentID", student.getId());
            query.setParameter("seq", student.getSeq());
            result = query.executeUpdate();


            tr.commit();
            LOG.info("Success");
        }catch (Exception e) {
            LOG.error("Update student error: " + e.getMessage());
            throw e;
        }
        return result;
    }

    public List<Student> getAllStudentsByFacultyID(int facultyID) {
        LOG.info("getAllStudentsByFacultyID");
        List<Student> studentList = new ArrayList<>();
        try {
            String hql = "select st from Student st where st.faculty.id =?1";
            Session session = HibernateConfig.getSession();
            session.beginTransaction();
            Query query = session.createQuery(hql);
            query.setParameter(1, facultyID);
            studentList = query.getResultList();

            Faculty faculty = studentList.get(0).getFaculty();

            faculty.getName();
        } catch (Exception e) {
            LOG.error("getAllStudentsByFacultyID error: " + e.getMessage());
            throw e;
        }
        return studentList;
    }

    public void calculateAvgStudentAgeByFaculty() {
        LOG.info("CalculateAvgStudentAgeByFaculty");
        try {
            String hql = "select avg(years),st.faculty from Student st GROUP BY st.faculty";
            Session session = HibernateConfig.getSession();
            session.beginTransaction();
            Query query = session.createQuery(hql);
            List studentList = query.getResultList();

            for (Object o : studentList) {
                Object[] rows = (Object[]) o;
                LOG.info(rows[0] + " -- " + rows[1]);
            }

            LOG.info(studentList);

        } catch (Exception e) {
            LOG.error("Student List error: " + e.getMessage());
            throw e;
        }
    }
}
