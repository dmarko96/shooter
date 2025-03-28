module com.example.shooter {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.shooter to javafx.fxml;
    exports com.example.shooter;
    exports com.example.shooter.field;
    opens com.example.shooter.field to javafx.fxml;
    exports com.example.shooter.player;
    opens com.example.shooter.player to javafx.fxml;
}