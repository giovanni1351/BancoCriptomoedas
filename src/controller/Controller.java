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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import model.Administrador;
import model.Carteira;
import model.Extrato;
import model.Moedas;
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
    private double fracaoDeVenda =0;
    private double fracaoDeCompra =0;
    private int indiceMoedaVenda = 0;
    private int indiceMoedaCompra = 0;
    private int quantidadeDeMoedas = 3;

    
    
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
                    userAtual = new Administrador(nomeUser, senhaUser, cpfUser,idUser);
                    configuraMenuADM();
                }else{  
                    carteiraAtual = carregaCarteira(idUser);
                    userAtual = new Investidor(nomeUser,senhaUser,cpfUser,idUser,carteiraAtual);
                    System.out.println(userAtual);
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
            long senhaLong = Long.parseLong(senha);
            long cpfLong = Long.parseLong(cpf);
            Investidor novo = new Investidor(nome, senhaLong, cpfLong);
            long idNovo = pessoaDAO.cadastrar((Pessoa)novo);
            novo.setId(idNovo);
            System.out.println(idNovo);
            pessoaDAO.cadastrarCarteira(idNovo);
            userAtual = novo;
            //configuraMenu();
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
        
        try{
            extrato.clear();
            carregaExtrato(userAtual.getId());
        }catch(SQLException e){
            JOptionPane.showMessageDialog(menu, "Erro de sql: "+e);
        }
        System.out.println(extrato.size());


        var tabela= menu.getTabelaExtrato().getModel();
        for(int x = 0;x < extrato.size();x++){

            Extrato atual = extrato.get(x);
            tabela.setValueAt(atual.getData(), x, 0);
            tabela.setValueAt(atual.getOperacao(), x, 1);
            tabela.setValueAt(atual.getValor(), x, 2);
            tabela.setValueAt(atual.getTaxa(), x, 3);
            tabela.setValueAt(atual.getMoeda(), x, 4);
            tabela.setValueAt(atual.getSaldo(), x, 5);

        }
        //String textoExtrato ="" ;
        //for(Extrato i : extrato){
          //  textoExtrato+=(i.printar()+"\n");
        //}
        //menu.getTxtPrintInfos().setText(textoExtrato);
    }
    public void carregaExtrato(long idUser) throws SQLException{
        ResultSet resExtrato = pessoaDAO.consultarTabelaExtrato(idUser);
        while(resExtrato.next()){
            String op = resExtrato.getString("operacao");
            Date data = resExtrato.getDate("Data");
            double valor = resExtrato.getDouble("valor");
            double taxa = resExtrato.getDouble("taxa");
            String moeda = resExtrato.getString("moeda");
            double saldo = resExtrato.getDouble("saldo");
            extrato.add(new Extrato(data,op,valor,taxa,saldo,moeda));
        }
        for(Extrato i: extrato){
            System.out.println(i.printar());
        }
    }
    public Carteira carregaCarteira(long idUser) throws SQLException{
        ResultSet resCarteira = pessoaDAO.consultarTabelaCarteira(idUser);
        double bitcoin =0 ,ripple =0 ,ethereum =0,real =0;
        if(resCarteira.next()){
            bitcoin = resCarteira.getDouble("Bitcoin");
            ripple = resCarteira.getDouble("Ripple");
            ethereum = resCarteira.getDouble("Ethereum");
            real = resCarteira.getDouble("Reais");
        }
        Carteira carteira = new Carteira(bitcoin,ripple,ethereum,real);
        return carteira;
    }
    public void calcularVenda(){
        String valor = menu.getTxtValorVenda().getText();
        int index = menu.getComboBoxMoedas().getSelectedIndex();  
        indiceMoedaVenda = index;
        System.out.println(index);
        
        String moeda = menu.getComboBoxMoedas().getItemAt(index);
        System.out.println(moeda);
        double valorDouble =0;
        try{
            valorDouble = Double.parseDouble(valor);
            fracaoDeVenda = valorDouble;
        }catch(NumberFormatException e){
            menu.getTxtValorVenda().setText("Digite apenas numeros");
        }
        Moedas moedaSelecionada = carteiraAtual.getMoeda(index);
        
        double valorDeVenda =valorDouble*moedaSelecionada.getCotacaoAtualParaReal()
                *moedaSelecionada.tarifaVenda();
        menu.getTxtDisplayValorVenda().setText(String.format("R$:%.2f",valorDeVenda));
        
    }
    public void calculaCompra(){
        String valor = menu.getTxtValorCompra().getText();
        int index = menu.getComboBoxMoedas().getSelectedIndex();  
        indiceMoedaCompra = index;
        System.out.println(index);
        
        
        String moeda = menu.getComboBoxMoedas().getItemAt(index);
        System.out.println(moeda);
        
        double valorDouble =0;
        try{
            valorDouble = Double.parseDouble(valor);
            fracaoDeCompra = valorDouble;
        }catch(NumberFormatException e){
            menu.getTxtValorCompra().setText("Digite apenas numeros");
        }
        Moedas moedaSelecionada = carteiraAtual.getMoeda(index);
        
        double valorDeCompra =valorDouble*moedaSelecionada.getCotacaoAtualParaReal()
                *moedaSelecionada.tarifaCompra();
        menu.getLblDisplayValorCompra().setText(String.format("R$:%.2f",valorDeCompra));
        
    }
    public void venderMoedas(){
        int index = menu.getComboBoxMoedas().getSelectedIndex();  
        if(index == indiceMoedaVenda && fracaoDeVenda !=0){
            double reaisCarteira = carteiraAtual.getReal().getQuantidade();
            double fracaoMoeda = carteiraAtual.getMoeda(index).getQuantidade();
            if(fracaoMoeda>fracaoDeVenda){
                int confirmou = JOptionPane.showConfirmDialog(menu, "Confimar-se gostaria realmente de vender");
                if(confirmou ==0){
                    reaisCarteira += (fracaoDeVenda*carteiraAtual.getMoeda(index).
                            getCotacaoAtualParaReal()*carteiraAtual.getMoeda(index)
                                    .tarifaVenda());
                    fracaoMoeda-=fracaoDeVenda;
                    carteiraAtual.getMoeda(index).setQuantidade(fracaoMoeda);
                    carteiraAtual.getReal().setQuantidade(reaisCarteira);
                    System.out.println(carteiraAtual);
                    try{
                        pessoaDAO.addExtrato(userAtual.getId(), new Extrato(null,"vendeu",
                        fracaoDeVenda,carteiraAtual.getMoeda(index).tarifaVenda(),
                        reaisCarteira,menu.getComboBoxMoedas().getItemAt(index)));
                        pessoaDAO.atualizarCarteira(userAtual.getId(), carteiraAtual);
                    }catch(SQLException e){
                        JOptionPane.showMessageDialog(menu, "Erro de Sql"+e);

                    }
                    JOptionPane.showMessageDialog(menu, "Venda concluida");
                    atualizaSaldoTela();
                }
            }else{
                JOptionPane.showMessageDialog(menu, "Fração de venda maior que a possuida");
            }
        }else{
            calcularVenda();
        }
    }
    public void comprarMoedas(){
        int index = menu.getComboBoxMoedas().getSelectedIndex();  
        if(index == indiceMoedaCompra && fracaoDeCompra >0){
            Moedas moedaAtual= carteiraAtual.getMoeda(index);
            double valorEmReais = fracaoDeCompra * moedaAtual.getCotacaoAtualParaReal();
            valorEmReais*=moedaAtual.tarifaCompra();
            System.out.println("Valor da compra: "+valorEmReais);
            double reaisAtual = carteiraAtual.getReal().getQuantidade();
            double fracaoMoedaAtual = carteiraAtual.getMoeda(index).getQuantidade();
            
            if(valorEmReais< reaisAtual){
                int confirmou = JOptionPane.showConfirmDialog(menu, "Gostaria mesmo de comprar?");
                if(confirmou ==0){
                    carteiraAtual.getReal().setQuantidade(reaisAtual-valorEmReais);
                    carteiraAtual.getMoeda(index).setQuantidade(fracaoMoedaAtual+fracaoDeCompra);
                    try{
                        pessoaDAO.addExtrato(userAtual.getId(), new Extrato(null,
                                "Comprou",fracaoDeCompra,moedaAtual.tarifaCompra(),
                                reaisAtual,menu.getComboBoxMoedas().getItemAt(index)
                        ));
                        pessoaDAO.atualizarCarteira(userAtual.getId(), carteiraAtual);
                    }catch(SQLException e){
                        JOptionPane.showMessageDialog(menu, "Erro de Sql"+e);

                    }
                    JOptionPane.showMessageDialog(menu, "Compra concluida");
                    atualizaSaldoTela();

                }
            }else{
                JOptionPane.showMessageDialog(menu, "Saldo insuficiente!");
            }
        }
    }
    public void atualizaSaldoTela(){
        mostraInfos();
        int index = menu.getComboBoxMoedas().getSelectedIndex();
        System.out.println(index);
        var lblNomeMoeda = menu.getLblNomeMoedaOlhada();
        var lblsaldo = menu.getLblSaldoAtual();
        var lblfracaoAtual = menu.getLblFracaoAtual();
        var lblcotacaoMoeda = menu.getLblPrecoUnidade();
        var lblsaldocripto = menu.getLblSaldoCripto();
        lblsaldo.setText(String.format("Saldo atual:%.2f",carteiraAtual.getReal().getQuantidade()));
        lblNomeMoeda.setText(menu.getComboBoxMoedas().getItemAt(index));
        //Fração:
        //Preco unidade:
        Moedas moedaSelecionada = carteiraAtual.getMoeda(index);
        lblfracaoAtual.setText(String.format("Fração:%.10f",moedaSelecionada.getQuantidade()));
        lblcotacaoMoeda.setText(String.format("Preco unidade:%.2f",moedaSelecionada.getCotacaoAtualParaReal()));
        double valor = moedaSelecionada.getQuantidade()*moedaSelecionada.getCotacaoAtualParaReal();
        lblsaldocripto.setText(String.format("Saldo Cripto:%.2f",valor));

    }
    public void mostraInfos(){
        var botao = menu.getToggleMostraInfos();
        var nome = menu.getLblNomeInfos();
        var cpf = menu.getLblCPFInfos();
        var bitcoin = menu.getLblBitcoinInfos();
        var ethereum = menu.getLblEthereumInfos();
        var ripple = menu.getLblRippleInfos();
        var reais = menu.getLblReaisInfos();
        
        nome.setText("Nome: "+userAtual.getNome());
        if(botao.isSelected()){
            cpf.setText("CPF: "+userAtual.getCPF());
            bitcoin.setText("Saldo Bitcoin R$: "+carteiraAtual.getBitcoin().getCotacaoAtualParaReal()*carteiraAtual.getBitcoin().getQuantidade());
            ethereum.setText("Saldo Ethereum R$: "+carteiraAtual.getEth().getCotacaoAtualParaReal()*carteiraAtual.getEth().getQuantidade());
            ripple.setText("Saldo Ripple R$: "+carteiraAtual.getRipple().getCotacaoAtualParaReal()*carteiraAtual.getRipple().getQuantidade());
            reais.setText("Saldo R$: "+carteiraAtual.getReal().getQuantidade());

        }else{
            cpf.setText("CPF: XXX.XXX.XXX-XX");
            bitcoin.setText("Saldo Bitcoin R$: ?");
            ethereum.setText("Saldo Ethereum R$: ?");
            ripple.setText("Saldo Ripple R$: ?");
            reais.setText("Saldo R$: ?");
        }   
    }
    public void atualizaCotacao(){
        atualizaSaldoTela();
        String texto = "";
        for(int x = 0 ; x <quantidadeDeMoedas;x++){
            carteiraAtual.getMoeda(x).atualizaCotacao();
            String nome = carteiraAtual.getMoeda(x).getNome();
            Double cotacao = carteiraAtual.getMoeda(x).getCotacaoAtualParaReal();
            String atual = String.format("%s = %.2f \n",nome,cotacao);
            texto+=atual;
        }
        menu.getTxtMostrarCotacaoMoedas().setText(texto);
    }
    public void depositarReais(){
        String valorDeposito = menu.getTxtValorDeposito().getText();
        double valor = 0;
        try{
            valor = Double.parseDouble(valorDeposito);
        }catch(NumberFormatException e){
            menu.getTxtValorDeposito().setText("Digite apenas numeros");
        }
        if(valor>0){
            double realAtual = carteiraAtual.getReal().getQuantidade();
            realAtual +=valor;
            carteiraAtual.getReal().setQuantidade(realAtual);
            try{
                pessoaDAO.atualizarCarteira(userAtual.getId(), carteiraAtual);
                pessoaDAO.addExtrato(userAtual.getId(), new Extrato(null,"Depositou",valor,0,realAtual,"Reais"));
                JOptionPane.showMessageDialog(menu, "Depositou "+valor+" com sucesso!");
            }catch(SQLException e){
                JOptionPane.showMessageDialog(menu,"Erro de SQL: "+e);
            }
            
            atualizaCotacao();
        }else{
            JOptionPane.showMessageDialog(menu,"Digite um valor positivo safado");
        }
        
    } 
    public void sacarReais(){
        String valorTexto = menu.getTxtValorParaSacar().getText();
        double valor = 0;
        try{
            valor = Double.parseDouble(valorTexto);
        }catch(NumberFormatException e){
            menu.getTxtValorParaSacar().setText("Digite apenas numeros");
        }
        double reaisAtual = carteiraAtual.getReal().getQuantidade();
        
        if(valor<=reaisAtual && valor>=0&& valor <=1e20){
            reaisAtual-=valor;
            carteiraAtual.getReal().setQuantidade(reaisAtual);
            try{
                pessoaDAO.atualizarCarteira(userAtual.getId(), carteiraAtual);
                pessoaDAO.addExtrato(userAtual.getId(), new Extrato(null,"Sacou",valor,0,reaisAtual,"Reais"));
                JOptionPane.showMessageDialog(menu, "Sacou "+valor+" Com sucesso!");
            }catch(SQLException e){
                JOptionPane.showMessageDialog(menu, "Erro de sql: " + e);
            }
            atualizaCotacao();
        }else if(valor > reaisAtual){
            JOptionPane.showMessageDialog(menu,"Valor maior que o possuido!");
        }else if(valor<0){
            JOptionPane.showMessageDialog(menu, "Digite um valor positivo");
        }
                
    }
    
    public void cadastrarInvestidorADM(){
        String cpf = menuADM.getTxtCadastroCPF().getText();
        String senha =menuADM.getTxtCadastroSenha().getText();
        String nome = menuADM.getTxtCadastroNome().getText();
        long cpfLong =0 ,senhaLong =0 ;
        try{
           senhaLong = Long.parseLong(senha);
        }catch(NumberFormatException e){
           menuADM.getTxtCadastroSenha().setText("Digite apenas numeros");
        }
        try{
           cpfLong = Long.parseLong(cpf);
        }catch(NumberFormatException e){
           menuADM.getTxtCadastroCPF().setText("Digite apenas numeros");
        }
        try{
            long idNovo = pessoaDAO.cadastrar(new Investidor(nome,senhaLong,cpfLong));
            pessoaDAO.cadastrarCarteira(idNovo);
            JOptionPane.showMessageDialog(menuADM,"Usuario cadastrado");
        }catch(SQLException e){
            JOptionPane.showMessageDialog(menuADM,"Erro de sql: "+e);
        }       
    }
    public void deletarUsuario(){
        String cpf = menuADM.getTxtCPFDeletar().getText();
        long cpfLong =0;
        try{
           cpfLong = Long.parseLong(cpf);
        }catch(NumberFormatException e){
           menuADM.getTxtCadastroCPF().setText("Digite apenas numeros");
        }
        try{
            ResultSet pessoa = pessoaDAO.procurarPeloCPF(cpfLong);
            System.out.println("asdasdasasd");
            System.out.println(pessoa.rowInserted());
            if(pessoa.next()){
                System.out.println("foi");
                boolean ehADM = pessoa.getBoolean("isADM");
                if(!ehADM){
                    if(pessoaDAO.deletarUsuario(cpfLong)){
                        System.out.println("deletou");
                        JOptionPane.showMessageDialog(menuADM, "Conta deletada com sucesso");
                    }else{
                        JOptionPane.showMessageDialog(menuADM, "erro ao deletar a conta");
                    }
                }else if (ehADM){
                    JOptionPane.showMessageDialog(menuADM, "não é possivel deleterar uma conta de Administrador");
                }
            }else{
                JOptionPane.showMessageDialog(menuADM, "Conta não encontrada");
            }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(menuADM, "Erro de sql: "+e);
            System.out.println("n pssou pelo procurarpelocpf");
        }
    }
    public void mostrarTodosUsuarios(){
        
        ResultSet usuarios;
        var tabela = menuADM.getTabelaUsuarios().getModel();
        List<Pessoa> listaUsuarios = new ArrayList<>();
        

        try{
            usuarios = pessoaDAO.consultarListaDeUsuarios();
            while(usuarios.next()){
                long idAtualLista = usuarios.getLong(1);
                String nomeAtualLista = usuarios.getString(2);
                long cpfAtualLista = usuarios.getLong(3);
                long senhaAtualLista = usuarios.getLong(4);
                boolean isADM= usuarios.getBoolean(5);
                if(isADM){
                    listaUsuarios.add(new Administrador( nomeAtualLista, 
                            senhaAtualLista,  cpfAtualLista,  idAtualLista));
                }else{
                    listaUsuarios.add(new Investidor( nomeAtualLista, 
                            senhaAtualLista,  cpfAtualLista,  idAtualLista));
                }
                
            }
            for(int x = 0 ;x < 50;x++){
                tabela.setValueAt("",x,0);
                tabela.setValueAt("",x,1);
                tabela.setValueAt("",x,2);
                tabela.setValueAt("",x,3);

            }
            for(int x = 0 ;x < listaUsuarios.size();x++){
                var atual = listaUsuarios.get(x);
                tabela.setValueAt(atual.getId(),x,0);
                tabela.setValueAt(atual.getNome(),x,1);
                tabela.setValueAt(atual.getCPF(),x,2);
                tabela.setValueAt(atual.getSenha(),x,3);

            }
            
        }catch(SQLException e){
            JOptionPane.showMessageDialog(menuADM, "Erro:" + e);
        }

        
    }
    
    
    
    
}

