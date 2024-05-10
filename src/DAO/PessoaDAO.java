/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
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
    
        public void cadastrar(Pessoa pessoa) throws SQLException{
        String sqlInsert = "INSERT INTO public.\"Pessoa\"(\"Nome\",\"CPF\",\"Senha\")"
                + "VALUES (?,?,?)";
        PreparedStatement state = conn.prepareStatement(sqlInsert);
        state.setString(1, pessoa.getNome());
        state.setLong(2, pessoa.getCPF());
        state.setLong(3, pessoa.getSenha());
        state.execute();
        conn.close();
        
    }
    
}
