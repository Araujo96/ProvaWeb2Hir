package com.example.tads.eaj.ufrn.jogostab.services;


import com.example.tads.eaj.ufrn.jogostab.models.JogosTabuleiro;
import com.example.tads.eaj.ufrn.jogostab.repository.JogoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Date;

@Service
public class JogoServices {

        JogoRepository repository;

    @Autowired
    public void setRepository(JogoRepository repository){
        this.repository = repository;
    }

    public List<JogosTabuleiro> findall(){
        return repository.findAll();
    }
    public void save(JogosTabuleiro jogosTabuleiro){
        repository.save(jogosTabuleiro);
    }
    public void delete(Long id){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        JogosTabuleiro jogosTabuleiro = new JogosTabuleiro();
        jogosTabuleiro = repository.getById(id);
        Date data  = new Date();
        jogosTabuleiro.setDeleted(data);
        repository.save(jogosTabuleiro);

    }


    public List<JogosTabuleiro> listAll(){
        return repository.findAllByDeletedIsNull();
    }

    public JogosTabuleiro findById(Long id){
        return repository.getById(id);
    }
    public void deletedtrue(Long id){
        repository.deleteById(id);
    }
}


