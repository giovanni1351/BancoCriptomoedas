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
import model.Generica;
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
   
    
    public Controller(LoginCadastro loginCadastro) throws SQLException {
        //contrutor da classe, onde recebe como parametro uma pagina de login
        //essa classe e chamada na main
        
        this.loginCadastro = loginCadastro;// atribuindo a variavel do controler com o parametro recebido
        Conexao conexao = new Conexao();//criando o objeto para a criacao da conexao com o banco de dados
        connection = conexao.getConnection(); // usando o metodo getConnection da classe Conexao
        pessoaDAO = new PessoaDAO(connection);//pessoaDAO sendo instanciada com a conexao criada e estabelecida 
        //pelo objeto conexao
    }
    
    //Funcao para logar e carregar todas as informacoes necessarias para o funcionamento
    //do codigo.
    
    public void logarAbrirMenu(){
        //Ele pega o cpf e senha e converte para double e faz o tratamento de excecao
        //usando get e set 
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
            
            //aqui instanciamos um objeto do tipo pessoa para armazenar as informacoes
            
            userTentativa = new Pessoa(cpfLong,senhaLong);
            //essa funcao recebe um objeto pessoa para verificar se existe no banco de dados
            //caso exista ele vai criar o userAtual com o usuario encontrado e suas informacoes
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
        //nesta funcao ele vai pegar as informacoes das textFieds e converter para o tipo de 
        //dados corretos para o funcionamento correto dos objetos
        //ele vai usar a funcao do objeto pessoaDAO apra adicionar no banco de dados
        try{
            String nome = loginCadastro.getTxtNomeCadastro().getText();
            String cpf = loginCadastro.getTxtCpfCadastro().getText();
            String senha = loginCadastro.getPfSenhaCadastro().getText();
            long senhaLong = Long.parseLong(senha);
            long cpfLong = Long.parseLong(cpf);
            if(!pessoaDAO.procurarExistenciaPeloCPF(cpfLong)){
                Investidor novo = new Investidor(nome, senhaLong, cpfLong);
                //essa funcao usa como parametro um objeto Pessoa, com isso ele retorna
                //o id correspondente dessa pessoa, para ser encontrado no banco de dados 
                // com mais faacilidade
                //E com o id conseguimos usar as outras funcoes.
                long idNovo = pessoaDAO.cadastrar((Pessoa)novo);
                novo.setId(idNovo);
                System.out.println(idNovo);
                pessoaDAO.cadastrarCarteira(idNovo);
                userAtual = novo;
                //configuraMenu();
                JOptionPane.showMessageDialog(loginCadastro, "Conta cadastrada com sucesso");
            }else{
                JOptionPane.showMessageDialog(loginCadastro, "CPF ja cadastrado");
            }
        }catch(NumberFormatException e){
            loginCadastro.getTxtCpfCadastro().setText("Digite apenas numeros");
            loginCadastro.getLblAvisoErroSenha1().setText("Digite apenas numeros");
        } catch (SQLException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void contruirMenu(){
        //nesta funcao ele vai pegar o nome do usuario do objeto e colocar na label
        //do menu para mostrar o nome
        String nome = userAtual.getNome();
        menu.getLblName().setText("Seja bem vindo " + nome);
               
    }
    private void configuraMenu(){
        //nesta funcao ele vai pegar o menu e setar o controller dele sendo o este objeto
        menu.setC(this);
        //metodo construir do objeto menu da classe Menu, onde tem o objetivo de fazer a 
        //configuracao
        menu.construir();
        //deixa o menu visivel
        menu.setVisible(true);
        //deixa a pagina de login ou cadastro invisivel
        loginCadastro.setVisible(false);
    }
    private void configuraMenuADM(){
        //ele vai setar  o controller do objeto menuADM como sendo esse controller
        menuADM.setVisible(true);
        menuADM.setC(this);
        loginCadastro.setVisible(false);
        //e vai trocar a tela
        
    }

    public void verExtrato(){
        //se a funcao pedir senha retornar verdadeira ele entra no if e executa o codigo
        if(pedirSenha()){
            //ele vai carregar o extrato no atributo arraylist de extrato do controller
            try{
                extrato.clear();
                carregaExtrato(userAtual.getId());
            }catch(SQLException e){
                JOptionPane.showMessageDialog(menu, "Erro de sql: "+e);
            }
            System.out.println(extrato.size());

            //ele vai pegar a tabela do extato e vai atualizar o atributo model
            var tabela= menu.getTabelaExtrato().getModel();
            //este for vai ir de linha em linha colocando as informacoes armazenadas na arraylist extrato
            for(int x = 0;x < extrato.size();x++){
                //
                Extrato atual = extrato.get(x);
                tabela.setValueAt(atual.getData(), x, 0);
                tabela.setValueAt(atual.getOperacao(), x, 1);
                tabela.setValueAt(atual.getValor(), x, 2);
                tabela.setValueAt(atual.getTaxa(), x, 3);
                tabela.setValueAt(atual.getMoeda(), x, 4);
                tabela.setValueAt(atual.getSaldo(), x, 5);

            }
        }
    }
    public void carregaExtrato(long idUser) throws SQLException{
        // funcao que vai carregar o extrato do banco de dados para a arraylist
        //pega o result set
        ResultSet resExtrato = pessoaDAO.consultarTabelaExtrato(idUser);
        //le o result set e da add no extrato
        while(resExtrato.next()){
            String op = resExtrato.getString("operacao");
            Date data = resExtrato.getDate("Data");
            double valor = resExtrato.getDouble("valor");
            double taxa = resExtrato.getDouble("taxa");
            String moeda = resExtrato.getString("moeda");
            double saldo = resExtrato.getDouble("saldo");
            extrato.add(new Extrato(data,op,valor,taxa,saldo,moeda));
        }
        //printa a arraylist para debugar
        for(Extrato i: extrato){
            System.out.println(i.printar());
        }
    }
    public Carteira carregaCarteira(long idUser) throws SQLException{
        //funcao apra carregar a carteira do usuario quando for logar
        //ele vai pegar o resultset
        ResultSet resCarteira = pessoaDAO.consultarTabelaCarteira(idUser);
        double bitcoin =0 ,ripple =0 ,ethereum =0,real =0;

        //caso senha encontrado ele vai ler os valores da carteira 
        //neste if ele vai pegar as moedas que sao as padroes
        if(resCarteira.next()){
            bitcoin = resCarteira.getDouble("Bitcoin");
            ripple = resCarteira.getDouble("Ripple");
            ethereum = resCarteira.getDouble("Ethereum");
            real = resCarteira.getDouble("Reais");
        }
        //ele vai criar um objeto carteira e instanciar com os valores ja adquiridos 
        Carteira carteira = new Carteira(bitcoin,ripple,ethereum,real);
        //agora ele vai ler todas a moedas aque foram criadas pelo adm usando a aplicacao
        
        ResultSet gen = pessoaDAO.consultaMoedasGenericas();
        //contador para saber quantas moedas foram lidas
        int contador = 0;
        //ele vai fazer um looping while para pegar todos os resultados do result set
        while(gen.next()){
            //daqui pra baixo ele so vai ler o resultset dando gget nos valores da linha atual
            double taxaCompra = gen.getDouble(3);
            double taxaVenda = gen.getDouble(2);
            String nomeMoeda = gen.getString(1);
            double quantidadeDaMoeda = resCarteira.getDouble(nomeMoeda);
            Generica generic = new Generica(taxaCompra,taxaVenda,nomeMoeda);
            generic.setQuantidade(quantidadeDaMoeda);
            carteira.getGenericas().add(generic);
            System.out.print(taxaCompra+ " ");
            System.out.print(taxaVenda+ " ");
            System.out.println(nomeMoeda+ " ");
            menu.getComboBoxMoedas().addItem(nomeMoeda);
            this.quantidadeDeMoedas++;
            //a moeda vai ser adicionada na combobox e na arraylist de moedas genericas criada na carteira
            contador++;
        }
        
        return carteira;
    }
    public void calcularVenda(){
        //nesta funcao ele vai calcular o valor que ele vai receber 
        //ele vai ler os valores da caixa de texto e fazer a conversao
        //fazendo o tratamento de excessoes 
        
        String valor = menu.getTxtValorVenda().getText();
        //ele vai pegar o indice da moeda que foi selecionada
        //para sabermos qual moeda vai ser calculada
        int index = menu.getComboBoxMoedas().getSelectedIndex();  
        //variavel global para sabermos em outros metodos a moeda selecionada
        indiceMoedaVenda = index;
        //print para debug
        System.out.println(index);
        //pegando a moeda a partir do indice usando o metodo getItemAt
        String moeda = menu.getComboBoxMoedas().getItemAt(index);
        //print para debug
        System.out.println(moeda);
        //valor iniciado
        double valorDouble =0;
        try{
            
            valorDouble = Double.parseDouble(valor);
            fracaoDeVenda = valorDouble;
        }catch(NumberFormatException e){
            menu.getTxtValorVenda().setText("Digite apenas numeros");
        }
        //pegando a moeda selecionada da carteira usando o metodo getmoeda que recebe como parametro o indice
        Moedas moedaSelecionada = carteiraAtual.getMoeda(index);
        //calculo para saber o valor de venda
        double valorDeVenda =valorDouble*moedaSelecionada.getCotacaoAtualParaReal()
                *moedaSelecionada.tarifaVenda();
        //comando para mostrar na tela o valor em rais para comprar a quantidade desejadaa de moeda
        menu.getTxtDisplayValorVenda().setText(String.format("R$:%.2f",valorDeVenda));
        
    }
    public void calculaCompra(){
        //ele vai ler os valores da tela
        //e converter fazendo seu tratamento de excessao
        String valor = menu.getTxtValorCompra().getText();
        int index = menu.getComboBoxMoedas().getSelectedIndex();  
        indiceMoedaCompra = index;
        System.out.println(index);
        
        //pegando o nome a partir do indice selecionado da combobox
        String moeda = menu.getComboBoxMoedas().getItemAt(index);
        System.out.println(moeda);
        //valor iniciado
        double valorDouble =0;
        try{
            valorDouble = Double.parseDouble(valor);
            fracaoDeCompra = valorDouble;
        }catch(NumberFormatException e){
            menu.getTxtValorCompra().setText("Digite apenas numeros");
        }
        //pegando o objeto da moeda pela carteira usando o metodo getMoeda que passaos o indice como parametro
        Moedas moedaSelecionada = carteiraAtual.getMoeda(index);
        //calculo de valor de compra
        double valorDeCompra =valorDouble*moedaSelecionada.getCotacaoAtualParaReal()
                *moedaSelecionada.tarifaCompra();
        //mostrando na tela o valor de compra
        menu.getLblDisplayValorCompra().setText(String.format("R$:%.2f",valorDeCompra));
        
    }
    public void venderMoedas(){
        //para vender moeda ele vai pedir a senha e caso o metodo retorne verdadeiro 
        //ele entra no controle de fluxo e os comando desejados sao executados
        if(pedirSenha()){
            //ele vai pegar o indice da combobox 
            int index = menu.getComboBoxMoedas().getSelectedIndex();  
            //se o indice slecionado for o mesmo que o calculado, ele vai seguir a logica
            if(index == indiceMoedaVenda && fracaoDeVenda !=0){
                //ele vai pegar os valores dos reais da carteira, e da fracao da moeda usando o get
                double reaisCarteira = carteiraAtual.getReal().getQuantidade();
                double fracaoMoeda = carteiraAtual.getMoeda(index).getQuantidade();
                //caso a fracao que ele possua seja maior que a que ele deseja vender
                //a logica continua
                if(fracaoMoeda>fracaoDeVenda){
                    //mensagem de confirmacao
                    int confirmou = JOptionPane.showConfirmDialog(menu, "Confimar-se gostaria realmente de vender");
                    //casos ele tenha confirmado o retorno sera 0 entao caso 
                    //confirmou seja 0 ele continua a trasacao
                    if(confirmou ==0){
                        //conta para somar o valor nos reais da carteira
                        reaisCarteira += (fracaoDeVenda*carteiraAtual.getMoeda(index).
                                getCotacaoAtualParaReal()*carteiraAtual.getMoeda(index)
                                        .tarifaVenda());
                        //conta para subtrair a fracao da carteira
                        fracaoMoeda-=fracaoDeVenda;
                        //ele vai setar a quantidade da fracao da moeda e reais nova
                        carteiraAtual.getMoeda(index).setQuantidade(fracaoMoeda);
                        carteiraAtual.getReal().setQuantidade(reaisCarteira);
                        System.out.println(carteiraAtual);
                        try{
                            //neste bloco try el vai fazer o salvamento da transacao no extrato
                            //e vai atualizar a tabela carteira usando os metodos do objeto
                            //da classe PessoaDAO
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
    }
    public void comprarMoedas(){
        // vai chamar a funcao pedirSenha que retorna true caso esteja correta
        if(pedirSenha()){
            //dentro deste controle de fluxo, a funcao vai pedar o indice que 
            //foi selecionado pela combobox
            int index = menu.getComboBoxMoedas().getSelectedIndex();  
            if(index == indiceMoedaCompra && fracaoDeCompra >=0){
                //depois de pegar o indice e verificar se é o mesmo que ja foi 
                //selecionado quando foi fazer o calculo, e a fracao de compra é positiva
                //ele vai pegar o objeto da moeda atual usando o metodo getMoeda que recebeo indice
                //e retona uma moeda da carteira 
                Moedas moedaAtual= carteiraAtual.getMoeda(index);
                //calcular valor
                double valorEmReais = fracaoDeCompra * moedaAtual.getCotacaoAtualParaReal();
                valorEmReais*=moedaAtual.tarifaCompra();
                //print para debug
                System.out.println("Valor da compra: "+valorEmReais);
                //pegando o valor atual de reais
                double reaisAtual = carteiraAtual.getReal().getQuantidade();
                double fracaoMoedaAtual = carteiraAtual.getMoeda(index).getQuantidade();
                //verifica se o valor em reais é menor que o reais atual
                if(valorEmReais< reaisAtual){
                    //casos seja
                    //mensagem para confirmação
                    int confirmou = JOptionPane.showConfirmDialog(menu, "Gostaria mesmo de comprar?");
                    if(confirmou ==0){
                        //fazer as conta para atualizar a carteira atual e com isso depois 
                        //atualizar usando os metodos do DAO que lancam para o banco de dadso
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
    }
    public void atualizaSaldoTela(){
        //nesta funcao vamo chamar primeiramente a funcao de printar na tela
        informacoesUsuario();
        //pega o indice da combo box
        int index = menu.getComboBoxMoedas().getSelectedIndex();
        //printa o indice da combo box
        System.out.println(index);
        //pega os objetos das labels e troca o text usando o setText para o valor e string desejado
        var lblNomeMoeda = menu.getLblNomeMoedaOlhada();
        var lblsaldo = menu.getLblSaldoAtual();
        var lblfracaoAtual = menu.getLblFracaoAtual();
        var lblcotacaoMoeda = menu.getLblPrecoUnidade();
        var lblsaldocripto = menu.getLblSaldoCripto();
        lblsaldo.setText(String.format("Saldo atual:%.2f",carteiraAtual.getReal().getQuantidade()));
        lblNomeMoeda.setText(menu.getComboBoxMoedas().getItemAt(index));
        Moedas moedaSelecionada = carteiraAtual.getMoeda(index);
        lblfracaoAtual.setText(String.format("Fração:%.10f",moedaSelecionada.getQuantidade()));
        lblcotacaoMoeda.setText(String.format("Preco unidade:%.2f",moedaSelecionada.getCotacaoAtualParaReal()));
        double valor = moedaSelecionada.getQuantidade()*moedaSelecionada.getCotacaoAtualParaReal();
        lblsaldocripto.setText(String.format("Saldo Cripto:%.2f",valor));
        

    }
    public void verificaSenhaEMostraInfo(){
        //pega o objeto do botao togle e usa o metodo para saber se esta ativo ou nao
        var botao = menu.getToggleMostraInfos().isSelected();
        //verifica se o botao está ativo ou n
        if(botao){
            //caso seja ativado ele vai pedir a senha e mostrará as infomações atualizadas
            String senha  = JOptionPane.showInputDialog(menu, "Digite sua senha");
            
            long senhaLong =0;
            try{
                senhaLong= Long.parseLong(senha);
            }
            catch(NumberFormatException e){

                JOptionPane.showMessageDialog(menu, "Digite apenas numeros");
            }
            //caso a senha estaja correta ele vai chamar o metodo que printa a informações na tela
            if(senhaLong == userAtual.getSenha()){
                informacoesUsuario();
            }
        }else{
            //
            informacoesUsuario();

        }
    }
    
    public void atualizaCotacao(){
        //primeira mente ele vai chamar a funcao de atualizar saldo na tela
        atualizaSaldoTela();
        //depois o texto que vai ser mostrado no menu
        String texto = "";
        //aqui ele vai percorrer todas as moedas e vai colocar na variavel texto
        
        for(int x = 0 ; x <quantidadeDeMoedas;x++){
            carteiraAtual.getMoeda(x).atualizaCotacao();
            String nome = carteiraAtual.getMoeda(x).getNome();
            Double cotacao = carteiraAtual.getMoeda(x).getCotacaoAtualParaReal();
            String atual = String.format("%s = %.2f \n",nome,cotacao);
            texto+=atual;
        }
        //ele vai setar na textArea
        menu.getTxtMostrarCotacaoMoedas().setText(texto);
    }
    public void depositarReais(){
        //funcao para depositar Reais, vai pedir senha... ver se é a correta
        if(pedirSenha()){
            //vai pegar o valor que quer depositar
            String valorDeposito = menu.getTxtValorDeposito().getText();
            double valor = 0;
            try{
                valor = Double.parseDouble(valorDeposito);
            }catch(NumberFormatException e){
                menu.getTxtValorDeposito().setText("Digite apenas numeros");
            }
            //vai verificar esse valor depois de convertido para double
            if(valor>=0){
                //ele vai pegar os reais que ja tem na carteira
                double realAtual = carteiraAtual.getReal().getQuantidade();
                //vai somar com o valor que foi desejado no deposito
                realAtual +=valor;
                //vai setar na carteira
                carteiraAtual.getReal().setQuantidade(realAtual);
                try{
                    //vai chamar a funcao que vai lancar para o banco de dados
                    pessoaDAO.atualizarCarteira(userAtual.getId(), carteiraAtual);
                    //vai chamar o metodo que vai lançar essa transação no extrato
                    //para a tabela de extrato do banco de dados
                    pessoaDAO.addExtrato(userAtual.getId(), new Extrato(null,"Depositou",valor,0,realAtual,"Reais"));
                    JOptionPane.showMessageDialog(menu, "Depositou "+valor+" com sucesso!");
                }catch(SQLException e){
                    JOptionPane.showMessageDialog(menu,"Erro de SQL: "+e);
                }
                //depois ele chama a funcao de atualizar a cotacao na tela para ja 
                //mostrar o valor atualizado na tela
                atualizaCotacao();
            }else{
                JOptionPane.showMessageDialog(menu,"Digite um valor positivo safado");
            }
        }
        
    } 
    public void sacarReais(){
        //funcao para a implementacao do saque
        //ele vai pedir a senha, caso seja correta ele faz a operacao
        if(pedirSenha()){
            //ele vai pegar o valor do txt field
            String valorTexto = menu.getTxtValorParaSacar().getText();
            double valor = 0;
            try{
                valor = Double.parseDouble(valorTexto);
            }catch(NumberFormatException e){
                menu.getTxtValorParaSacar().setText("Digite apenas numeros");
            }
            double reaisAtual = carteiraAtual.getReal().getQuantidade();
            //se o valor é menor ou igual e o valor é possitivo, ele pode realizar a operacao
            if(valor<=reaisAtual && valor>=0&& valor <=1e20){
                reaisAtual-=valor;
                carteiraAtual.getReal().setQuantidade(reaisAtual);
                try{
                    //aqui ele salva tudo no banco de dados, salva a carteira, e depois 
                    //coloca o extrato
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
                
    }
    
    public void cadastrarInvestidorADM(){
        //aqui é a funcao de cadastrar um investidor pelo menu de adm
        //ele vai pegar as informações dos texts labels e usar a funcao do dao
        //ele vai criar um registro na tabela de usuarios e na tabela da carteira
        
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
            if(!pessoaDAO.procurarExistenciaPeloCPF(cpfLong)){
                long idNovo = pessoaDAO.cadastrar(new Investidor(nome,senhaLong,cpfLong));
                pessoaDAO.cadastrarCarteira(idNovo);
                JOptionPane.showMessageDialog(menuADM,"Usuario cadastrado");
            }else{
                JOptionPane.showMessageDialog(menuADM,"CPF ja existente");

            }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(menuADM,"Erro de sql: "+e);
        }       
    }
    public void deletarUsuario(){
        //ele vai pegar o cpf e dar um select na tabela, e depois deletar
        //o select na tabela é uma funcao que esta na DAO
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
                    if(pessoaDAO.deletarUsuarioCarteira(cpfLong)){
                        System.out.println("deletou carteira");
                        if(pessoaDAO.deletarUsuario(cpfLong)){
                            System.out.println("deletou");
                            JOptionPane.showMessageDialog(menuADM, "Conta deletada com sucesso");
                        }else{
                            JOptionPane.showMessageDialog(menuADM, "Não deletou conta, mas deletou carteira");
                        }
                    }else{
                            JOptionPane.showMessageDialog(menuADM, "erro ao deletar a conta");
                    }
                }
                else if (ehADM){
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
        //nesta funcao ele vai dar um select all em todos da tabela
        //usuario é o result set que vai ser retornado pela funcao DAO
        
        ResultSet usuarios;
        //aqui é o model da tabela para alterar no menu
        var tabela = menuADM.getTabelaUsuarios().getModel();
        List<Pessoa> listaUsuarios = new ArrayList<>();
        

        try{
            //aqui ele chama a funcao e atribui o retorno na variavel declarada acima
            usuarios = pessoaDAO.consultarListaDeUsuarios();
            //nesse while ele vai ler o resutset e colocar na arraylust
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
            //ele vai limpar a tabela
            for(int x = 0 ;x < 50;x++){
                tabela.setValueAt("",x,0);
                tabela.setValueAt("",x,1);
                tabela.setValueAt("",x,2);
                tabela.setValueAt("",x,3);

            }
            //ele vai escrever na tabela
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
    public void admMostraInfomarcoesUsuariosADM(){
        //nesta funcao ele vai ver o extrato e ver a carteira de um usuario aa
        //partir de um cpf que foi digitado pelo administrador para fazer a consulta
        
        //ele pega o model das tabelas e cria as arraylist e pega tbm o cpf digitado
        var tabelaCarteira = menuADM.getTabelaCarteiraConsulta().getModel();
        var tabelaExtrato = menuADM.getTabelaExtrato().getModel();
        String cpfString = menuADM.getTxtCPFConsultarExtrato().getText();
        ArrayList<Extrato> extratoAtual = new ArrayList<>();
        ArrayList<Moedas> carteiraAtual = new ArrayList<>();
        
        //converte para long o cpf
        long cpf =0;
        try{
            cpf= Long.parseLong(cpfString);
        }catch(NumberFormatException e ){
            menuADM.getTxtCPFConsultarExtrato().setText("Digite apenas numeros");
        }
        try{
            //procura o id com o cpf
            long idAchado = pessoaDAO.acharIDpeloCPF(cpf);
            //procura o extrato usando o id achado
            ResultSet resExtrato = pessoaDAO.consultarTabelaExtrato(idAchado);
            System.out.println("pegou o resultset ");
            //coloca o extrato na arraylist
            while(resExtrato.next()){
                var data = resExtrato.getDate(2);
                String op = resExtrato.getString(3);
                double valor = resExtrato.getDouble(4);
                double taxa = resExtrato.getDouble(5);
                String moeda = resExtrato.getString(6);
                double saldo = resExtrato.getDouble(7);
                extratoAtual.add(new Extrato(data,op,valor,taxa,saldo,moeda));
                System.out.println("adicionou na array list do extrato");
            }
            //limpa a tabela
            for(int x = 0 ; x < 90;x++){
                
                tabelaExtrato.setValueAt("", x, 0);
                tabelaExtrato.setValueAt("", x, 1);
                tabelaExtrato.setValueAt("", x, 2);
                tabelaExtrato.setValueAt("", x, 3);
                tabelaExtrato.setValueAt("" , x, 4);
                tabelaExtrato.setValueAt("", x, 5);
                
            }
            //preencje a tabela com o extrato
            for(int x = 0 ; x < extratoAtual.size();x++){
                Extrato a = extratoAtual.get(x);
                tabelaExtrato.setValueAt(a.getData(), x, 0);
                tabelaExtrato.setValueAt(a.getOperacao(), x, 1);
                tabelaExtrato.setValueAt(a.getValor(), x, 2);
                tabelaExtrato.setValueAt(a.getTaxa(), x, 3);
                tabelaExtrato.setValueAt(a.getMoeda(), x, 4);
                tabelaExtrato.setValueAt(a.getSaldo(), x, 5);
                
            }
            //pega a carteira usando o id
            ResultSet resCarteira = pessoaDAO.consultarTabelaCarteira(idAchado);
            if(resCarteira.next()){
                carteiraAtual.add(new Generica(resCarteira.getDouble(5),"Reais"));
                carteiraAtual.add(new Generica(resCarteira.getDouble(4),"Ethreum"));
                carteiraAtual.add(new Generica(resCarteira.getDouble(3),"Ripple"));
                carteiraAtual.add(new Generica(resCarteira.getDouble(2),"BitCoin"));
                ResultSet resMoedas = pessoaDAO.consultaMoedasGenericas();
                while(resMoedas.next()){
                    String nomeGenerica = resMoedas.getString(1);
                    double valor = resCarteira.getDouble(nomeGenerica);
                    carteiraAtual.add(new Generica(valor ,nomeGenerica));
                }
            }
            //limpa a tabela 
            for(int x = 0 ; x < 90;x++){
                tabelaCarteira.setValueAt("", x, 0);
                tabelaCarteira.setValueAt("", x, 1);
            }
            //printa na tabela
            for(int x = 0 ; x < carteiraAtual.size();x++){
                var atual = carteiraAtual.get(x);
                tabelaCarteira.setValueAt(atual.getNome(), x, 0);
                tabelaCarteira.setValueAt(atual.getQuantidade(), x, 1);
            }
            
            
            
        }catch(SQLException e){
            System.out.println("erro de sql" + e);
        }
    }
    public void adicionarMoedasADM(){
        //funcao para adicionar moedas
        //primeiro ele vai pegar todas as infomaçoes 
        //depois ele vai convertar as taxas para double
        //e depois vai chamar um metodo do DAO para adicionar na tabela
        String nomeCrip = menuADM.getTxtNomeCripto().getText();
        String taxaCompraStr = menuADM.getTxtTaxaCompra().getText();
        String taxaVendaStr = menuADM.getTxtTaxaVenda().getText();
        double taxaCompra =0 ,taxaVenda =0;
        try{
            taxaCompra = Double.parseDouble(taxaCompraStr);
        }catch(NumberFormatException e){
            menuADM.getTxtTaxaCompra().setText("Digite apenas numeros");
        }
        try{
            taxaVenda = Double.parseDouble(taxaVendaStr);
        }catch(NumberFormatException e){
            menuADM.getTxtTaxaVenda().setText("Digite apenas numeros");
        }
        Generica nova = new Generica(taxaCompra,taxaVenda,nomeCrip );
        try{
            pessoaDAO.addMoedaNaTabela(nova);
            JOptionPane.showMessageDialog(menuADM, "Moeda adicionada com sucesso");
        }catch(SQLException e){
            JOptionPane.showMessageDialog(menuADM, "Error: "+e);
        }
        
    }
    public void deletarMoedaADM(){
        //para deletar ele vai chamar um metodo do DAO que vai deletar usando uma
        //condição where para dizer que é o nome tal que vai ser deletado
        String nomeMoeda = menuADM.getTxtNomeDeletarMoeda().getText();
        try{
            pessoaDAO.removeMoedaDaTabela(nomeMoeda);
            JOptionPane.showMessageDialog(menuADM, "Moeda deletada com sucesso!");
        }catch(SQLException e){
            JOptionPane.showMessageDialog(menuADM, "Erro: "+  e);
        }
    }
    
    public void informacoesUsuario(){
        //ele vai pegar a tabela
        //e depois o botao e ver se esta selecionado
        var tabelaCarteira = menu.getTabelaCarteiraConsulta().getModel();
        var botao = menu.getToggleMostraInfos().isSelected();
        //limpando a tabela
        var valorReal = menu.getLblValorReais();
        valorReal.setText(String.format("Reais : %.2f", carteiraAtual.getReal().getQuantidade()));
        for(int x = 0 ; x < 90;x++){
            tabelaCarteira.setValueAt("", x, 0);
            tabelaCarteira.setValueAt("", x, 1);
            tabelaCarteira.setValueAt("", x, 2);
        }
        if(botao){
            //se o botao estiver ativo ele vai digitar as informaçoes
            for(int x = 0 ; x <quantidadeDeMoedas;x++){
                var atual = carteiraAtual.getMoeda(x);
                tabelaCarteira.setValueAt(atual.getNome(), x, 0);
                tabelaCarteira.setValueAt(atual.getQuantidade(), x, 1);
                tabelaCarteira.setValueAt(atual.getCotacaoAtualParaReal()*atual.getQuantidade(), x, 2);


            }
        }
    }
    
    public boolean pedirSenha(){
        //funcao para pedir a senha, ela vai retornar true caso esteja correta
        //primeiro ele vai usar um JOptionPane para pegar o input da senha
        //ele vai converter para long, e verificar com a senha do usuario atual
        String senhaPedida = JOptionPane.showInputDialog("Digite sua senha");
        long senha =0;
        try{
            senha = Long.parseLong(senhaPedida);
        }catch(Exception e){
            JOptionPane.showMessageDialog(menu, "Digite apenas numeros");
        }
        if(senha == userAtual.getSenha()){
            return true;
        }else{
            JOptionPane.showMessageDialog(menu, "Senha incorreta");
            //retorno falso para senha incorret
            return false;
        }
    }
}

