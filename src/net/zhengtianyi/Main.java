package net.zhengtianyi;

import javafx.application.Application;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import net.zhengtianyi.entity.FileData;
import net.zhengtianyi.exception.FileSizeException;
import net.zhengtianyi.exception.FileTypeException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;


public class Main extends Application {
    //设置左侧列表主目录
    Path path = Paths.get("/Users/zhengtianyi");
    //记录访问路径,用于返回
    ObservableList<Path> historyPath = FXCollections.observableArrayList();
    TextField textField = new TextField("/");
    Alert alert = new Alert(Alert.AlertType.WARNING); //用于提示


    @Override
    public void start(Stage primaryStage) throws Exception {
        historyPath.add(path);
        BorderPane root = new BorderPane();
        //顶部水平布局
        HBox top_HBox = new HBox(10);
        //顶部Button
        Button top_Previous = new Button("返回");
        top_Previous.setDisable(true);
        Button top_Next = new Button("前进");
        textField.setPrefWidth(600);

        top_HBox.getChildren().addAll(top_Previous,top_Next,textField);

        //左侧列表

        TreeItem<FileData> left_ItemRoot = new TreeItem<>(new FileData("我的文件夹",path));
        TreeView<FileData> left_TreeView = new TreeView<>(left_ItemRoot);
        left_TreeView.setCellFactory(TextFieldTreeCell.forTreeView(new StringConverter<FileData>() {
            @Override
            public String toString(FileData object) {
                return object.getFileName();
            }

            @Override
            public FileData fromString(String string) {
                return null;
            }
        }));

        left_ItemRoot.getChildren().addAll(getLeft_TreeItems(path));
        left_ItemRoot.setExpanded(true);


        //中部列表
        TableView<FileData> center_tableView = new TableView<>();
        //文件名列
        TableColumn<FileData, String> center_column_fileName = new TableColumn<>("名称");
        center_column_fileName.setCellValueFactory(new PropertyValueFactory<>("fileName"));
        //修改时间列
        TableColumn<FileData, String> center_column_modificationTime = new TableColumn<>("修改时间");
        center_column_modificationTime.setCellValueFactory(new PropertyValueFactory<>("modificationTime"));
        //大小列
        TableColumn<FileData, String> center_column_fileSize = new TableColumn<>("大小");
        center_column_fileSize.setCellValueFactory(new PropertyValueFactory<>("fileSize"));
        //种类列
        TableColumn<FileData, String> center_column_fileType = new TableColumn<>("种类");
        center_column_fileType.setCellValueFactory(new PropertyValueFactory<>("fileType"));
        //所有者
        TableColumn<FileData, String> center_column_owner = new TableColumn<>("所有者");
        center_column_owner.setCellValueFactory(new PropertyValueFactory<>("owner"));
        center_column_owner.setVisible(false);  //设置为默认不显示
        //创建时间列
        TableColumn<FileData, String> center_column_createTime = new TableColumn<>("创建时间");
        center_column_createTime.setCellValueFactory(new PropertyValueFactory<>("createTime"));
        center_column_createTime.setVisible(false); //设置为默认不显示
        //选择列
        TableColumn<FileData, Boolean> center_column_isSelected = new TableColumn<>("选择");
        center_column_isSelected.setCellValueFactory(new PropertyValueFactory<>("isSelected"));
        center_column_isSelected.setVisible(false);  //设置为默认不显示

        //将所有列添加到文件列表
        center_tableView.getColumns().addAll(center_column_fileName,center_column_modificationTime,center_column_fileSize,center_column_fileType,center_column_owner,center_column_createTime,center_column_isSelected);
        //设置文件列表所有列宽平均分配
        center_tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        //设置边角菜单选择显示列
        center_tableView.setTableMenuButtonVisible(true);
        //设置占位符
        center_tableView.setPlaceholder(new Label("暂无数据"));

        root.setTop(top_HBox);
        root.setLeft(left_TreeView);
        root.setCenter(center_tableView);
        root.setPadding(new Insets(10));
        BorderPane.setMargin(top_HBox,new Insets(0,0,10,0));
        BorderPane.setMargin(left_TreeView,new Insets(0,10,0,0));
        primaryStage.setScene(new Scene(root));
        primaryStage.setWidth(1000);
        primaryStage.setHeight(600);
        primaryStage.setTitle("文件管理器");
        primaryStage.show();

        //给左侧菜单列表设置选择监听事件
        left_TreeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            setCenterTableView(center_tableView,newValue.getValue().getFile());
            setLocation(newValue.getValue().getFile());
        });

        //给每行设置双击事件,如果是文件夹可以打开
        center_tableView.setRowFactory(new Callback<TableView<FileData>, TableRow<FileData>>() {
            @Override
            public TableRow<FileData> call(TableView<FileData> param) {
                return new TableRow<FileData>(){
                    @Override
                    protected void updateItem(FileData item, boolean empty) {
                        super.updateItem(item, empty);
                        if (!empty){
                            setOnMouseClicked(p -> {
                                if (p.getClickCount() >= 2 && p.getButton() == MouseButton.PRIMARY){
                                    if (Files.isDirectory(item.getFile())) {
                                        setCenterTableView(param,item.getFile());
                                        historyPath.add(item.getFile().getParent());
                                        setLocation(item.getFile());
                                    }else{
                                        try {
                                            new FilesView(item).show();
                                        } catch (FileSizeException e) {
                                            alert.setContentText("只支持查看10M以内的文件!");
                                            alert.show();
                                        } catch (FileTypeException e) {
                                            alert.setContentText("暂不支持此格式!");
                                            alert.show();
                                        } catch (Exception e) {
                                            System.err.println(e);
                                            alert.setContentText("错误!");
                                            alert.show();
                                        }
                                    }
                                }
                            });
                        }
                    }
                };
            }
        });

        top_Previous.setOnAction(p ->{
            if (historyPath.size() == 1) return;
            Path path = historyPath.get(historyPath.size() - 1);
            setCenterTableView(center_tableView,path);
            setLocation(path);
            historyPath.remove(historyPath.size() - 1);

        });

        historyPath.addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                ObservableList<Path> temp = (ObservableList<Path>)observable;
                if (temp.size() == 1) {
                    top_Previous.setDisable(true);
                }else{
                    top_Previous.setDisable(false);
                }
            }
        });

    }

    public static void main(String[] args) {
        launch(args);
    }


    //向中间列表设置数据
    public void setCenterTableView(TableView tableView,Path file){
        tableView.getItems().clear();
        tableView.getItems().addAll(getFileDatas(file));
    }


    //获取文件对象
    private ObservableList<FileData> getFileDatas(Path path) {
        ObservableList<FileData> observableList = FXCollections.observableArrayList();
        //获取文件
        Stream<Path> fileList = null;
        try {
            fileList = Files.list(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        fileList.forEach(p -> {
            try {
                if (!Files.isHidden(p)) {
                    observableList.add(new FileData(p));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return observableList;
    }

    //获取TreeItem
    private ObservableList<TreeItem<FileData>> getLeft_TreeItems(Path path) throws IOException {
        ObservableList<TreeItem<FileData>> observableList = FXCollections.observableArrayList();
        Stream<Path> fileList = Files.list(path);
        fileList.forEach(p -> {
            try {
                if (Files.isDirectory(p) && !Files.isHidden(p)){
                    TreeItem<FileData> left_Item = new TreeItem<>(new FileData(p));
                    observableList.add(left_Item);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return  observableList;
    }

    //设置地址栏路径
    private void setLocation(Path path){
        textField.setText(path.toString());
    }
}
