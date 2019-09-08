package org.levy.filecollector.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.levy.filecollector.entity.Result;
import org.levy.filecollector.utils.FileUtil;
import org.levy.filecollector.utils.StringUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class FileDealController {
    @FXML
    private AnchorPane rootPane;

    public void chooseDir() {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("请选择待处理文件夹");
        File file = chooser.showDialog(this.getStage());
        if (file != null) {
            ((TextField) rootPane.lookup("#directoryBox")).setText(file.getPath());
            ((TextField) rootPane.lookup("#targetBox")).setText(file.getPath() + "/target");
        }
    }

    private Stage getStage() {
        return (Stage) rootPane.getScene().getWindow();
    }

    public void executeMove() {
        TextField directoryText = (TextField) rootPane.lookup("#directoryBox");
        String path = directoryText.getText();
        if (StringUtil.isEmpty(path)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.titleProperty().set("信息");
            alert.headerTextProperty().set("请先选择待处理文件夹！");
            alert.showAndWait();
            return;
        }
        List<String> todoFormat = new ArrayList<>();
        if (((CheckBox) rootPane.lookup("#mkvCheck")).isSelected()) {
            todoFormat.add("mkv");
        }
        if (((CheckBox) rootPane.lookup("#mp4Check")).isSelected()) {
            todoFormat.add("mp4");
        }
        String customFormat = ((TextField) rootPane.lookup("#otherFormatBox")).getText();
        if (!StringUtil.isEmpty(customFormat)) {
            todoFormat.add(customFormat.trim());
        }
        if (todoFormat.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.titleProperty().set("信息");
            alert.headerTextProperty().set("至少选择一种文件格式！");
            alert.showAndWait();
            return;
        }
        String targetDir = ((TextField) rootPane.lookup("#targetBox")).getText();
        if (StringUtil.isEmpty(targetDir)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.titleProperty().set("信息");
            alert.headerTextProperty().set("目标目录不能为空！");
            alert.showAndWait();
            return;
        }
        List<String> files = this.getTodoFiles(path, todoFormat, ((CheckBox) rootPane.lookup("#skipCurrentCheck")).isSelected(),
                ((CheckBox) rootPane.lookup("#onlyCurrentCheck")).isSelected());
        String keyWord = ((TextField) rootPane.lookup("#keyWordBox")).getText();
        FileUtil.createDirRecursive(new File(targetDir));
        files.forEach(s -> {
            File file = new File(s);
            if (!StringUtil.isEmpty(keyWord)) {
                if (!file.getName().contains(keyWord)) {
                    return;
                }
            }
            file.renameTo(new File(targetDir + (targetDir.endsWith("/") ? "" : "/") + file.getName()));
        });
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.titleProperty().set("信息");
        alert.headerTextProperty().set("Done！");
        alert.showAndWait();
    }

    private List<String> getTodoFiles(String todoPath, List<String> targetFormats, boolean skipCurrent, boolean onlyCurrent) {
        List<String> files = new ArrayList<>();
        Result<Map<String, Boolean>> scanResult = FileUtil.scanDir(todoPath);
        if (scanResult.isSuccess()) {
            scanResult.getData().forEach((key, value) -> {
                if (value) {
                    if (!skipCurrent) {
                        if (targetFormats.contains(key.substring(key.lastIndexOf(".") + 1).toLowerCase())) {
                            files.add(key);
                        }
                    }
                } else {
                    if (!onlyCurrent) {
                        if (!key.startsWith(".")) {
                            files.addAll(this.getTodoFiles(key, targetFormats, false, false));
                        }
                    }
                }
            });
        }
        return files;
    }
}
