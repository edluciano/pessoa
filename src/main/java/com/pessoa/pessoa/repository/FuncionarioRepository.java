package com.pessoa.pessoa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.pessoa.pessoa.models.FuncionarioModel;
import org.springframework.data.domain.Pageable;

@Repository
public interface FuncionarioRepository extends JpaRepository<FuncionarioModel,Integer>{
	@Query("SELECT f FROM FuncionarioModel f WHERE MONTH(f.sdt_nascimento) = :mes")
    List<FuncionarioModel> findByMesNascimento(@Param("mes") int mes);

    @Query("SELECT f FROM FuncionarioModel f ORDER BY f.sdt_nascimento DESC")
    List<FuncionarioModel> findOldestEmployee(Pageable pageable);
}