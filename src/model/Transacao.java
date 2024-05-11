/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author unifgmorassi
 */
public class Transacao {
    private String moedaInicial,moedaFinal;
    private double taxa,data,cpfDoUsuario,saldoMoedaInicial,saldoMoedaFinal;

    public Transacao() {
    }
    public Transacao(String moedaInicial, String moedaFinal,
            double taxa, double data, double cpfDoUsuario,
            double saldoMoedaInicial, double saldoMoedaFinal) 
    {
        this.moedaInicial = moedaInicial;
        this.moedaFinal = moedaFinal;
        this.taxa = taxa;
        this.data = data;
        this.cpfDoUsuario = cpfDoUsuario;
        this.saldoMoedaInicial = saldoMoedaInicial;
        this.saldoMoedaFinal = saldoMoedaFinal;
    }
    public void declarar(String moedaInicial, String moedaFinal,
            double taxa, double data, double cpfDoUsuario,
            double saldoMoedaInicial, double saldoMoedaFinal) 
    {
        this.moedaInicial = moedaInicial;
        this.moedaFinal = moedaFinal;
        this.taxa = taxa;
        this.data = data;
        this.cpfDoUsuario = cpfDoUsuario;
        this.saldoMoedaInicial = saldoMoedaInicial;
        this.saldoMoedaFinal = saldoMoedaFinal;
    }

    public String printa() {
        return "Troca da moeda " + moedaInicial + " para a moeda" +
                moedaFinal + " com a taxa de " + taxa + " na data " + data +
                " cpf: " + cpfDoUsuario + "\n Saldo da Moeda Inicial " +
                saldoMoedaInicial + "\n Saldo da Moeda Final=" +
                saldoMoedaFinal + '\n';
    }
    
    
}
