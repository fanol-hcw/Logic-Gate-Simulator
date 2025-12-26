package org.example.logicgatesimulator;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import java.io.InputStream;
/*
 * Moin,
 * das hier ist der Startpunkt unserer App.
 * Die Klasse kümmert sich nur um den "Splash Screen".
 * Sie zeigt das Logo und den Ladebalken an, wartet 3 Sekunden und öffnet dann
 * erst das eigentliche Hauptfenster (SimulatorUI).
 */
public class LogicGataSimulatorApplication extends Application {

    @Override
    public void start(Stage stage) {
        StackPane splashRoot = new StackPane();
        splashRoot.setStyle("-fx-background-color: transparent;");
        InputStream is = getClass().getResourceAsStream("/images/logo.jpg");
        if (is != null) {
            ImageView backgroundLogo = new ImageView(new Image(is));
            backgroundLogo.setFitWidth(600);
            backgroundLogo.setFitHeight(400);
            backgroundLogo.setOpacity(0.85);
            splashRoot.getChildren().add(backgroundLogo);
        }
        VBox overlay = new VBox(15);
        overlay.setAlignment(Pos.CENTER);
        overlay.setMaxHeight(VBox.USE_PREF_SIZE);
        overlay.setPadding(new javafx.geometry.Insets(0, 0, 30, 0));

        Label subtitle = new Label("Initializing components...");
        subtitle.setFont(Font.font("Segoe UI", 14));
        subtitle.setTextFill(Color.WHITE);
        subtitle.setStyle("-fx-effect: dropshadow(three-pass-box, black, 5, 0, 0, 0);");

        ProgressBar progressBar = new ProgressBar();
        progressBar.setPrefWidth(300);
        progressBar.setStyle("-fx-accent: #00ff00;");

        overlay.getChildren().addAll(progressBar, subtitle);
        splashRoot.getChildren().add(overlay);
        StackPane.setAlignment(overlay, Pos.BOTTOM_CENTER);

        Scene splashScene = new Scene(splashRoot);
        splashScene.setFill(Color.TRANSPARENT);

        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setScene(splashScene);
        stage.show();

        PauseTransition delay = new PauseTransition(Duration.seconds(3));
        delay.setOnFinished(event -> showMainSimulator(stage));
        delay.play();
    }

    private void showMainSimulator(Stage splashStage) {
        splashStage.close();
        Stage mainStage = new Stage();
        SimulatorUI ui = new SimulatorUI();
        Scene scene = new Scene(ui.getRoot(), 1200, 800);
        mainStage.setTitle("Logic Gate Simulator");
        mainStage.setScene(scene);
        mainStage.setOpacity(0);
        mainStage.show();
        FadeTransition fade = new FadeTransition(Duration.seconds(1), mainStage.getScene().getRoot());
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();
        mainStage.setOpacity(1);
    }

    public static void main(String[] args) {
        launch();
    }
}