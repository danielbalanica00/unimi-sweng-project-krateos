module com.simpolab.client_elector {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;
    requires org.apache.httpcomponents.core5.httpcore5;
    requires org.apache.httpcomponents.client5.httpclient5;

    opens com.simpolab.client_elector to javafx.fxml;
    opens com.simpolab.client_elector.login to javafx.fxml;
    exports com.simpolab.client_elector;
}