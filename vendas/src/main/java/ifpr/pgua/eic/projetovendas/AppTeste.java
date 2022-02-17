package ifpr.pgua.eic.projetovendas;

import ifpr.pgua.eic.projetovendas.daos.JDBCVendaDAO;
import ifpr.pgua.eic.projetovendas.utils.FabricaConexoes;

/**
 * JavaFX App
 */
public class AppTeste {

    public static void main(String[] args) throws Exception {
        FabricaConexoes fabricaConexoes = FabricaConexoes.getInstance();

        JDBCVendaDAO vendaDAO = new JDBCVendaDAO(fabricaConexoes);

        System.out.println(vendaDAO.totalVendasPessoa(1));
        
        
    }

}