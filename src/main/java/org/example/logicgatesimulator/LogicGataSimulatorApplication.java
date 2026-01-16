package org.example.logicgatesimulator;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
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
import java.io.IOException;
import java.io.InputStream;
/*
 * das hier ist der Startpunkt unserer App.
 * Die Klasse kümmert sich um den "Splash Screen".
 * Sie zeigt das Logo und den Ladebalken an, wartet kurz und öffnet dann
 * das Start-Menü (start-menu.fxml).
 */
public class LogicGataSimulatorApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
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

        delay.setOnFinished(event -> showStartMenu(stage));
        delay.play();
    }

    private void showStartMenu(Stage splashStage) {
        splashStage.close();
        Stage stage = new Stage();

        try {
            Parent root = FXMLLoader.load(getClass().getResource("start-menu.fxml"));
            stage.setScene(new Scene(root, 600, 400));
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}