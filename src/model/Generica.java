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
        double porcento = Math.random();

        if(this.getCotacaoAtualParaReal()==0){
            this.setCotacaoAtualParaReal(100000*porcento); 
        };
        
        return this.getCotacaoAtualParaReal();
    
    };    
    
}
