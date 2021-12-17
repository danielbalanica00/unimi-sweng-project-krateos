module com.simpolab.client_manager {
    requires javafx.controls;
    requires javafx.fxml;
            
                        
    opens com.simpolab.client_manager to javafx.fxml;
    exports com.simpolab.client_manager;
}