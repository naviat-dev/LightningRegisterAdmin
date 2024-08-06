module lightning_productivity {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    opens lightning_productivity to javafx.fxml;

    exports lightning_productivity;
}
