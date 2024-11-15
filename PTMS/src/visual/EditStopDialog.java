package visual;

import java.util.Optional;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import logic.PTMS;
import logic.Stop;

public class EditStopDialog extends Stage{
	
	private TextField idField;
	private TextField labelField;
    private Button saveButton;
    private Button moveButton;
    private Button deleteButton;
    private Button cancelButton;
    
    public EditStopDialog(MainScreen app) {
        setTitle("Parada " + app.selectedStop.getLabel());
        initModality(Modality.APPLICATION_MODAL);
        
        idField = new TextField(app.selectedStop.getId());
        labelField = new TextField(app.selectedStop.getLabel());
        saveButton = new Button("Guardar");
        moveButton = new Button("Mover");
        deleteButton = new Button("Eliminar");
        cancelButton = new Button("Cancelar");
        
        idField.setEditable(false);
        
        
        // Set action for save an updated stop
        saveButton.setOnAction(e -> {
        	Stop stop = new Stop(idField.getText(), labelField.getText());
            app.selectedStop = stop;
            close();
        });
        
        moveButton.setOnAction(e -> {
        	app.waitForUserAction(1);
            close();
        });
        
        deleteButton.setOnAction(e -> {
        	Alert alert = new Alert(AlertType.CONFIRMATION);
        	alert.setTitle("Eliminar " + app.selectedStop.getLabel());
        	alert.setHeaderText("¿Estás seguro de que quieres eliminar " + app.selectedStop.getLabel() + "?");
        	alert.setContentText("Esta acción no puede deshacerse");
        	ButtonType yesButton = new ButtonType("Sí");
        	ButtonType noButton = new ButtonType("No");
        	alert.getButtonTypes().setAll(yesButton, noButton);
        	
        	Optional<ButtonType> result = alert.showAndWait();
        	if(result.isPresent() && result.get() == yesButton) {
        		app.deleteStop(app.selectedStop);
        		close();
        	}
        });
        
        cancelButton.setOnAction(e -> {
            close();
        });
        
        // Layout
        VBox layout = new VBox(10);
        HBox mybuttons = new HBox(5);
        layout.setPadding(new Insets(10));
        layout.getChildren().addAll(
        	new Label("ID:"), idField,
        	new Label("Nombre:"), labelField,
            mybuttons
        );
        mybuttons.getChildren().addAll(saveButton, moveButton, deleteButton, cancelButton);

        Scene scene = new Scene(layout, 300, 180);
        setScene(scene);
    }
	
}
