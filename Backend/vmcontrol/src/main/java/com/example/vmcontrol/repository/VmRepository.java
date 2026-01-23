package com.example.vmcontrol.repository;

import com.example.vmcontrol.model.entity.Vm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface VmRepository extends JpaRepository<Vm, Long> {
    Optional<Vm> findByNome(String nome);
    boolean existsByNome(String nome);
    boolean existsByNomeAndIdNot(String nome, Long id);
}