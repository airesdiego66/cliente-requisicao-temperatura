package com.clientes.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CoordernadasGeograficas {

    private String status;
    private CorpoCoordenadas corpo ;

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public CorpoCoordenadas getCorpo() {
        return corpo;
    }
    public void setCorpo(CorpoCoordenadas corpo) {
        this.corpo = corpo;
    }
}

