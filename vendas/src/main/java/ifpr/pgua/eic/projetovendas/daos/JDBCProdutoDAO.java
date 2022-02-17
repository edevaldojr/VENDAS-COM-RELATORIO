package ifpr.pgua.eic.projetovendas.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import ifpr.pgua.eic.projetovendas.daos.interfaces.ProdutoDAO;
import ifpr.pgua.eic.projetovendas.models.Produto;
import ifpr.pgua.eic.projetovendas.utils.FabricaConexoes;

public class JDBCProdutoDAO implements ProdutoDAO {

    private FabricaConexoes fabricaConexoes;

    public JDBCProdutoDAO(FabricaConexoes fabricaConexoes){
        this.fabricaConexoes = fabricaConexoes;
    }


    @Override
    public boolean cadastrar(Produto p) throws Exception {
        Connection con = fabricaConexoes.getConnection();

        String sql = "INSERT INTO ATIVIDADE_OO2_produto(nome, descricao, quantidadeEstoque, valor) VALUES (?,?,?,?)";

        PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        pstmt.setString(1, p.getNome());
        pstmt.setString(2, p.getDescricao());
        pstmt.setInt(3, p.getQuantidadeEstoque());
        pstmt.setDouble(4, p.getValor());

        pstmt.execute();

        // pegando o id gerado para a Produto
        ResultSet rsId = pstmt.getGeneratedKeys();
        rsId.next();
        int id = rsId.getInt(1);

        rsId.close();
        pstmt.close();
        con.close();

        p.setId(id);

        return true;

    }

    @Override
    public boolean atualizar(int id, Produto p) throws Exception {
        Connection con = fabricaConexoes.getConnection();

        String sql = "UPDATE ATIVIDADE_OO2_produto SET nome=?, descricao=?, quantidadeEstoque=?, valor=?  WHERE idProduto=?";

        PreparedStatement pstmt = con.prepareStatement(sql);

        pstmt.setString(1, p.getNome());
        pstmt.setString(2, p.getDescricao());
        pstmt.setInt(3, p.getQuantidadeEstoque());
        pstmt.setDouble(4, p.getValor());
        pstmt.setInt(5, id);

        int ret = pstmt.executeUpdate();

        pstmt.close();
        con.close();

        return ret==1;

    }

    @Override
    public boolean remover(int id) throws Exception {
        Connection con = fabricaConexoes.getConnection();

        //String sql = "DELETE FROM ATIVIDADE_OO2_produto WHERE idProduto=?";

        String sql = "UPDATE ATIVIDADE_OO2_produto SET ativo=0 WHERE idProduto=?";

        PreparedStatement pstmt = con.prepareStatement(sql);
    
        pstmt.setInt(1, id);

        int ret = pstmt.executeUpdate();
        con.close();
        pstmt.close();
        return ret==1;
    }

    private Produto montarProduto(ResultSet rs) throws Exception {
        int id = rs.getInt("idProduto");
        String nome = rs.getString("nome");
        String descricao = rs.getString("descricao");
        int quantidadeEstoque = rs.getInt("quantidadeEstoque");
        double valor = rs.getDouble("valor");

        Produto p = new Produto(id, nome, descricao, quantidadeEstoque, valor);
        
        return p;
    }

    @Override
    public ArrayList<Produto> lista() throws Exception {
        ArrayList<Produto> lista = new ArrayList<>();

        Connection con = fabricaConexoes.getConnection();

        String sql = "SELECT * FROM ATIVIDADE_OO2_produto WHERE ativo=1";

        PreparedStatement pstmt = con.prepareStatement(sql);
        
        ResultSet rs = pstmt.executeQuery();

        while(rs.next()){
            Produto p = montarProduto(rs);
            lista.add(p);
        }

        rs.close();
        pstmt.close();
        con.close();

        return lista;
    }

    @Override
    public Produto buscar(int id) throws Exception {
        Produto p = null;

        Connection con = fabricaConexoes.getConnection();

        String sql = "SELECT * FROM ATIVIDADE_OO2_produto WHERE idProduto=?";

        PreparedStatement pstmt = con.prepareStatement(sql);

        pstmt.setInt(1, id);

        ResultSet rs = pstmt.executeQuery();

        while(rs.next()){
            p = montarProduto(rs);
        }

        rs.close();
        pstmt.close();
        con.close();

        return p;
    }


    @Override
    public Produto buscarProdutoItem(int idItem) throws Exception {
        Connection con = fabricaConexoes.getConnection();

        String sqlIdProduto = "SELECT idProduto FROM ATIVIDADE_OO2_itensvendas WHERE id=?";
        
        PreparedStatement pstmt = con.prepareStatement(sqlIdProduto);

        pstmt.setInt(1, idItem);

        ResultSet rsIdProduto = pstmt.executeQuery();

        rsIdProduto.next();
        int idProduto = rsIdProduto.getInt(1);

        rsIdProduto.close();
        pstmt.close();
        rsIdProduto.close();

        return buscar(idProduto);
    }

}