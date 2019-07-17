package com.clientes.domain;

import com.clientes.domain.dto.ClienteDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

}
