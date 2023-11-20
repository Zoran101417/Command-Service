package com.command_service.service.impl;

import com.command_service.dao.STDSubjectDAO;
import com.command_service.service.STDSubjectService;
import org.springframework.stereotype.Service;

@Service
public class STDSubjectServiceImpl implements STDSubjectService {

    STDSubjectDAO stdSubjectDAO;

//    @Override
//    public int add(STDSubject subject) {
//        return stdSubjectDAO.add(subject);
//    }

//    @Override
//    public HashMap<Integer, StudentDTO> getAllSubjectsByStudent() {
//        return stdSubjectDAO.getAllSubjectsByStudent();
//    }

}
