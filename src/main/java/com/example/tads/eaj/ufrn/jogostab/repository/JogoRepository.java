package com.example.tads.eaj.ufrn.jogostab.repository;

import com.example.tads.eaj.ufrn.jogostab.models.JogosTabuleiro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JogoRepository extends JpaRepository<JogosTabuleiro, Long> {

    List<JogosTabuleiro> findAllByDeletedIsNotNull();
    List<JogosTabuleiro> findAllByDeletedIsNull();

}
