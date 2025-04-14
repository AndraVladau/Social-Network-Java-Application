package com.social.socialnetwork.Controller;

import com.social.socialnetwork.AppExceptions.AppException;
import com.social.socialnetwork.Domain.CererePrietenie;
import com.social.socialnetwork.Domain.Mesaj;
import com.social.socialnetwork.Domain.PrietenieDTO;
import com.social.socialnetwork.Domain.Utilizator;
import com.social.socialnetwork.Service.Service;
import com.social.socialnetwork.Utils.Events.ChangeEventType;
import com.social.socialnetwork.Utils.Events.ReplyEvent;
import com.social.socialnetwork.Utils.Events.ServiceChangeEvent;
import com.social.socialnetwork.Utils.Observer.Observable;
import com.social.socialnetwork.Utils.Observer.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.converter.IntegerStringConverter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;
import java.util.stream.StreamSupport;

public class UserController implements Observer<ServiceChangeEvent> {
    public Label numeCunoscut;
    public ListView<Utilizator> listaPrieteni;
    public ListView<Utilizator> listaUtilizatoriCereri;
    public ButtonBar baraButoane;
    public Button refuzaCererea;
    public Button acceptaCererea;
    public Button trimiteCerere;
    public ButtonBar baraPrietenButoane;
    public Label numePrieten;
    public GridPane gridSendMessage;
    private Service service;
    private Utilizator utilizatorLogat;
    public TextField idPrietenNou;
    public TextField mesajNou;
    public Button sendButton;
    public ScrollPane scrollPane;

    private Mesaj mesajLaCareSeDaReply;

    ObservableList<Utilizator> modelPrieteni = FXCollections.observableArrayList();
    ObservableList<Utilizator> modelUtilizatoriCereri = FXCollections.observableArrayList();
    public void initUserController(Service service, Utilizator utilizator){
        this.mesajLaCareSeDaReply = null;
        this.service = service;
        utilizatorLogat = utilizator;
        this.loadListe();
        this.service.addObserver(this);
        this.baraButoane.setVisible(false);
        this.baraPrietenButoane.setVisible(false);
        listaPrieteni.getSelectionModel().selectedItemProperty().addListener((observable -> {
            listaUtilizatoriCereri.getSelectionModel().clearSelection();
            this.baraButoane.setVisible(false);
            this.baraPrietenButoane.setVisible(true);
            this.gridSendMessage.setVisible(false);
            var utilizatorCunoscut = listaPrieteni.getSelectionModel().getSelectedItem();
            if(utilizatorCunoscut!=null){
                loadMesaje(utilizatorCunoscut);
            }
        }));


        listaUtilizatoriCereri.getSelectionModel().selectedItemProperty().addListener((observable -> {
            listaPrieteni.getSelectionModel().clearSelection();
            this.baraButoane.setVisible(true);
            this.gridSendMessage.setVisible(false);
            this.baraPrietenButoane.setVisible(false);
            var utilizatorCunoscut = listaUtilizatoriCereri.getSelectionModel().getSelectedItem();
            if(utilizatorCunoscut!=null){
                loadButoaneCereri(utilizatorCunoscut);
            }

        }));
    }

    private void loadButoaneCereri(Utilizator cunoscut){
        numeCunoscut.setText(cunoscut.getFirstName() + " " + cunoscut.getLastName());
        this.acceptaCererea.setDisable(false);
        this.refuzaCererea.setDisable(false);
        var lblNou = new Label("Nu poti trimite mesaj cat timpu nu esti prieten cu el!");
        var relatie = service.getRelatieBetween(utilizatorLogat.getId() , cunoscut.getId()).get();
        if(relatie.getId().getLeft().equals(utilizatorLogat.getId())){
            this.baraButoane.setVisible(false);
            if(relatie.getStatus() == CererePrietenie.REFUSED){
                this.numeCunoscut.setText("Cererea ta a fost refuzata");
                lblNou.setText("Cererea ta a fost refuzata");
            }
            else{
                this.numeCunoscut.setText("Cererea ta e in asteptare");
                lblNou.setText("Cererea ta e in asteptare");
            }
        }
        else{
            if(relatie.getStatus() == CererePrietenie.REFUSED){
                this.refuzaCererea.setDisable(true);
                this.numeCunoscut.setText("I-ai refuzat cererea lui " + cunoscut.getFirstName() + " " + cunoscut.getLastName());
                lblNou.setText("I-ai refuzat cererea lui " + cunoscut.getFirstName() + " " + cunoscut.getLastName());
            }
            else{
            }
        }
        this.scrollPane.setContent(lblNou);
    }


    private void loadListe(){
        listaPrieteni.setItems(modelPrieteni);
        listaUtilizatoriCereri.setItems(modelUtilizatoriCereri);
        this.reloadListe();
    }

    private void reloadListe(){
        var toateListele = this.service.cereriDePrietenie(utilizatorLogat.getId());
        if(toateListele.isEmpty()){
            return;
        }
        modelPrieteni.setAll(toateListele.get(1));
        modelUtilizatoriCereri.setAll(toateListele.get(2));
    }

    private void loadMesaje(Utilizator cunoscut){
        this.gridSendMessage.setVisible(true);
        this.numePrieten.setText(cunoscut.getFirstName() + " " + cunoscut.getLastName());

        var listaToateMesajele = this.service.getAllMessagesBetween(utilizatorLogat.getId(), cunoscut.getId());


        VBox vBox = new VBox();
        this.scrollPane.setContent(vBox);
        vBox.setPrefWidth(scrollPane.getPrefWidth());
        listaToateMesajele.forEach(mesaj->{
            try{
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("views/single_mesaj_view.fxml"));
                AnchorPane root = fxmlLoader.load();
                vBox.getChildren().add(root); // il adugi
                MesajViewController controllerMesaj = fxmlLoader.getController();
                controllerMesaj.initMesaj(this,mesaj, utilizatorLogat);
                root.setPrefWidth(vBox.getPrefWidth()); // sa fie cat mesajul de mare
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        scrollPane.setVvalue(1.0);
    }

    public void sendMesssage(ActionEvent actionEvent) {
        var cunoscut = listaPrieteni.getSelectionModel().getSelectedItem(); // se ia utilizatorul cu care se vorbeste

        var text_scris = this.mesajNou.getText(); // mesajul nou scris
        try{
            if(mesajLaCareSeDaReply == null) {
                this.service.sentNewMessage(utilizatorLogat.getId(), Collections.singletonList(cunoscut.getId()), text_scris, LocalDateTime.now());
            }
            else{
                this.service.sentNewMessage(utilizatorLogat.getId(), cunoscut.getId(), mesajLaCareSeDaReply.getId(), text_scris, LocalDateTime.now());
            }
            this.mesajNou.clear();
            this.mesajLaCareSeDaReply = null;
        }
        catch (AppException e){
            MessageAlert.showMessage(null, Alert.AlertType.ERROR, "", e.getMessage());
        }
    }

    @Override
    public void update(ServiceChangeEvent eventUpdate) {
        Long idCelalaltUser;
        if(Objects.equals(eventUpdate.getUser1(), utilizatorLogat.getId())){
            idCelalaltUser = eventUpdate.getUser2();
        }
        else if(Objects.equals(eventUpdate.getUser2(), utilizatorLogat.getId())){
            idCelalaltUser = eventUpdate.getUser1();
        }
        else {
            return;
        }
        var user = service.findOne(idCelalaltUser).get();
        this.baraButoane.setVisible(false);
        this.baraPrietenButoane.setVisible(false);
        if (eventUpdate.getType().equals(ChangeEventType.MESSAGES)) {
            var cunoscut = listaPrieteni.getSelectionModel().getSelectedItem();
            listaPrieteni.getSelectionModel().select(user);
            this.loadMesaje(user);
        } else if (eventUpdate.getType().equals(ChangeEventType.FRIENDS)) {
            this.reloadListe();
        }
    }

    public void handlerRefuzaCererea(ActionEvent actionEvent) {
        var cunoscut = listaUtilizatoriCereri.getSelectionModel().getSelectedItem();

        this.service.refuseCererePrietenie(cunoscut.getId(), utilizatorLogat.getId());
    }

    public void handlerAcceptaCererea(ActionEvent actionEvent) {
        var cunoscut = listaUtilizatoriCereri.getSelectionModel().getSelectedItem();

        this.service.acceptCererePrietenie(cunoscut.getId(), utilizatorLogat.getId());
    }

    public void handlerTrimiteCerere(ActionEvent actionEvent) {
        if(this.idPrietenNou.getText().isEmpty()) {
            MessageAlert.showMessage(null, Alert.AlertType.ERROR, "", "Campul id e gol");
            return;
        }
        var idViitorPrieten = Long.parseLong(this.idPrietenNou.getText());
        try{
            service.trimiteCererePrietenie(utilizatorLogat.getId(), idViitorPrieten);
            this.idPrietenNou.clear();
        }
        catch (AppException e){
            MessageAlert.showMessage(null, Alert.AlertType.ERROR, "", e.getMessage());
        }
    }

    public void setReply(Mesaj mesaj){
        mesajLaCareSeDaReply = mesaj;
    }
}