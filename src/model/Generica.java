/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Givas
 */
public class Generica extends Moedas implements Tarifacao{
    private double taxaCompra,taxaVenda;
    public Generica(double quantidade) {
        super(quantidade);
    }

    public Generica(double quantidade, String nome) {
        super(quantidade, nome);
    }

    public Generica(double taxaCompra, double taxaVenda, String nome) {
        super(nome);
        this.taxaCompra = taxaCompra;
        this.taxaVenda = taxaVenda;
    }

    public void setTaxaCompra(double taxaCompra) {
        this.taxaCompra = taxaCompra;
    }

    public void setTaxaVenda(double taxaVenda) {
        this.taxaVenda = taxaVenda;
    }
    
    
    @Override
    public double tarifaCompra(){return taxaCompra;};
    @Override
    public double tarifaVenda(){ return taxaVenda;};
    @Override
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
    
}
