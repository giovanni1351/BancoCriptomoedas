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
        carteira = new Carteira(0,0,0,0);
    }
    @Override
    public String printarInformacoes(){
        String infos = String.format("Nome: %s\n"
                + "CPF: %d\n"
                + "Reais: %.2f\n"
                + "Bitcoin: %.2f\n"
                + "Ripple: %.2f\n"
                + "Ethereum: %.2f\n"
                ,this.getNome(),this.getCPF(),
                carteira.getReal().getCotacaoAtualParaReal(),
                carteira.getBitcoin().getCotacaoAtualParaReal(),
                carteira.getRipple().getCotacaoAtualParaReal(),
                carteira.getEth().getCotacaoAtualParaReal());
        return infos;
    
    }
}
