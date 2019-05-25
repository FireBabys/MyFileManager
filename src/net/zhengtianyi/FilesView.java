package net.zhengtianyi;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import net.zhengtianyi.entity.FileData;
import net.zhengtianyi.exception.FileSizeException;
import net.zhengtianyi.exception.FileTypeException;

import java.io.IOException;
import java.nio.file.Files;


public class FilesView extends Stage {

    String str;
    SimpleStringProperty textString = new SimpleStringProperty();

    public FilesView(FileData fileData) throws IOException {
        HBox root = new HBox();
        root.setPadding(new Insets(10));
        setScene(new Scene(root));
        setWidth(800);
        setHeight(480);
        dispatcher(root,fileData);
        setTitle("文件查看器");


    }

    //根据文件不同类型进行不同处理
    private void dispatcher(HBox root,FileData fileData) throws IOException {
        //判断如果大小超过10兆抛文件大小异常
        if (Long.valueOf(fileData.getFileSize()) > 10 * 1024 * 1024) throw new FileSizeException();
        String fileType = fileData.getFileType();
        fileType = fileType.substring(0, fileType.indexOf("/"));


        if ("image".equals(fileType)) { //图相处理
            ImageView imageView = new ImageView("file:" + fileData.getFileUri());
            imageView.setPreserveRatio(true); //开启同比例缩放
            imageView.prefHeight(-1);
            imageView.setFitWidth(getWidth() - 20);
            root.widthProperty().addListener((observable, oldValue, newValue) -> imageView.setFitWidth(newValue.doubleValue() - 20));
            root.heightProperty().addListener((observable, oldValue, newValue) -> imageView.setFitHeight(newValue.doubleValue() - 20));
            root.getChildren().add(imageView);
        }else if ("text".equals(fileType)){ //文本处理
            VBox vBox = new VBox(10);
            vBox.setPadding(new Insets(10));
            vBox.prefWidthProperty().bind(widthProperty());
            vBox.prefHeightProperty().bind(heightProperty());
            TextArea text = new TextArea();
            HBox hBox = new HBox(10);
            hBox.setAlignment(Pos.CENTER_LEFT);
            ObservableList<String> strings = FXCollections.observableArrayList("UTF_8","GBK");
            ComboBox<String> comboBox = new ComboBox<String>(strings);
            comboBox.setValue("UTF_8");
            textString.set(new String(Files.readAllBytes(fileData.getFile()),"utf-8"));
            text.textProperty().bind(textString);
            text.setEditable(false);
            Label label = new Label("文件编码");
            hBox.getChildren().addAll(label,comboBox);

            VBox.setVgrow(text, Priority.ALWAYS);
            vBox.getChildren().addAll(hBox,text);

            root.getChildren().add(vBox);

            comboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                try {
                    if ("GBK".equals(newValue)) {
                        textString.set(new String(Files.readAllBytes(fileData.getFile()),newValue));
                    }else if ("UTF_8".equals(newValue)){
                        textString.set(new String(Files.readAllBytes(fileData.getFile()),"utf-8"));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }else{
            throw new FileTypeException();
        }
    }

}
