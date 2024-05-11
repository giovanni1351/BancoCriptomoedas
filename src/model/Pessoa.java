/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author unifgmorassi
 */
public class Pessoa {
    private String nome;
    private long CPF,senha;
    private ArrayList<Transacao> extrato;  
    

    public Pessoa(String nome, long senha, long CPF) {
        this.nome = nome;
        this.senha = senha;
        this.CPF = CPF;
        extrato =  new ArrayList<>();
        
    }

    public Pessoa(long CPF, long senha) {
        this.CPF = CPF;
        this.senha = senha;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public long getSenha() {
        return senha;
    }

    public void setSenha(long senha) {
        this.senha = senha;
    }

    public long getCPF() {
        return CPF;
    }

    public void setCPF(long CPF) {
        this.CPF = CPF;
    }

    public ArrayList<Transacao> getExtrato() {
        return extrato;
    }

    public void setExtrato(ArrayList<Transacao> extrato) {
        this.extrato = extrato;
    }
    
    public String printarInformacoes(){
        String infos = String.format("Nome: %s\nCPF: %d\n",nome,CPF);
        return infos;
    }
    
    
    @Override
    public String toString() {
        return "Pessoa{" + "nome=" + nome + ", CPF=" + CPF + ", senha=" + senha + ", extrato=" + extrato + '}';
    }
    
    

}
