package com.social.socialnetwork.Controller;

import com.social.socialnetwork.AppExceptions.AppException;
import com.social.socialnetwork.Domain.PrietenieDTO;
import com.social.socialnetwork.Domain.Utilizator;
import com.social.socialnetwork.Service.Service;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

import java.util.function.UnaryOperator;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class EditPrieteniController {

    @FXML
    TextField idUtilizator1;
    @FXML
    TextField idUtilizator2;
    @FXML
    Button saveButton;
    @FXML
    Button cancelButton;
    @FXML
    ComboBox<PrietenieDTO> utilizatoriComboBox;

    private Service service;
    private Stage dialogStage;
    private Utilizator utilizator;

    public void setService(Service service, Stage dialogStage, Utilizator utilizator){
        this.service = service;
        this.dialogStage = dialogStage;
        this.utilizator = utilizator;
        this.idUtilizator1.setEditable(false);
        utilizatoriComboBox.setVisible(false);
        loadPossibleFriends();
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (Pattern.matches("[0-9]*", newText)) {
                return change;
            }
            return null;
        };
        TextFormatter<Integer> textFormatter = new TextFormatter<>(new IntegerStringConverter(), 0, filter);
        idUtilizator2.setTextFormatter(textFormatter);
        idUtilizator2.setText("1");
    }
    private void loadPossibleFriends(){
        this.idUtilizator1.setText(utilizator.getId().toString());}

    public void handlerCancel(ActionEvent actionEvent) {
        this.dialogStage.close();
    }

    public void handlerSave(ActionEvent actionEvent) {
        //idk
        Long id_utilizator1 = utilizator.getId();
        Long id_utilizator2 = Long.parseLong(idUtilizator2.getText());
        try{
            service.addPrietenie(id_utilizator1, id_utilizator2);
            this.dialogStage.close();
        } catch (AppException e) {
            MessageAlert.showMessage(null, Alert.AlertType.ERROR, "Eroare", e.getMessage());
        }
    }
}
