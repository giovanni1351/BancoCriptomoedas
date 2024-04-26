/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import static controller.Controller.users;

/**
 *
 * @author unifgmorassi
 */
public class CriarPessoa {
    public static Investidor criarInvestidor(String nome,String senha,Long CPF){
        Investidor novoInvestidor= new Investidor(nome,senha,CPF);
        return novoInvestidor;
    } 
    public static Pessoa procura(Long cpf){
        for(Pessoa atual: users){
            if(atual.getCPF()==cpf){
                return atual;
            }
        }
        return null;
    }
}
