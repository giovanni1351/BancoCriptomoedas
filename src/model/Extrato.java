/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.Date;

/**
 *
 * @author giova
 */
public class Extrato {
    private Long pessoaID;
    private Date data;
    private String operacao;
    private double valor;

    public Extrato(Long pessoaID, Date data, String operacao, double valor) {
        this.pessoaID = pessoaID;
        this.data = data;
        this.operacao = operacao;
        this.valor = valor;
    }
    
    public Long getPessoaID() {
        return pessoaID;
    }

    public void setPessoaID(Long pessoaID) {
        this.pessoaID = pessoaID;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getOperacao() {
        return operacao;
    }

    public void setOperacao(String operacao) {
        this.operacao = operacao;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }
    
    public String printar(){
        String texto = String.format("Data: %02d/%02d/%02d "
                + "realizou uma %s no valor de %.2f"
                ,data.getDay(),data.getMonth(),data.getYear(),operacao,valor);
        return texto;
    }
    
    
    @Override
    public String toString() {
        return "Extrato{" + "pessoaID=" + pessoaID + ", data=" + data + ", operacao=" + operacao + ", valor=" + valor + '}';
    }
    
    
}
