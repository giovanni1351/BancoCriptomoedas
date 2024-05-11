    /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import DAO.Conexao;
import DAO.PessoaDAO;
import java.util.ArrayList;
import model.Investidor;
import model.Pessoa;
import view.LoginCadastro;
import view.Menu;
import java.sql.Connection;
import java.sql.ResultSet;

/**
 *
 * @author unifgmorassi
 */
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import model.Administrador;
import model.Carteira;
import model.Extrato;
import view.MenuADM;
public class Controller {
    private final LoginCadastro loginCadastro;
    private MenuADM menuADM = new MenuADM();
    private final Menu menu = new Menu();
    private Pessoa userAtual;
    private Carteira carteiraAtual;
    ArrayList<Extrato> extrato = new ArrayList<>();
    Pessoa userTentativa;
    Connection connection;
    PessoaDAO pessoaDAO;

    
    
    public void logarAbrirMenu(){

        String cpf = loginCadastro.getTxtCpfLogin().getText();
        String senha = loginCadastro.getPfSenhaLogin().getText();
        long senhaLong=0,cpfLong=0;

        try{
            try{
            senhaLong = Long.parseLong(senha);
            cpfLong = Long.parseLong(cpf);
            }
            catch(NumberFormatException e){
                loginCadastro.getTxtCpfLogin().setText("Digite apenas numeros");
                loginCadastro.getLblAvisoErroSenha().setText("Digite apenas numeros");
            }
            
            
            userTentativa = new Pessoa(cpfLong,senhaLong);
            ResultSet res = pessoaDAO.consultar(userTentativa);
            if(res.next()){
                JOptionPane.showMessageDialog(loginCadastro, "Login feito com sucesso ");
                String nomeUser = res.getString("Nome");
                Long senhaUser = res.getLong("Senha");
                Long cpfUser = res.getLong("CPF");
                Long idUser = res.getLong("PessoaID");
                boolean isAdm = res.getBoolean("IsADM");                
                carregaExtrato(idUser);
                
               
                if(isAdm){
                    userAtual = new Administrador(nomeUser, senhaUser, cpfUser);
                    configuraMenuADM();
                }else{  
                    
                    userAtual = new Investidor(nomeUser,senhaUser,cpfUser);
                    configuraMenu();
                }
            }

        }catch (SQLException ex) {
            JOptionPane.showMessageDialog(loginCadastro, "Problemas tecnicos "+ex);
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
            Investidor novo = new Investidor(nome, senhaLong, cpfLong);
            long idNovo = pessoaDAO.cadastrar((Pessoa)novo);
            //pessoaDAO.cadastrarCarteira(idNovo, 0, 0, 0, 0);
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
    private void configuraMenuADM(){
        menuADM.setVisible(true);
        menuADM.setC(this);
        loginCadastro.setVisible(false);

        
    }
    public Controller(LoginCadastro loginCadastro) throws SQLException {
        this.loginCadastro = loginCadastro;
        Conexao conexao = new Conexao();
        connection = conexao.getConnection();
        pessoaDAO = new PessoaDAO(connection);
    }
    public void verExtrato(){
        String textoExtrato ="" ;
        for(Extrato i : extrato){
            textoExtrato+=(i.printar()+"\n");
        }
        menu.getTxtPrintInfos().setText(textoExtrato);
    }
    public void carregaExtrato(long idUser) throws SQLException{
        ResultSet resExtrato = pessoaDAO.consultarTabelaExtrato(idUser);
        while(resExtrato.next()){
            String op = resExtrato.getString("operacao");
            Date data = resExtrato.getDate("Data");
            double valor = resExtrato.getDouble("valor");
            Long id = resExtrato.getLong("PessoaID");
            extrato.add(new Extrato(id,data,op,valor));

        }
        for(Extrato i: extrato){
            System.out.println(i.printar());
        }
    }
    public Carteira carregaCarteira(long idUser) throws SQLException{
        ResultSet resCarteira = pessoaDAO.consultarTabelaExtrato(idUser);
        double bitcoin =0 ,ripple =0 ,ethereum =0,real =0;
        bitcoin = resCarteira.getDouble("Bitcoin");
        ripple = resCarteira.getDouble("Ripple");
        ethereum = resCarteira.getDouble("Ethereum");
        real = resCarteira.getDouble("Reais");
        Carteira carteira = new Carteira(bitcoin,ripple,ethereum,real);
        return carteira;
    }
    public void addMoedaComboBox(){
        var comboBox = loginCadastro.getCombo();
        comboBox.addItem("Givas");
    }
}
