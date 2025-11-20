module org.example.demo6 {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.demo6 to javafx.fxml;
    exports org.example.demo6;
}