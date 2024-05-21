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
    private final ArrayList<String> moedas = new ArrayList<>();
    public PessoaDAO(Connection conn) {
        this.conn = conn;
    }
    
    public long cadastrar(Pessoa pessoa) throws SQLException{
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
        String sql = "select * from public.\"Pessoa\" where \"CPF\" = ? and \"Senha\" = ?";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setLong(1, Usuario.getCPF());
        statement.setLong(2, Usuario.getSenha());
        statement.execute();
        ResultSet resultado = statement.getResultSet();
        return resultado;
    }
        
    public ResultSet consultarTabelaExtrato(long id) throws SQLException{
        String sql = "select * from \"Extrato\" where \"PessoaID\" = ? ORDER BY \"numeroRegistro\" DESC ";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setLong(1, id);
        statement.execute();
        ResultSet resultado = statement.getResultSet();
        return resultado;
    }
    public ResultSet consultarTabelaCarteira(long id) throws SQLException{
        String sql = "select * from \"Carteira\" where \"PessoaID\" = ? ";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setLong(1, id);
        statement.execute();
        ResultSet resultado = statement.getResultSet();
        return resultado;
    }
    public void cadastrarCarteira(long id) throws SQLException{
        String sql = "INSERT INTO public.\"Carteira\"(\"PessoaID\")\n VALUES (?);";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setLong(1, id);
        statement.execute();
    }

    public void atualizarColunaCarteira(long id,String nome,double valor) throws SQLException{
        String sql = "UPDATE public.\"Carteira\""+
	"SET  ?=?"+
	"WHERE \"PessoaID\"=?;";
        PreparedStatement comando = conn.prepareStatement(sql);
        comando.setString(1,nome);
        comando.setDouble(2, valor);
        comando.setLong(3,id);
        comando.execute();

    }
    public void atualizarCarteira(long id,Carteira user) throws SQLException{
        
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
        String sql="""
                   SELECT "PessoaID", "Nome", "CPF", "Senha", "IsADM"
                   	FROM public."Pessoa";
                   """;
        PreparedStatement comando = conn.prepareStatement(sql);
        comando.execute();
        return comando.getResultSet();
    }
    
    public ResultSet procurarPeloCPF(long cpf) throws SQLException{
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
        String sql = """
                        SELECT * FROM moedas
                     """;
        PreparedStatement comando = conn.prepareStatement(sql);
        comando.execute();
        return comando.getResultSet();
    }
    
}
