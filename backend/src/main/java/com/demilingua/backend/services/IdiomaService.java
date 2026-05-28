package com.demilingua.backend.services;

import com.demilingua.backend.entities.Idioma;
import com.demilingua.backend.repositories.IdiomaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class IdiomaService {

    @Autowired
    private IdiomaRepository idiomaRepository;

    public List<Idioma> getAllIdiomas() {
        return idiomaRepository.findAll();
    }
}