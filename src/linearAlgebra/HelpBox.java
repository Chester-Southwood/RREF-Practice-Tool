package linearAlgebra;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.geometry.*;

public class HelpBox
{
   public static void show()
   {
       Stage window = new Stage();
       window.initModality(Modality.APPLICATION_MODAL);
       window.setTitle("Help Menu");
       window.setMinWidth(500);
       window.setMinHeight(350);
      
       Label headerLabel = new Label();
       headerLabel.setText("Acceptable Operations");
       headerLabel.setStyle("-fx-font-weight: bold");
       
       Label scalarLabel = new Label();
       scalarLabel.setText("Multiply row by scalar: #r#. Example -1/2r3");
       
       Label addRowsLabel = new Label();
       addRowsLabel.setText("Add one row to another row: #r+#r, will set result to latter row. Example -1/4r2+r3");
       
       Label switchRowsLabel = new Label();
       switchRowsLabel.setText("Switch one row to another row: #r<->#r, will switch rows.");
       
       Label acceptCommandsLabel = new Label();
       acceptCommandsLabel.setText("Acceptable Commands");
       acceptCommandsLabel.setStyle("-fx-font-weight: bold");
       
       Label helpViewLabel = new Label();
       helpViewLabel.setText("View Help Menu: Enter help in terminal.");
       
       Label exitCommandLabel = new Label();
       exitCommandLabel.setText("Exit Program: Enter exit in terminal.");
       
       Label shortcutKeysLabel = new Label();
       shortcutKeysLabel.setText("Shortcut Keys");
       shortcutKeysLabel.setStyle("-fx-font-weight: bold");
       
       Label escapeKeyLabel = new Label();
       escapeKeyLabel.setText("Exit window: Press esc key.");
       
       Button closeButton = new Button("Close the window");
       closeButton.setOnAction(e -> window.close());
      
       VBox layout = new VBox(10);
       layout.getChildren().addAll(headerLabel, scalarLabel, 
    		                       addRowsLabel, switchRowsLabel,
    		                       acceptCommandsLabel, helpViewLabel, 
    		                       exitCommandLabel, shortcutKeysLabel,
    		                       escapeKeyLabel, closeButton);
       layout.setAlignment(Pos.CENTER);
      
       Scene scene = new Scene(layout);
	   scene.setOnKeyPressed(e -> {
		   if (e.getCode() == KeyCode.ESCAPE || e.getCode() == KeyCode.ENTER) {
		        window.close();
		   }
	   });
      
       window.setScene(scene);
       window.showAndWait();
   }
}
