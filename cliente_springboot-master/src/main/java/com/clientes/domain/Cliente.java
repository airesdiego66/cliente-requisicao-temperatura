package com.clientes.domain;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@NoArgsConstructor
@Data
@Entity
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private Integer idade;
    private double temperaturaMax;
    private double temperaturaMin;
    private Date dataCriacao;

    //lombok não está funcionando
    //get
    public Long getId() { return id; }
    public String getNome() { return nome; }
    public Integer getIdade() { return idade; }
    public double getTemperaturaMax() { return temperaturaMax; }
    public double getTemperaturaMin() { return temperaturaMin; }
    public Date getDataCriacao() { return dataCriacao; }

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
    public void setTemperaturaMax(Double temperaturaMax) { this.temperaturaMax = temperaturaMax; }
    public void setTemperaturaMin(Double temperaturaMin) { this.temperaturaMin = temperaturaMin; }
    public void setDataCriacao(Date dataCriacao) { this.dataCriacao = dataCriacao; }
}

