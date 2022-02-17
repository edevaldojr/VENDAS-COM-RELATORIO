module ifpr.pgua.eic.projetovendas {
    requires javafx.controls;
    requires javafx.fxml;
    requires itextpdf;
    requires mysql.connector.java;
    requires java.sql;
    

    opens ifpr.pgua.eic.projetovendas.telas to javafx.fxml;
    exports ifpr.pgua.eic.projetovendas;
}
