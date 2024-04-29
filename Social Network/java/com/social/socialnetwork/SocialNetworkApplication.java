package com.social.socialnetwork;

import com.social.socialnetwork.Controller.SocialNetworkController;
import com.social.socialnetwork.Domain.Prietenie;
import com.social.socialnetwork.Domain.Tuplu;
import com.social.socialnetwork.Domain.Utilizator;
import com.social.socialnetwork.Repository.*;
import com.social.socialnetwork.Repository.Paging.IPagingRepository;
import com.social.socialnetwork.Service.Service;
import com.social.socialnetwork.Validators.ValidatorStrategies;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TableRow;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;

public class SocialNetworkApplication extends Application{
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("views/socialnetwork-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 450);
        intializeStructure(fxmlLoader); /// initializam elementele din arhitectura stratificata
        clearSelectionClickOutside(fxmlLoader, scene);
        stage.setTitle("Social Network");
        stage.setScene(scene);
        stage.show();
    }
    private void clearSelectionClickOutside(FXMLLoader fxmlLoader, Scene scene){
        SocialNetworkController controller = fxmlLoader.getController();
        scene.addEventFilter(MouseEvent.MOUSE_CLICKED, evt -> {
            Node source = evt.getPickResult().getIntersectedNode();
            while (source != null && !(source instanceof TableRow)) {
                source = source.getParent();
            }
            if (source == null || (source instanceof TableRow && ((TableRow) source).isEmpty())) {
                controller.clearSelectionMainTable();
            }
        });
    }


    private void intializeStructure(FXMLLoader fxmlLoader){
        DBConnection dbConnection = new DBConnection();

        UtilizatorDBRepository userDBRepository = new UtilizatorDBRepository(dbConnection, ValidatorStrategies.UTILIZATOR);

        Repository<Tuplu<Long, Long>, Prietenie> prietenieDBRepository = new PrietenieDBRepository(dbConnection, ValidatorStrategies.PRIETENIE);
        CereriPrieteniiDBRepository repositoryCereriPrietenii = new CereriPrieteniiDBRepository(dbConnection, ValidatorStrategies.CEREREPRIETENIE);
        MesajeDBRepository mesajDBRepository = new MesajeDBRepository(dbConnection);
        Service serviceApp = new Service(userDBRepository, repositoryCereriPrietenii, prietenieDBRepository, mesajDBRepository, ValidatorStrategies.UTILIZATOR, ValidatorStrategies.PRIETENIE);

        SocialNetworkController socialNetworkController = fxmlLoader.getController();
        socialNetworkController.setServiceSocialNetwork(serviceApp);

    }
    public static void main(String[] args) {
        launch();
    }






    public static void tests(){
        ValidatorStrategies validatorStrategies = ValidatorStrategies.UTILIZATOR;
        ValidatorStrategies validatorPrietenieStrategie = ValidatorStrategies.PRIETENIE;
        DBConnection dbConnection = new DBConnection();
        UtilizatorDBRepository userDBRepository = new UtilizatorDBRepository(dbConnection, ValidatorStrategies.UTILIZATOR);
        Repository<Tuplu<Long, Long>, Prietenie> prietenieDBRepository = new PrietenieDBRepository(dbConnection, ValidatorStrategies.PRIETENIE);
        MesajeDBRepository mesajDBRepository = new MesajeDBRepository(dbConnection);
        CereriPrieteniiDBRepository repositoryCereriPrietenii = new CereriPrieteniiDBRepository(dbConnection, ValidatorStrategies.CEREREPRIETENIE);
        Service serviceApp = new Service(userDBRepository, repositoryCereriPrietenii, prietenieDBRepository, mesajDBRepository, validatorStrategies, validatorPrietenieStrategie);
        System.out.println(serviceApp.getAllMessages());
        System.out.println(serviceApp.getAllMessages());
    }


}