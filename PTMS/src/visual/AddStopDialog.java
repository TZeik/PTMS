package visual;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import logic.PTMS;
import logic.Stop;

public class AddStopDialog extends Stage{
	
	static final int buttonWidth = 150;
	static final int buttonHeight = 28;
	
	private TextField labelField;
    private Button addButton;
    private Button cancelButton;

    public AddStopDialog(MainScreen app, Stop selectedStop) {
        setTitle("Agregar Parada");
        initModality(Modality.APPLICATION_MODAL);
        initStyle(StageStyle.UNDECORATED);
        
        labelField = new TextField();
        labelField.getStyleClass().add("text-field");
        addButton = new Button("Crear");
        cancelButton = new Button("Cancelar");
        
        addButton.setPrefHeight(buttonHeight);
        addButton.setPrefWidth(buttonWidth);
        cancelButton.setPrefHeight(buttonHeight);
        cancelButton.setPrefWidth(buttonWidth);
        
        // Set action for the Add button
        addButton.setOnAction(e -> {
        	selectedStop.setId(PTMS.getInstance().generateStopID());
        	selectedStop.setLabel(labelField.getText());
            app.addStop(selectedStop);
            close();
        });
        
        cancelButton.setOnAction(e -> {
            close();
        });
        
        // Layout
        VBox layout = new VBox(10);
        HBox mybuttons = new HBox(5);
        layout.setPadding(new Insets(10));
        layout.getChildren().addAll(
        	new Label("Nombre:"), labelField,
            mybuttons
        );
        mybuttons.getChildren().addAll(addButton, cancelButton);

        Scene scene = new Scene(layout, 300, 110);
        scene.getStylesheets().add(getClass().getResource("modal.css").toExternalForm());
        setScene(scene);
    }
}
