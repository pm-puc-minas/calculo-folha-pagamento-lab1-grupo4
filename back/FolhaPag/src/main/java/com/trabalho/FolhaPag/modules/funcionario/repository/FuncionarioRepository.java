package com.trabalho.FolhaPag.modules.funcionario.repository;

import com.trabalho.FolhaPag.modules.funcionario.entity.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// aqui cria os metodos de acesso a dados para a entidade Funcionario, não precisa
@Repository
public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {
    Funcionario findByMatricula(String matricula);
}
