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
import model.Pessoa;

/**
 *
 * @author unifgmorassi
 */
public class PessoaDAO {
    private Connection conn;

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
        String sql = "select * from \"Extrato\" where \"PessoaID\" = ? ";
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
    public void atualizarCarteira(long id,Carteira user,ArrayList<String> moedas) throws SQLException{
        
        String sql = "UPDATE public.\"Carteira\"\n" +
"	SET \n"
                + "\"Bitcoin\"=?, \"Ripple\"=?, \"Ethereum\"=?, \"Reais\"=?\n" +
"	WHERE \"PessoaID\"=? ;";
        PreparedStatement comando = conn.prepareStatement(sql);
        
    }
    public void addExtrato(long id,Extrato extrato) throws SQLException{
        String sql = "INSERT INTO public.\"Extrato\"(\n" +
"	\"PessoaID\", \"Data\", operacao, valor, taxa, moeda, saldo)\n" +
"	VALUES (?, ?, ?, ?, ?, ?, ?);";
        PreparedStatement comando = conn.prepareStatement(sql);
        comando.setLong(1,id);
        comando.setString(2,extrato.getData().toString());
        comando.setString(3,extrato.getOperacao());
    }
}
