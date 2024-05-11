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
    private Date data;
    private String operacao;
    private double valor;
    private double taxa;
    private double saldo;
    private String moeda;

    public Extrato(Date data, String operacao, double valor, double taxa, double saldo, String moeda) {
        this.data = data;
        this.operacao = operacao;
        this.valor = valor;
        this.taxa = taxa;
        this.saldo = saldo;
        this.moeda = moeda;
    }

    public double getTaxa() {
        return taxa;
    }

    public void setTaxa(double taxa) {
        this.taxa = taxa;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public String getMoeda() {
        return moeda;
    }

    public void setMoeda(String moeda) {
        this.moeda = moeda;
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
        return "Extrato{" + "data=" + data + ", operacao=" + operacao + ", valor=" + valor + ", taxa=" + taxa + ", saldo=" + saldo + ", moeda=" + moeda + '}';
    }
    
    

}
