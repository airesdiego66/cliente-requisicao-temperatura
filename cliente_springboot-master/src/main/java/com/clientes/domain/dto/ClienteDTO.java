package com.clientes.domain.dto;

import com.clientes.domain.Cliente;
import lombok.Data;
import org.modelmapper.ModelMapper;

@Data
public class ClienteDTO {
    private Long id;

    private String nome;
    private Integer idade;

    //get
    public Long getId() { return id; }
    public String getNome() { return nome; }
    public Integer getIdade() { return idade; }

    //set
    public void setId(Long id) {
        this.id = id;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public void setIdade(Integer idade) {
        this.idade = idade;
    }

    public static ClienteDTO create(Cliente cliente) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(cliente, ClienteDTO.class);
    }
}
