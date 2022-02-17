package ifpr.pgua.eic.projetovendas.telas;

import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;

import ifpr.pgua.eic.projetovendas.App;
import ifpr.pgua.eic.projetovendas.models.ItemVenda;
import ifpr.pgua.eic.projetovendas.models.Pessoa;
import ifpr.pgua.eic.projetovendas.models.Produto;
import ifpr.pgua.eic.projetovendas.models.Venda;
import ifpr.pgua.eic.projetovendas.repositorios.RepositorioPessoas;
import ifpr.pgua.eic.projetovendas.repositorios.RepositorioProdutos;
import ifpr.pgua.eic.projetovendas.repositorios.RepositorioVendas;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;

public class Listas {

    @FXML
    private ListView<Pessoa> lstPessoas;

    @FXML
    private ListView<Produto> lstProdutos;

    @FXML
    private Label lbListaVaziaProdutos;

    @FXML
    private Label lbTotalVendas;

    @FXML
    private FlowPane rootPane;

    @FXML
    private TableView<ItemVenda> tbItensVenda;

    @FXML
    private TableView<Venda> tbListaVendas;

    @FXML
    private TableColumn<Venda, Integer> tbcIdVenda;

    @FXML
    private TableColumn<Venda, String> tbcDataVenda;

    @FXML
    private TableColumn<Venda, String> tbcNomeCliente;

    @FXML
    private TableColumn<Venda, Double> tbcTotalVenda;

    @FXML
    private TableColumn<ItemVenda, String> tbcNomeItem;

    @FXML
    private TableColumn<ItemVenda, Integer> tbcQuantidadeItem;

    private RepositorioProdutos repositorioProdutos;
    private RepositorioPessoas repositorioPessoas;
    private RepositorioVendas repositorioVendas;

    public Listas(RepositorioPessoas repositorioPessoas, RepositorioProdutos repositorio,
            RepositorioVendas repositorioVendas) {
        this.repositorioProdutos = repositorio;
        this.repositorioPessoas = repositorioPessoas;
        this.repositorioVendas = repositorioVendas;
    }

    public void initialize() {

        lstPessoas.setCellFactory(lista -> new ListCell<>() {
            protected void updateItem(Pessoa pessoa, boolean alterou) {
                super.updateItem(pessoa, alterou);
                if (pessoa != null) {
                    setText("(" + pessoa.getId() + ")" + pessoa.getNome());
                } else {
                    setText(null);
                }
            };
        });
        lstProdutos.setCellFactory(lista -> new ListCell<>() {
            protected void updateItem(Produto produto, boolean alterou) {
                super.updateItem(produto, alterou);
                if (produto != null) {
                    setText("(" + produto.getId() + ")" + produto.getNome());
                } else {
                    setText(null);
                }
            };
        });

        // configura colunas

        tbcIdVenda.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getId()).asObject());
        tbcDataVenda.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getDataHora().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
        tbcNomeCliente.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPessoa().getNome()));
        tbcTotalVenda.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getValorTotal()).asObject());

        tbcNomeItem.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getProduto().getNome()));
        tbcQuantidadeItem
                .setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getQuantidade()).asObject());
        ;

        try {
            lstPessoas.getItems().addAll(repositorioPessoas.listarPessoas());
            List<Produto> produtos = repositorioProdutos.listarProdutos();
            lstProdutos.getItems().addAll(produtos);

            tbListaVendas.getItems().addAll(repositorioVendas.listar());

            lbTotalVendas.setText("Total Vendas R$ " + repositorioVendas.totalVendas());
        } catch (Exception e) {
            Alert alert = new Alert(AlertType.ERROR, e.getMessage());
            alert.showAndWait();
        }

    }

    @FXML
    private void atualizarRemoverPessoa(MouseEvent event) {
        Pessoa pessoaSelecionada = lstPessoas.getSelectionModel().getSelectedItem();

        if (event.getButton() == MouseButton.SECONDARY && event.getClickCount() == 2) {
            if (pessoaSelecionada != null) {
                try {
                    repositorioPessoas.removerPessoa(pessoaSelecionada.getId());
                    lstPessoas.getItems().clear();
                    lstPessoas.getItems().addAll(repositorioPessoas.listarPessoas());
                } catch (Exception e) {
                    Alert alert = new Alert(AlertType.ERROR, e.getMessage());
                    alert.showAndWait();
                }

            }
        } else if (event.getClickCount() == 2) {

            if (pessoaSelecionada != null) {
                // substituir o painelCentral do Home
                StackPane painelCentral = (StackPane) rootPane.getParent();

                painelCentral.getChildren().clear();
                painelCentral.getChildren().add(App.loadTela("fxml/cadastro_pessoa.fxml",
                        o -> new CadastroPessoa(pessoaSelecionada, repositorioPessoas)));
            }
        }
    }

    @FXML
    private void atualizarRemoverProduto(MouseEvent event) {
        Produto produtoSelecionado = lstProdutos.getSelectionModel().getSelectedItem();
        if (event.getButton() == MouseButton.SECONDARY && event.getClickCount() == 2) {
            if (produtoSelecionado != null) {
                try {
                    lstProdutos.getItems().clear();
                    lstProdutos.getItems().addAll(repositorioProdutos.listarProdutos());
                    repositorioProdutos.removerProduto(produtoSelecionado.getId());
                } catch (Exception e) {
                    Alert alert = new Alert(AlertType.ERROR, e.getMessage());
                    alert.showAndWait();
                }
            }
        } else if (event.getClickCount() == 2) {
            if (produtoSelecionado != null) {
                StackPane painelCentral = (StackPane) rootPane.getParent();

                painelCentral.getChildren().clear();
                painelCentral.getChildren().add(App.loadTela("fxml/cadastro_produto.fxml",
                        o -> new CadastroProduto(produtoSelecionado, repositorioProdutos)));
            }
        }
    }

    @FXML
    private void mostrarItensVenda(MouseEvent event) {
        Venda vendaSelecionada = tbListaVendas.getSelectionModel().getSelectedItem();
        if (event.getClickCount() == 1) {
            if (vendaSelecionada != null) {
                try {
                    tbItensVenda.getItems().clear();
                    tbItensVenda.getItems().addAll(vendaSelecionada.getItens());
                } catch (Exception e) {
                    Alert alert = new Alert(AlertType.ERROR, e.getMessage());
                    alert.showAndWait();
                }

            }
        }
    }

}
