module lightning_productivity {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;

    opens lightning_productivity to javafx.fxml;

    exports lightning_productivity;
}
