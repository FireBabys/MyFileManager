package net.zhengtianyi.entity;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import net.zhengtianyi.util.SingLetonTika;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

public class FileData {

    private String fileName;        //文件名
    private SimpleStringProperty fileNamePro;

    private String fileSize;        //文件大小
    private SimpleStringProperty fileSizePro;

    private String fileType;        //文件类型
    private SimpleStringProperty fileTypePro;

    private String fileUri;         //文件路径
    private SimpleStringProperty fileUriPro;

    private String createTime;      //创建时间
    private SimpleStringProperty createTimePro;

    private String modificationTime;    //修改时间
    private SimpleStringProperty modificationTimePro;

    private String owner;           //拥有者
    private SimpleStringProperty ownerPro;

    private Boolean isSelected = false; //是否被选中
    private SimpleBooleanProperty isSelectedPro;

    private Path file;  //文件

    public FileData(){}
    public FileData(String fileName){
        this.fileName = fileName;
    }
    public FileData(String fileName,Path file){
        this.fileName = fileName;
        try {
            initFileData(file);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("获取文件属性异常!");
        }
    }
    public FileData(Path file){
        this.fileName = file.getFileName().toString(); //设置名称
        try {
            initFileData(file);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("获取文件属性异常!");
        }
    }

    private void initFileData(Path file) throws IOException {
        this.file = file;
        this.fileUri = file.toString(); //获取全路径
        BasicFileAttributes ba  = Files.readAttributes(file, BasicFileAttributes.class);
        this.createTime = ba.creationTime().toString(); //获取创建时间
        this.modificationTime = ba.lastModifiedTime().toString(); //获取修改时间
        this.owner = Files.getOwner(file).toString(); //获取拥有者
        //判断是否是文件夹
        if (Files.isDirectory(file)){
            this.fileSize = "--";
            this.fileType = "文件夹";
            this.createTime = "";
        }else{
            this.fileSize = String.valueOf(Files.size(file));   //获取文件大小
            this.fileType = SingLetonTika.getInstance().detect(file); //获取文件类型
        }
    }


    public String getFileName() {
        if (fileNamePro == null) {
            return fileName;
        }else{
            return fileNamePro.get();
        }
    }

    public void setFileName(String fileName) {
        if (fileNamePro == null) {
            this.fileName = fileName;
        }else{
            fileNamePro.set(fileName);
        }
    }

    public SimpleStringProperty getFileNamePro(){
        if (fileNamePro == null) {
            fileNamePro = new SimpleStringProperty(this,"fileName",fileName);
        }
        return fileNamePro;
    }

    public String getFileSize() {
        if (fileSizePro == null) {
            return fileSize;
        }else{
            return fileSizePro.get();
        }
    }

    public void setFileSize(String fileSize) {
        if (fileSizePro == null) {
            this.fileSize = fileSize;
        }else{
            fileSizePro.set(fileSize);
        }
    }

    public SimpleStringProperty getFileSizePro(){
        if (fileSizePro == null) {
            fileSizePro = new SimpleStringProperty(this,"fileSize",fileSize);
        }
        return fileSizePro;
    }

    public String getFileType() {
        if (fileTypePro == null) {
            return fileType;
        }else{
            return fileTypePro.get();
        }
    }

    public void setFileType(String fileType) {
        if (fileTypePro == null) {
            this.fileType = fileType;
        }else{
            fileTypePro.set(fileType);
        }
    }

    public SimpleStringProperty getFileTypePro(){
        if (fileTypePro == null) {
            fileTypePro = new SimpleStringProperty(this,"fileType",fileType);
        }
        return fileTypePro;
    }

    public String getFileUri() {
        if (fileUriPro == null) {
            return fileUri;
        }else{
            return fileUriPro.get();
        }
    }

    public void setFileUri(String fileUri) {
        if (fileUriPro == null) {
            this.fileUri = fileUri;
        }else{
            fileUriPro.set(fileUri);
        }
    }

    public SimpleStringProperty getFileUriPro(){
        if (fileUriPro == null) {
            fileUriPro = new SimpleStringProperty(this,"fileUri",fileUri);
        }
        return fileUriPro;
    }

    public String getCreateTime() {
        if (createTimePro == null) {
            return createTime;
        }else{
            return createTimePro.get();
        }
    }

    public void setCreateTime(String createTime) {
        if (createTimePro == null) {
            this.createTime = createTime;
        }else{
            createTimePro.set(createTime);
        }
    }

    public SimpleStringProperty getCreateTimePro(){
        if (createTimePro == null) {
            createTimePro = new SimpleStringProperty(this,"createTime",createTime);
        }
        return createTimePro;
    }

    public String getModificationTime() {
        if (modificationTimePro == null) {
            return modificationTime;
        }else{
            return modificationTimePro.get();
        }
    }

    public void setModificationTime(String modificationTime) {
        if (modificationTimePro == null) {
            this.modificationTime = modificationTime;
        }else{
            modificationTimePro.set(modificationTime);
        }
    }

    public SimpleStringProperty getModificationTimePro(){
        if (modificationTimePro == null) {
            modificationTimePro = new SimpleStringProperty(this,"modificationTime",modificationTime);
        }
        return modificationTimePro;
    }

    public String getOwner() {
        if (ownerPro == null) {
            return owner;
        }else{
            return ownerPro.get();
        }
    }

    public void setOwner(String owner) {
        if (ownerPro == null) {
            this.fileName = owner;
        }else{
            ownerPro.set(owner);
        }
    }

    public SimpleStringProperty getOwnerPro(){
        if (ownerPro == null) {
            ownerPro = new SimpleStringProperty(this,"owner",owner);
        }
        return ownerPro;
    }

    public Boolean getSelected() {
        if (isSelectedPro == null) {
            return isSelected;
        }else{
            return isSelectedPro.get();
        }
    }

    public void setSelected(Boolean selected) {
        if (isSelectedPro == null) {
            this.isSelected = selected;
        }else{
            isSelectedPro.set(selected);
        }
    }

    public SimpleBooleanProperty getSelectedPro(){
        if (isSelectedPro == null) {
            isSelectedPro = new SimpleBooleanProperty(this,"isSelected",isSelected);
        }
        return isSelectedPro;
    }

    public Path getFile() {
        return file;
    }

    public void setFile(Path file) {
        this.file = file;
    }





}
