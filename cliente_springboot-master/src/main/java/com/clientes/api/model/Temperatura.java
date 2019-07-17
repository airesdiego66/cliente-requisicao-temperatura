package com.clientes.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Temperatura {

    private String min_temp;
    private String max_temp;
    @JsonIgnore
    private Date data_criacao;

    public String getMin_temp() {
        return min_temp;
    }
    public void setMin_temp(String min_temp) {
        this.min_temp = min_temp;
    }

    public String getMax_temp() {
        return max_temp;
    }
    public void setMax_temp(String max_temp) {
        this.max_temp = max_temp;
    }

    public Date getData_criacao() {
        return data_criacao;
    }
    public void setData_criacao(Date data_criacao) {
        this.data_criacao = data_criacao;
    }
}

