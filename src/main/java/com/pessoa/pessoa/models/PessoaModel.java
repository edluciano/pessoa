package com.pessoa.pessoa.models;

import java.io.Serializable;
import java.time.LocalDate;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.persistence.MappedSuperclass;

@Data
@EqualsAndHashCode(callSuper=false)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "cod_tipo")
@MappedSuperclass
public class PessoaModel extends RepresentationModel<PessoaModel> implements Serializable{

    @Column(name = "vch_nome")
    private String vch_nome;

    @Column(name = "sdt_nascimento")
    @JsonFormat(pattern = "dd/MM/yyyy")
	private LocalDate  sdt_nascimento;
}