module com.social.socialnetwork {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires java.sql;

    opens com.social.socialnetwork to javafx.fxml;
    exports com.social.socialnetwork;

    opens com.social.socialnetwork.Controller to javafx.fxml;
    exports com.social.socialnetwork.Controller;

    opens com.social.socialnetwork.Domain to java.base;
    exports com.social.socialnetwork.Domain;
}
