package com.social.socialnetwork.Controller;

import com.social.socialnetwork.AppExceptions.AppException;
import com.social.socialnetwork.Domain.AES;
import com.social.socialnetwork.Domain.Utilizator;
import com.social.socialnetwork.Service.Service;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Base64;

public class EditUtilizatorController {
    @FXML
    TextField idTextField;
    @FXML
    TextField prenumeTextField;
    @FXML
    TextField numeTextField;
    @FXML
    TextField usernameField;
    @FXML
    PasswordField passwordField;


    @FXML
    Button saveButton;

    @FXML
    Button cancelButton;

    private Service service;
    private Stage dialogStage;

    private Utilizator utilizator;

    void setService(Service service, Stage dialogStage, Utilizator utilizator){
        this.service = service;
        this.dialogStage = dialogStage;
        this.utilizator = utilizator;
        this.idTextField.setEditable(false);
        if(this.utilizator != null){
            loadFields(utilizator);
            this.saveButton.setText("Update");
        }
        else{
            this.idTextField.setText("Will be generated");
            this.prenumeTextField.setPromptText("INTRODU PRENUME");
            this.numeTextField.setPromptText("INTRODU NUME");
        }
    }

    private void loadFields(Utilizator utilizator) {
        this.idTextField.setText(utilizator.getId().toString());
        this.prenumeTextField.setText(utilizator.getFirstName());
        this.numeTextField.setText(utilizator.getLastName());
    }

    public void handlerCancel(ActionEvent actionEvent) {
        this.dialogStage.close();
    }

    public void handlerSave(ActionEvent actionEvent) {
        String prenume = this.prenumeTextField.getText();
        String nume = this.numeTextField.getText();
        String username=this.usernameField.getText();
        String password=this.passwordField.getText();


        if(utilizator==null) {
            try {

                this.service.addNewUser(nume, prenume,username,password);
                MessageAlert.showMessage(dialogStage, Alert.AlertType.CONFIRMATION, "", "A mers!");
                dialogStage.close(); //sa nu mai dea save de mai multe ori sau cv
            } catch (AppException e) {
                MessageAlert.showMessage(dialogStage, Alert.AlertType.ERROR, "EROARE", e.getMessage());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        else{
            try{
                this.service.updateUser(utilizator.getId(), nume, prenume);
                MessageAlert.showMessage(dialogStage, Alert.AlertType.CONFIRMATION, "", "A mers!");
                dialogStage.close();
            }catch (AppException e) {
                MessageAlert.showMessage(dialogStage, Alert.AlertType.ERROR, "EROARE", e.getMessage());
            }
        }
    }

}
