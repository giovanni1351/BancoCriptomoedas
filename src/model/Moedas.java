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
    public double atualizaCotacao(){return 1.666;};
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
