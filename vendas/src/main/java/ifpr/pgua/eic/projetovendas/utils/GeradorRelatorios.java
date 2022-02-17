package ifpr.pgua.eic.projetovendas.utils;

import java.io.FileOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import ifpr.pgua.eic.projetovendas.App;
import ifpr.pgua.eic.projetovendas.repositorios.RepositorioVendas;

public class GeradorRelatorios {

    private RepositorioVendas repositorioVendas;

    public GeradorRelatorios(RepositorioVendas repositorioVendas) {
        this.repositorioVendas = repositorioVendas;
    }

    public void gerarRelatorio(String nomeArquivo) throws Exception {

        // cria um novo documento PDF
        Document document = new Document();

        // cria o arquivo onde será salvo o documento
        PdfWriter.getInstance(document, new FileOutputStream(nomeArquivo));

        // abre o documento para edição
        document.open();

        // carrega uma imagem armazenada no recursos para inserir no relatório
        Path path = Paths.get(App.class.getResource("img/img.jpg").toURI());

        Image img = Image.getInstance(path.toAbsolutePath().toString());

        // ajustando o tamanho da imagem
        img.scaleToFit(new Rectangle(0, 0, 50, 50));

        // alinhando a imagem a esquerda
        img.setAlignment(Image.LEFT);

        // cria um elemento parágrafo, que serve para inserir texto no documento.
        Paragraph paragrafo = new Paragraph();

        // adiciona a imagem no parágrafo
        paragrafo.add(img);

        // adiciona um texto no parágrafo
        paragrafo.add("Os três items mais vendidos!!!");

        // adiciona o parágrafo no documento
        document.add(paragrafo);

        // adiciona um espaço em branco no documento
        document.add(new Chunk());

        // cria uma tabela com 3 colunas
        PdfPTable table = new PdfPTable(3);

        // ajusta o cabeçalho da tabela, inserindo uma linha que contém o
        // título de cada coluna
        String[] cabecalho = { "idProduto", "Nome", "Quantidade total" };
        for (String s : cabecalho) {
            // cria uma célula
            PdfPCell cell = new PdfPCell();
            // adiciona o texto na célula
            cell.addElement(new Paragraph(s));
            // ajusta a cor de fundo da célula
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            // ajusta a largura da borda
            cell.setBorderWidth(2);
            // adiciona a célula na tabela. Importante ressaltar
            // que a inserção se dá da esquerda para a direita
            table.addCell(cell);
        }

        for (String[] s : repositorioVendas.maisVendidos()) {
            table.addCell(s[0]);
            table.addCell(s[1]);
            table.addCell(s[2]);
        }

        // adiciona a tabela no documento
        document.add(table);

        // fecha o documento e salva em arquivo
        document.close();

    }

}
