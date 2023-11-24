package com.pessoa.pessoa.dto;

import java.math.BigDecimal;
import java.time.LocalDate;



public record FuncionarioRecordDTO(Integer cod_funcionario, String vch_nome, LocalDate sdt_nascimento, BigDecimal mny_salario, String vch_funcao) {
    public String getVch_nome(){
        return this.vch_nome;
    }
    public LocalDate getSdt_nascimento(){
        return this.sdt_nascimento;
    }
    public BigDecimal getMny_salario(){
        return this.mny_salario;
    }
    public String getVch_funcao(){
        return this.vch_funcao;
    }
}