package linearAlgebra;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.geometry.*;
public class AlertBox
{
   public static void display(final String message)
   {
       Stage window = new Stage();
       window.initModality(Modality.APPLICATION_MODAL);
       window.setTitle("Error Occured");
       window.setMinWidth(275);
       window.setMinHeight(100);
      
       Label label = new Label();
       label.setText(message);
      
       VBox layout = new VBox(10);
       layout.getChildren().addAll(label);
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