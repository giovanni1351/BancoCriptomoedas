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
    
    public double tarifaCompra(){return 0;};
    public double tarifaVenda(){ return 0;};
    public double atualizaCotacao(){ return 0;};    

}
