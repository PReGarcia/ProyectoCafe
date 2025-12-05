package utils;

import org.w3c.dom.Document;

public class Message {
    private String comandaId;
    private String correlationId;
    private int tamSecuencia;
    private int ordenSecuencia;
    private Document cuerpo;

    public Message(String cid, String corrid, int tam, int orden, Document c){
        comandaId = cid;
        correlationId = corrid;
        tamSecuencia = tam;
        ordenSecuencia = orden;
        cuerpo = c;
    }

    public String getComandaId() {
        return comandaId;
    }

    public int getTamSecuencia() {
        return tamSecuencia;
    }

    public void setTamSecuencia(int tamSecuencia) {
        this.tamSecuencia = tamSecuencia;
    }

    public int getOrdenSecuencia() {
        return ordenSecuencia;
    }

    public void setOrdenSecuencia(int ordenSecuencia) {
        this.ordenSecuencia = ordenSecuencia;
    }

    public void setComandaId(String comandaId) {
        this.comandaId = comandaId;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public Document getCuerpo() {
        return cuerpo;
    }

    public void setCuerpo(Document cuerpo) {
        this.cuerpo = cuerpo;
    }

    public Message clonar(){
        return new Message(comandaId, correlationId, tamSecuencia, ordenSecuencia, cuerpo);
    }

}