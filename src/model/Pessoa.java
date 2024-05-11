/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.ArrayList;

/**
 *
 * @author unifgmorassi
 */
public class Pessoa {
    private String nome;
    private long CPF,senha;
    private long id;

    public Pessoa(String nome, long senha, long CPF,long id) {
        this.nome = nome;
        this.senha = senha;
        this.CPF = CPF;
        this.id = id;

        
    }
    public Pessoa(String nome, long senha, long CPF) {
        this.nome = nome;
        this.senha = senha;
        this.CPF = CPF;
        
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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


    
    public String printarInformacoes(){
        String infos = String.format("Nome: %s\nCPF: %d\n",nome,CPF);
        return infos;
    }

    @Override
    public String toString() {
        return "Pessoa{" + "nome=" + nome + ", CPF=" + CPF + ", senha=" + senha + ", id=" + id + '}';
    }
    
    

    
    

}
