/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author unifgmorassi
 */
public class Ethereum extends Moedas implements Tarifacao {

    public Ethereum(double quantidade) {
        super(quantidade);
    }

    @Override
    public double tarifaCompra(){return 1.01;};
    @Override
    public double tarifaVenda(){ return 0.98;};
    @Override
    public double atualizaCotacao(){
        double porcento = Math.random();
        this.setCotacaoAtualParaReal(100000*porcento);
        return this.getCotacaoAtualParaReal();
    
    };    

}
