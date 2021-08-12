package com.example.tads.eaj.ufrn.jogostab.models;


import com.example.tads.eaj.ufrn.jogostab.message.Mensagens;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.*;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class JogosTabuleiro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String nome;
    @Min(value=1, message = Mensagens.ERRO_QUANTIDADE_)
    Integer qtjodaores;
    String classificacao;
    @Min(value = 1, message = Mensagens.ERRO_PRECO_MINIMO)
    Float preco;
    String imagemUri;
    Date deleted = null;


}
