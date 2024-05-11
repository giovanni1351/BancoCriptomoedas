/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    public void cadastrarCarteira(long id,double bitcoin,double ripple,double ethereum,double reais) throws SQLException{
        String sql = "INSERT INTO public.\"Carteira\"(\"PessoaID\",\"Bitcoin\",\"Ripple\",\"Ethereum\",\"Reais\")"
                + "VALUES(\"?\",\"?\",\"?\",\"?\",\"?\")";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setLong(1, id);
        statement.setDouble(2, bitcoin);
        statement.setDouble(3, ripple);
        statement.setDouble(4, ethereum);
        statement.setDouble(5, reais);
        statement.execute();
    }
    
}
