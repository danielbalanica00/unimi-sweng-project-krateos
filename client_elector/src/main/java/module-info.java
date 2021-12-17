module com.simpolab.client_elector {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.simpolab.client_elector to javafx.fxml;
    exports com.simpolab.client_elector;
}