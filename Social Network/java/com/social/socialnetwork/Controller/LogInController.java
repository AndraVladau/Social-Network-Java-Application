package com.social.socialnetwork.Controller;

import com.social.socialnetwork.AppExceptions.ServiceException;
import com.social.socialnetwork.Domain.AES;
import com.social.socialnetwork.Domain.Utilizator;
import com.social.socialnetwork.Service.Service;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;

import com.social.socialnetwork.Service.Service;


public class LogInController {

    @FXML
    TextField usernameField;
    @FXML
    PasswordField passwordField;
    Service service;
    HashMap<Long, Node> listaUseriLogati;
    @FXML
    Stage stage;


    public void initLogInController(Service serviceSocialNetwork, Stage stage,HashMap<Long, Node> l) {
        this.service = serviceSocialNetwork;
        this.listaUseriLogati = l;
        this.stage=stage;
    }


    public void OnLogInAction(ActionEvent actionEvent) throws Exception {
        System.out.println("LOG IN BUTTON CLICKED");
        System.out.println(usernameField.getText());
        System.out.println(passwordField.getText());
        String username = usernameField.getText();
        String password = passwordField.getText();
        AES aes = AES.getInstance();
        byte[] hashedPasswordArrayByte = aes.encryptMessage(password.getBytes());
        String hasedPassword = Base64.getEncoder().encodeToString(hashedPasswordArrayByte);
        System.out.println(hasedPassword);

        try {
            Utilizator utilizator = service.findOnesAccount(username, hasedPassword);
            stage.close();
            showUserChatRoom(utilizator);

        } catch (ServiceException e) {
            MessageAlert.showMessage(null, Alert.AlertType.ERROR, "", e.getMessage());

        }


    }

    private void showUserChatRoom(Utilizator utilizator) throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("views/user_view.fxml"));
            AnchorPane root = fxmlLoader.load();
            Stage userStage = new Stage();
            userStage.setTitle(utilizator.getFirstName());
            this.listaUseriLogati.put(utilizator.getId(), root);
            Scene scene = new Scene(root);
            userStage.setScene(scene);
            userStage.setOnCloseRequest(event -> {
//              System.out.println("Close button was clicked!");
                listaUseriLogati.remove(utilizator.getId());
            });
            UserController userController = fxmlLoader.getController();
            userController.initUserController(service, utilizator);
            userStage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

}
