/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.util.ArrayList;
import java.util.List;
import model.CriarPessoa;
import model.Investidor;
import model.Pessoa;
import view.Menu;
import view.LoginCadastro;
import view.Menu;

/**
 *
 * @author unifgmorassi
 */
public class Controller {
    private LoginCadastro loginCadastro;
    private Menu menu;
        
    public static List<Pessoa> users = new ArrayList<Pessoa>();
    
    public void logarAbrirMenu(){
        try{
        String cpf = loginCadastro.getTxtCpfLogin().getText();
        String senha = loginCadastro.getPfSenhaLogin().getText();
        long senhaLong = Long.parseLong(senha);
        long cpfLong = Long.parseLong(cpf);
        Pessoa procurado = CriarPessoa.procura(cpfLong);
        
        if(null != procurado){
            if(procurado.getSenha() == senhaLong){
                Menu funcoes = new Menu(procurado);
                funcoes.setVisible(true);
            }          
        }
        }catch(NumberFormatException e){
             loginCadastro.getTxtCpfLogin().setText("Digite apenas numeros");
             loginCadastro.getLblAvisoErroSenha().setText("Digite apenas numeros");
        }
       
    }
    public void cadastrarInvestidor(){
        try{
            String nome = loginCadastro.getTxtNomeCadastro().getText();
            String cpf = loginCadastro.getTxtCpfCadastro().getText();
            String senha = loginCadastro.getPfSenhaCadastro().getText();
            System.out.println(senha);
            long senhaLong = Long.parseLong(senha);
            long cpfLong = Long.parseLong(cpf);
            Investidor novo = CriarPessoa.criarInvestidor(nome, senhaLong, cpfLong);
            users.add(novo);
        }catch(NumberFormatException e){
            loginCadastro.getTxtCpfCadastro().setText("Digite apenas numeros");
            loginCadastro.getLblAvisoErroSenha1().setText("Digite apenas numeros");
        }
    }
    public void contruirMenu(){
        String nome = menu.getUser().getNome();
        menu.getLblName().setText("Nome : " + nome);
        
    }
    
    
    
    
    
    public Controller(LoginCadastro loginCadastro) {
        this.loginCadastro = loginCadastro;
    }

    public Controller(Menu menu) {
        this.menu = menu;
    }
    

}
