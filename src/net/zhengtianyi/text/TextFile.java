package net.zhengtianyi.text;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;



public class TextFile extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        HBox hBox = new HBox();
        hBox.setPadding(new Insets(10));
        ImageView imageView = new ImageView("file:/Users/zhengtianyi/Pictures/鬼刀动漫/鬼刀漫画女孩图片_彼岸图网.jpg");
        imageView.setPreserveRatio(true);
        hBox.getChildren().add(imageView);
        System.out.println(hBox.isResizable());

        primaryStage.setScene(new Scene(hBox));
        primaryStage.setWidth(600);
        primaryStage.setHeight(600);
        primaryStage.show();
        imageView.setFitWidth(primaryStage.getWidth() - 20);
        hBox.widthProperty().addListener((observable, oldValue, newValue) -> {
            imageView.setFitWidth(newValue.doubleValue() - 20);
        });
        hBox.heightProperty().addListener((observable, oldValue, newValue) -> {
            imageView.setFitHeight(newValue.doubleValue() - 20);
        });
    }
}
