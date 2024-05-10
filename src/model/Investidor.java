/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author unifgmorassi
 */
public class  Investidor extends Pessoa {
    private Carteira carteira;
    
    public Investidor(String nome, long senha, long CPF) {
        super(nome, senha, CPF);
        carteira = new Carteira();
    }
    
}
