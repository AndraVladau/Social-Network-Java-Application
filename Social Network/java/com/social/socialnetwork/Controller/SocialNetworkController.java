package com.social.socialnetwork.Controller;

import com.social.socialnetwork.AppExceptions.AppException;
import com.social.socialnetwork.Domain.*;
import com.social.socialnetwork.Repository.Paging.Page;
import com.social.socialnetwork.Repository.Paging.Pageable;
import com.social.socialnetwork.Service.Service;
import com.social.socialnetwork.Utils.Events.ServiceChangeEvent;
import com.social.socialnetwork.Utils.Observer.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.stream.StreamSupport;

public class SocialNetworkController implements Observer<ServiceChangeEvent> {

    public Button loginUser;
    private Service serviceSocialNetwork;

    ObservableList<Utilizator> model = FXCollections.observableArrayList();
    ObservableList<PrietenieDTO> modelPrieteni = FXCollections.observableArrayList();

    HashMap<Long, Node> listaUseriLogati;

    @FXML
    TableView<Utilizator> utilizatorTableView;
    @FXML
    TableColumn<Utilizator, Long> columnID;
    @FXML
    TableColumn<Utilizator, String> columnFirstName;
    @FXML
    TableColumn<Utilizator, String> columnLastName;
    @FXML
    TableColumn<Utilizator,String> columnUsername;

    @FXML
    Button previousButton;

    @FXML
    Button nextButton;

    private int currentPage=0;
    private int numberOfRecordsPerPage = 5;

    private int totalNumberOfElements;
    @FXML
    HBox hBoxTables;
    @FXML
    ScrollBar numberOfElements;

    public SocialNetworkController() throws Exception {
    }

    @Override
    public void update(ServiceChangeEvent eventUpdate) {
        initData();
    }

    public void setServiceSocialNetwork(Service serviceSocialNetwork) {
        this.serviceSocialNetwork = serviceSocialNetwork;
        serviceSocialNetwork.addObserver(this);
        initData();

    }

    public void initialize() {
        listaUseriLogati = new HashMap<>();
        columnID.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        columnLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        columnUsername.setCellValueFactory(new PropertyValueFactory<>("username"));


        utilizatorTableView.setItems(model);
        utilizatorTableView.getSelectionModel().selectedItemProperty().addListener((observable -> {
            var utilizator = utilizatorTableView.getSelectionModel().getSelectedItem();
            if(utilizator == null) {
                utilizatorTableView.setPrefHeight(hBoxTables.getHeight());
                utilizatorTableView.setPrefWidth(hBoxTables.getWidth());
            }
            else {
                utilizatorTableView.setPrefHeight(hBoxTables.getHeight()/2);
                utilizatorTableView.setPrefWidth(hBoxTables.getWidth()/2);
                reloadFriendsModel(utilizator.getId());
            }
        }));
    }

    public void handleAddUtilizator(ActionEvent actionEvent){
        this.showUtilizatorEditDialog(null);
    }

    private void showUtilizatorEditDialog(Utilizator utilizator) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("views/edituser_view.fxml"));

            AnchorPane root = fxmlLoader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit User");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            EditUtilizatorController editUtilizatorController = fxmlLoader.getController();
            editUtilizatorController.setService(serviceSocialNetwork, dialogStage, utilizator);
            dialogStage.show();
        }
        catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    private void showPrieteniAddDialog(Utilizator utilizator) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("views/editprieteni_view.fxml"));
            AnchorPane root = fxmlLoader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Add prietenie");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            EditPrieteniController editPrieteniController = fxmlLoader.getController();
            editPrieteniController.setService(serviceSocialNetwork, dialogStage, utilizator);
            dialogStage.show();
        }
        catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    public void handleUpdateUtilizator(ActionEvent actionEvent){
        Utilizator utilizator = utilizatorTableView.getSelectionModel().getSelectedItem();
        if(utilizator == null){
            MessageAlert.showMessage(null, Alert.AlertType.ERROR, "Eroare", "Nu ai selectat niciun student");
            return;
        }
        showUtilizatorEditDialog(utilizator);
    }

    public void handleDeleteUtilizator(ActionEvent actionEvent){
        Utilizator utilizator = utilizatorTableView.getSelectionModel().getSelectedItem();
        if(utilizator == null){
            MessageAlert.showMessage(null, Alert.AlertType.ERROR, "Eroare", "Nu ai selectat niciun student");
            return;
        }
        try {
            serviceSocialNetwork.deleteUtilizator(utilizator.getId());
            MessageAlert.showMessage(null, Alert.AlertType.CONFIRMATION, "", "");
        }
        catch (AppException appException){
            MessageAlert.showMessage(null, Alert.AlertType.CONFIRMATION, "", appException.getMessage());
        }
    }

    private void reloadFriendsModel(Long idUser){
        Iterable<PrietenieDTO> listaPrieteni = serviceSocialNetwork.relatiiDePrietenie(idUser);
        List<PrietenieDTO> listaDTO = StreamSupport.stream(listaPrieteni.spliterator(), false).toList();
        modelPrieteni.setAll(listaDTO);
    }

    private void initModel(){
        Iterable<Utilizator> listaUsers = serviceSocialNetwork.getAllUtilizatori();
        List<Utilizator> utilizatorList = StreamSupport.stream(listaUsers.spliterator(), false).toList();
        model.setAll(utilizatorList);
        this.utilizatorTableView.getSelectionModel().clearSelection();
    }


    public void clearSelectionMainTable(){
        this.utilizatorTableView.getSelectionModel().clearSelection();
    }

    private void reloadColumns(){
        columnID.setPrefWidth(utilizatorTableView.getWidth()/5);
        columnLastName.setPrefWidth(2* utilizatorTableView.getWidth()/5);
        columnFirstName.setPrefWidth(2 * utilizatorTableView.getWidth()/5);
    }

    public void handleDeletePrietenie(ActionEvent actionEvent) {
//        PrietenieDTO prietenieDTO = prieteniTableView.getSelectionModel().getSelectedItem();
        PrietenieDTO prietenieDTO = null;
        if(prietenieDTO==null){
            MessageAlert.showMessage(null, Alert.AlertType.ERROR, "Eroare", "Nu ai selectat niciun student");
            return;
        }
        try{
            serviceSocialNetwork.deletePrietenie(new Tuplu<>(prietenieDTO.getId1(), prietenieDTO.getId2()));
        }
        catch (AppException e){
            MessageAlert.showMessage(null, Alert.AlertType.ERROR, "Eroare", e.getMessage());
        }
    }

    public void handleAddPrietenie(ActionEvent actionEvent){
        var utilizator = utilizatorTableView.getSelectionModel().getSelectedItem();
        if(utilizator==null){
            MessageAlert.showMessage(null, Alert.AlertType.ERROR, "Eroare", "Nu ai selectat niciun student");
            return;
        }
        showPrieteniAddDialog(utilizator);
    }
    public void handleLoginUser(ActionEvent actionEvent) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("views/login_view.fxml"));
            AnchorPane root = fxmlLoader.load();
            Stage userStage = new Stage();
            userStage.setTitle("LOG AS");

            Scene scene = new Scene(root);
            userStage.setScene(scene);
            LogInController logInController = fxmlLoader.getController();
            logInController.initLogInController(serviceSocialNetwork,userStage,listaUseriLogati);
            userStage.show();
        }
        catch (IOException e){
            System.out.println(e.getMessage());
        }
    }


    private void handlePageNavigationChecks(){
        previousButton.setDisable(currentPage ==0);
        nextButton.setDisable((currentPage+1)*numberOfRecordsPerPage >= totalNumberOfElements);
    }


    @FXML
    private void handleNumberOfElementsOnPage(){
        this.numberOfRecordsPerPage = (int) numberOfElements.getValue();
        initData();
    }



    public void initData(){
        Page<Utilizator> moviesOnCurrentPage = serviceSocialNetwork.getUsersOnPage(new Pageable(currentPage, numberOfRecordsPerPage));
        totalNumberOfElements = moviesOnCurrentPage.getTotalNumberOfElements();
        List<Utilizator> userList = StreamSupport.stream(moviesOnCurrentPage.getElementsOnPage().spliterator(), false).toList();

        model.setAll(userList);
        handlePageNavigationChecks();
        this.utilizatorTableView.getSelectionModel().clearSelection();
    }

    public void goToNextPage(ActionEvent actionEvent) {
        System.out.println("NEXT PAGE");
        currentPage++;
        initData();

    }

    public void goToPreviousPage(ActionEvent actionEvent) {
        System.out.println("PREVIOUS PAGE");
        currentPage--;
        initData();
    }
}
