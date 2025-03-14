/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author unifgmorassi
 */
public class Moedas implements Tarifacao{
    private double cotacaoAtualParaReal;
    private double quantidade;
    private String nome;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Moedas(double quantidade, String nome) {
        this.quantidade = quantidade;
        this.nome = nome;
    }

    public Moedas(String nome) {
        this.nome = nome;
    }
    
    
    
    public double getCotacaoAtualParaReal() {
        if(cotacaoAtualParaReal == 0){
            double porcento = Math.random();
            cotacaoAtualParaReal = (100000*porcento);            
        }
        return cotacaoAtualParaReal;
        
    }

    public void setCotacaoAtualParaReal(double cotacaoAtualParaReal) {
        this.cotacaoAtualParaReal = cotacaoAtualParaReal;
    }

    public double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(double quantidade) {
        this.quantidade = quantidade;
    }

    public Moedas(double quantidade) {
        this.quantidade = quantidade;
    }
    public double atualizaCotacao(){
        double zeroAum = Math.random();
        boolean positivoOuNao = (Math.random()>0.5)? true:false;
        double valor = zeroAum*5;
        valor/=100;
        double multiplicador = 1+(valor*((positivoOuNao)? -1:1));
        double valorAnterior = this.getCotacaoAtualParaReal();
        this.setCotacaoAtualParaReal(valorAnterior*multiplicador);
        return this.getCotacaoAtualParaReal();
    };
    @Override
    public String toString() {
        return "Moedas{" + "cotacaoAtualParaReal=" + cotacaoAtualParaReal + ", quantidade=" + quantidade + '}';
    }

    @Override
    public double tarifaCompra() {
        return 1;
    }

    @Override
    public double tarifaVenda() {
        return 1;
    }
    
  
    
}
