/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import DAO.Conexao;
import DAO.PessoaDAO;
import java.util.ArrayList;
import model.CriarPessoa;
import model.Investidor;
import model.Pessoa;
import view.LoginCadastro;
import view.Menu;
import java.sql.Connection;

/**
 *
 * @author unifgmorassi
 */
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
public class Controller {
    private final LoginCadastro loginCadastro;
    private final Menu menu = new Menu();
    private Pessoa userAtual;
    public static ArrayList<Pessoa> users = new ArrayList<>();
    
    public void logarAbrirMenu(){
        try{
        String cpf = loginCadastro.getTxtCpfLogin().getText();
        String senha = loginCadastro.getPfSenhaLogin().getText();
        long senhaLong = Long.parseLong(senha);
        long cpfLong = Long.parseLong(cpf);
        Pessoa procurado = CriarPessoa.procura(cpfLong);
        
        if(null != procurado){
            if(procurado.getSenha() == senhaLong){
                userAtual = procurado;
                configuraMenu();
                
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
            Conexao conexao = new Conexao();
            Connection conn =conexao.getConnection();
            PessoaDAO pessoaDAO = new PessoaDAO(conn);
            pessoaDAO.cadastrar((Pessoa)novo);
            userAtual = novo;
            configuraMenu();
        }catch(NumberFormatException e){
            loginCadastro.getTxtCpfCadastro().setText("Digite apenas numeros");
            loginCadastro.getLblAvisoErroSenha1().setText("Digite apenas numeros");
        } catch (SQLException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void contruirMenu(){
        String nome = userAtual.getNome();
        menu.getLblName().setText("Seja bem vindo " + nome);
        menu.getTxtInformacoesUsuario().setText(userAtual.printarInformacoes());
        
        
        
        
    }
    private void configuraMenu(){
        menu.setC(this);
        menu.construir();
        menu.setVisible(true);
        loginCadastro.setVisible(false);
    }
    public Controller(LoginCadastro loginCadastro) {
        this.loginCadastro = loginCadastro;
    }
    public void verExtrato(){
        menu.getTxtPrintInfos().setVisible(true);
        menu.getScrollPrintInfos().setVisible(true);
    }


}
