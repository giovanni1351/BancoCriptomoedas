/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author unifgmorassi
 */


public class Real extends Moedas implements Tarifacao{

    public Real(double quantidade) {
        super(quantidade);
    }
    
    @Override
    public double tarifaCompra(){return 1;};
    @Override
    public double tarifaVenda(){ return 1;};
    @Override
    public double atualizaCotacao(){ return 0;};    
}
