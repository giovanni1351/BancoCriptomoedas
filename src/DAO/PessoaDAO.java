/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import model.Carteira;
import model.Extrato;
import model.Generica;
import model.Moedas;
import model.Pessoa;

/**
 *
 * @author unifgmorassi
 */
public class PessoaDAO {
    private final Connection conn;
    
    public PessoaDAO(Connection conn) {
        //contrutor que recebe um objeto connection
        this.conn = conn;
    }
    
    public long cadastrar(Pessoa pessoa) throws SQLException{
        //funcao para cadastrar uma pessoa, ela vai receber um objeto do tipo pessoa
        //que contem todas as informaçoes necessarias para substituir nas colunas do
        //bancod de dados, que para isso, é usado um metodo da classe PreparedStatement
        //que serve para substituir os valores nos comando 
        String sqlInsert = "INSERT INTO public.\"Pessoa\"(\"Nome\",\"CPF\",\"Senha\")"
                + "VALUES (?,?,?)";
        PreparedStatement state = conn.prepareStatement(sqlInsert);
        state.setString(1, pessoa.getNome());
        state.setLong(2, pessoa.getCPF());
        state.setLong(3, pessoa.getSenha());
        state.execute();
        
        String pegarIDsql = "SELECT \"PessoaID\" from public.\"Pessoa\" where"
                + " \"CPF\" = ? and \"Senha\" = ? ";
        state = conn.prepareStatement(pegarIDsql);
        state.setLong(1, pessoa.getCPF());
        state.setLong(2, pessoa.getSenha());
        ResultSet resultadoID = state.executeQuery(); // Executar a consulta
        long id = -1; // Inicializar o id
        if (resultadoID.next()) { // Mover para o primeiro resultado
            id = resultadoID.getLong("PessoaID"); // Obter o ID
        }
        return id;

    }
            
    public ResultSet consultar(Pessoa Usuario) throws SQLException{
        //metodo para retornar a tabela de consulta da tabela Pessoa    
        String sql = "select * from public.\"Pessoa\" where \"CPF\" = ? and \"Senha\" = ?";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setLong(1, Usuario.getCPF());
        statement.setLong(2, Usuario.getSenha());
        statement.execute();
        ResultSet resultado = statement.getResultSet();
        return resultado;
    }
        
    public ResultSet consultarTabelaExtrato(long id) throws SQLException{
//metodo para retornar a tabela de consulta da tabela extrato buscando pelo id
                

        String sql = "select * from \"Extrato\" where \"PessoaID\" = ? ORDER BY \"numeroRegistro\" DESC ";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setLong(1, id);
        statement.execute();
        ResultSet resultado = statement.getResultSet();
        return resultado;
    }
    public ResultSet consultarTabelaCarteira(long id) throws SQLException{
//metodo para retornar a tabela de consulta da tabela carteira buscando pelo id

        String sql = "select * from \"Carteira\" where \"PessoaID\" = ? ";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setLong(1, id);
        statement.execute();
        ResultSet resultado = statement.getResultSet();
        return resultado;
    }
    public void cadastrarCarteira(long id) throws SQLException{
//metodo para realizar um comando de insert into, ou seja, adicionar um registro na tabela
//aqui neste caso ele vai inserir uma carteira nova, onde só é passado o id como dados

        String sql = "INSERT INTO public.\"Carteira\"(\"PessoaID\")\n VALUES (?);";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setLong(1, id);
        statement.execute();
    }

    public void atualizarCarteira(long id,Carteira user) throws SQLException{
//funcao que recebe uma carteira e um id, que vai servir para atualizar todas as informaçoes
//ele vai usar um where para dizer qual registro ele vai atualizar usando o ID como referencia
//depois ele vai passar os parametros e vai aplicar o comando, nesta funcao como podemos ter 
//mas na carteira tem como ter mais que apenas as colunas das moedas normais, então para isso
//usando os metodos para pegas as moedas genéricas, colocamos a partir de um for
//mais colunas como argumento até terminar de criar o comando, depois é só executar
        String sql1 = "UPDATE public.\"Carteira\" SET ";
        String sql2 = """
               "Bitcoin" = ?,
               "Ripple" = ? ,
               "Ethereum"=? ,
               "Reais" = ? """;
        for(Moedas a: user.getGenericas()){
            sql2 += String.format(",\n\"%s\"=?", a.getNome());
        }
        System.out.println(user.getGenericas());
        String condicao = String.format("\n WHERE \"PessoaID\" = %d ",id);
        String sql = sql1+sql2+condicao;
        System.out.println(sql);
        PreparedStatement comando = conn.prepareStatement(sql);
        comando.setDouble(1,user.getBitcoin().getQuantidade());
        comando.setDouble(2,user.getRipple().getQuantidade());
        comando.setDouble(3,user.getEth().getQuantidade());
        comando.setDouble(4,user.getReal().getQuantidade());
        for(int x = 0 ;x < user.getGenericas().size();x++){
            System.out.println("Quantidade: "+user.getGenericas().get(x).getQuantidade());
            comando.setDouble(x+5, user.getGenericas().get(x).getQuantidade());
        }
        comando.execute();
        
        
    }
    public void addExtrato(long id,Extrato extrato) throws SQLException{
//funcao para adicionar o regsitro do extrato na tabela, é necessário receber o id
//para a identificar qual pessoa pessoa é pelo id
        String sql = """
                     INSERT INTO public."Extrato"(
                     \t"PessoaID", "Data", operacao, valor, taxa, moeda, saldo)
                     \tVALUES (?, CURRENT_DATE, ?, ?, ?, ?, ?);""";
        PreparedStatement comando = conn.prepareStatement(sql);
        comando.setLong(1,id);
        comando.setString(2,extrato.getOperacao());
        comando.setDouble(3,extrato.getValor());
        comando.setDouble(4,extrato.getTaxa());
        comando.setString(5,extrato.getMoeda());
        comando.setDouble(6,extrato.getSaldo());
        comando.execute();
    }
    public ResultSet consultarListaDeUsuarios() throws SQLException{
//metodo para retornar a tabela que representa a lista dos usuarios
        String sql="""
                   SELECT "PessoaID", "Nome", "CPF", "Senha", "IsADM"
                   	FROM public."Pessoa";
                   """;
        PreparedStatement comando = conn.prepareStatement(sql);
        comando.execute();
        return comando.getResultSet();
    }
    
    public ResultSet procurarPeloCPF(long cpf) throws SQLException{
//retorna o resultset que representa o registro da tabela pessoa procurado pelo cpf
        String sql ="""
                       SELECT "PessoaID", "Nome", "CPF", "Senha", "IsADM"
                        	FROM public."Pessoa"
                        	WHERE "CPF" = ?; 
                    
                    """;
        PreparedStatement comando = conn.prepareStatement(sql);
        comando.setLong(1, cpf);
        comando.execute();
        ResultSet resultado = comando.getResultSet();
        return resultado;
    }
    public boolean procurarExistenciaPeloCPF(long cpf) throws SQLException{
//aqui ele vai apenas retorna que existe esta pessoa ou não procurando na tabela
//pelo cpf
        String sql ="""
                       SELECT "PessoaID", "Nome", "CPF", "Senha", "IsADM"
                        	FROM public."Pessoa"
                        	WHERE "CPF" = ?; 
                    
                    """;
        PreparedStatement comando = conn.prepareStatement(sql);
        comando.setLong(1, cpf);
        comando.execute();
        ResultSet resultado = comando.getResultSet();
        return resultado.next();
    }
    public long acharIDpeloCPF(long CPF) throws SQLException{
//ele vai usar o cpf para achar o registro e vai pegar a coluna do id para retornar
//com isso usar esse id em outras funções
        String sql = """
                     SELECT "PessoaID"
                     FROM public."Pessoa"
                     WHERE "CPF" = ? ;
                     """;
        PreparedStatement comando = conn.prepareStatement(sql);
        comando.setLong(1, CPF);
        comando.execute();
        ResultSet res = comando.getResultSet();
        if(res.next()){

            System.out.println("achou o id : " +  res.getLong("PessoaID"));
            return res.getLong("PessoaID");
        }else{
            return 0;
        }
    }
    public boolean deletarUsuario(long cpf){
//funcao para deletar um usuario pelo cpf
        String sql = """
                     DELETE FROM public."Pessoa"
                     	WHERE "CPF" = ?;
                     """;
        try{
            PreparedStatement comando = conn.prepareStatement(sql);
            comando.setLong(1, cpf);
            comando.execute();
            return true;
        }catch(SQLException e){
            return false;
        }
    }
    public boolean deletarUsuarioCarteira(long cpf){
//deletar a carteira do usuario recebendo um cpf de parametro, para que possa 
//usar no where como filtro para teletar corretamente
        String sql = """
                     DELETE FROM public."Carteira"
                     	WHERE "PessoaID" = ?;
                     """;
        try{
            PreparedStatement comando = conn.prepareStatement(sql);
            long id = acharIDpeloCPF(cpf);
            System.out.println(id);
            if(id !=0){
                comando.setLong(1, id);
            
            comando.execute();
            return true;
            }else{
                return false;
            }
        }catch(SQLException e){
            return false;
        }
    }
    public void addMoedaNaTabela(Generica moeda) throws SQLException{
//funcao para dar un insterrt da moeda na tabela de moedas onde armazena os valores
//da taxa e o nome, e depois colocar uma nova coluna na tabela da carteira para
//que cada registro contenha seu valor armazenado, cada usuario ter sua carteira
//com todas os saldos das moedas
        String sql = """
                     INSERT INTO public.moedas(
                     	nome, taxavenda, taxacompra)
                     	VALUES (?, ?, ?);
                     """;
        PreparedStatement comando = conn.prepareStatement(sql);
        comando.setString(1, moeda.getNome());
        comando.setDouble(2, moeda.tarifaVenda());
        comando.setDouble(3, moeda.tarifaCompra());
        comando.execute();
        System.out.println("moeda adicionada na tabela moedas");
        sql = String.format("""
              ALTER TABLE IF EXISTS public."Carteira"
                  ADD COLUMN %s real NOT NULL DEFAULT 0;
              """,moeda.getNome());
        comando = conn.prepareStatement(sql);
        comando.execute();
        System.out.println("moeda adicionada na carteira");
    }
    public void removeMoedaDaTabela(String nome) throws SQLException{
//ele vai remover um registro da tabela moedas e remover uma coluna pelo nome na 
//tabela da carteira
        String sql = """
                        DELETE FROM public.moedas
                            WHERE "nome" = ?;
                     """;
        PreparedStatement comando = conn.prepareStatement(sql);
        comando.setString(1, nome);
        comando.execute();
        sql = String.format("""

              ALTER TABLE IF EXISTS public."Carteira"
              DROP COLUMN %s
              """,nome);
        comando = conn.prepareStatement(sql);
        comando.execute();
    }
    
    public ResultSet consultaMoedasGenericas() throws SQLException{
        //apenas um select para retornar um resultset de todas as moedas
        String sql = """
                        SELECT * FROM moedas
                     """;
        PreparedStatement comando = conn.prepareStatement(sql);
        comando.execute();
        return comando.getResultSet();
    }
    
}
