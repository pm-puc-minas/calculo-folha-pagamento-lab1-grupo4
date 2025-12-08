package com.trabalho.FolhaPag.modules.folha.repository;

import com.trabalho.FolhaPag.modules.folha.entity.FolhaPagamento;
import com.trabalho.FolhaPag.modules.funcionario.entity.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FolhaPagamentoRepository extends JpaRepository<FolhaPagamento, Long> {
    List<FolhaPagamento> findByFuncionario(Funcionario funcionario);
    
    // garante que o funcionário seja carregado junto com a folha para evitar proxies nulos em serialização
    @org.springframework.data.jpa.repository.Query("select f from FolhaPagamento f join fetch f.funcionario")
    List<FolhaPagamento> findAllWithFuncionario();
}
