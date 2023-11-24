package com.pessoa.pessoa.models;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pessoa.pessoa.serials.SalarioSerializer;

@Data
@EqualsAndHashCode(callSuper=false)

@Table (name="funcionario")
@Entity
@DiscriminatorValue("1")
public class FuncionarioModel extends PessoaModel {

	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "cod_funcionario")
	private Integer cod_funcionario;

    @JsonSerialize(using = SalarioSerializer.class)
    @Column(name = "mny_salario")
    private BigDecimal mny_salario;
    
    @Column(name = "vch_funcao")
    private String vch_funcao;
}