package mazegame.game.controller;

import java.io.IOException;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

import gameresult.OnePlayerGameResult;
import gameresult.manager.json.JsonOnePlayerGameResultManager;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import util.DurationUtil;

public class TableViewController {

    private static final int NUMBER_OF_ROWS_TO_SHOW = 15;

    @FXML
    private TableView<OnePlayerGameResult> tableView;

    @FXML
    private TableColumn<OnePlayerGameResult, String> playerName;

    @FXML
    private TableColumn<OnePlayerGameResult, Integer> numberOfMoves;

    @FXML
    public TableColumn<OnePlayerGameResult, Boolean> solved;

    @FXML
    private TableColumn<OnePlayerGameResult, String> duration;

    @FXML
    private TableColumn<OnePlayerGameResult, String> created;

    @FXML
    private void initialize() throws IOException {
        playerName.setCellValueFactory(new PropertyValueFactory<>("playerName"));
        numberOfMoves.setCellValueFactory(new PropertyValueFactory<>("numberOfMoves"));
        solved.setCellValueFactory(new PropertyValueFactory<>("solved"));
        duration.setCellValueFactory(
                cellData -> {
                    var duration = cellData.getValue().getDuration();
                    return new ReadOnlyStringWrapper(DurationUtil.formatDuration(duration));
                });
        created.setCellValueFactory(
                cellData -> {
                    var dateTime = cellData.getValue().getCreated();
                    var formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG);
                    return new ReadOnlyStringWrapper(formatter.format(dateTime));
                }
        );
        ObservableList<OnePlayerGameResult> observableList = FXCollections.observableArrayList();
        observableList.addAll(new JsonOnePlayerGameResultManager(Path.of("results.json")).getBestByNumberOfMoves(NUMBER_OF_ROWS_TO_SHOW));
        tableView.setItems(observableList);
    }
}