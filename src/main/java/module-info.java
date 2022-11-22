module com.example.GUIClass {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.GUIClass to javafx.fxml;
    exports com.example.GUIClass;
}