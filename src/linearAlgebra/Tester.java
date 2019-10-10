package linearAlgebra;
import java.io.FileNotFoundException;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import mathRepresentation.Fraction;
import stringUtils.StringMethods;
import javafx.geometry.Insets;

public class Tester extends Application
{
   private TextArea  output = new TextArea();
   private TextField input  = new TextField();
   private Matrix    matrix;
   private int       rowOperationCount;
   
   private Parent createContent()
   {
	  rowOperationCount = 0;
	  output.setEditable(false);
	  output.setStyle("-fx-opacity: 1;");
	  output.setPrefHeight(600-80);
	  
	  VBox root = new VBox(15, output, input);
	  root.setPadding(new Insets(15));
	  root.setPrefSize(800, 600);
	  
	  input.setOnAction(e -> {
		  String inputText = input.getText();
	      input.clear();
	      onInput(inputText);
	  });
	  
	  try
	  {
		  this.matrix = new Matrix("matrix.txt");
	  }
	  catch(FileNotFoundException e)
	  {
		  AlertBox.display("matrix.txt not found.\nPlease place in same directory level as jar file");
		  System.exit(200);
	  }
      println("Operation Number: " + rowOperationCount);
	  println(matrix.getMatrixString());

	  return root;
   }
   
   @Override
   public void start(Stage stage) throws Exception
   {
      stage.setScene(new Scene(createContent()));
      stage.show();
      HelpBox.show();
   }
   
   private void onInput(String line) 
   {
		String userInput = line.toLowerCase().trim();
		if(userInput.contains("<->")) //<r3 <-> r2
		{
			switchRows(userInput, matrix);
		}
		else if(userInput.contains("+")) //r1+r1, r1+r2, 3r1+r2, 1/2r1+r2, 1/2r1+-1/3r2
		{
			addRows(userInput, matrix);
		}
		else if(userInput.contains("r")) //3r3 , 1/2r3
		{
			multiplyByScalar(userInput, matrix);
		}
		else if(userInput.equals("help"))
		{
			HelpBox.show();
		}
		else if(userInput.equals("exit"))
		{
			System.exit(0);
		}
		else
		{
			AlertBox.display("Invalid Command, please try again");
		}
   }
    
   private void println(String line) {
	   output.appendText(line + "\n");
   }
   
   public static class Launcher
   {
      public static void main(String[] args)
      {
         Application.launch(Tester.class, args);
      }
   }
   
   public void switchRows(final String userInput, Matrix matrix)
	{
		String[] components = StringMethods.removeEmptyElements(userInput.split("<->"));

		if(components.length != 2)
		{
			AlertBox.display("ERROR: Must contain two respective rows on both sides of the doublesided arrow.");
		}
		else
		{
			if(components[0].charAt(0) != 'r')
			{
				AlertBox.display("ERROR: Left side of double arrow does not contain row indicator letter.");
			}
			else if(components[1].charAt(0) != 'r')
			{
				AlertBox.display("ERROR: Right side of double arrow does not contain row indicator letter.");						
			}
			else
			{
				String rowOneStr = components[0].trim();
				String rowTwoStr = components[1].trim();
				if(rowOneStr.isEmpty() || !StringMethods.canParseStringToInt(rowOneStr.substring(1)))
				{
					AlertBox.display("ERROR: Left side of arrow does not contain row number.");
				}
				else if(rowTwoStr.isEmpty() || !StringMethods.canParseStringToInt(rowTwoStr.substring(1)))
				{
					AlertBox.display("ERROR: Right side of arrow does not contain row number.");
				}
				else
				{
					int rowOneInt = Integer.parseInt(rowOneStr.substring(1));
					int rowTwoInt = Integer.parseInt(rowTwoStr.substring(1));
					
					if(rowOneInt < 1 || rowOneInt > matrix.getRowSize())
					{
						AlertBox.display("ERROR: Left side of arrow does not contain a VALID row number.");
					}
					else if(rowTwoInt < 1 || rowTwoInt > matrix.getRowSize())
					{
						AlertBox.display("ERROR: Right side of arrow does not contain a VALID row number.");								
					}
					else
					{
						matrix.swapRows(rowOneInt - 1, rowTwoInt - 1);
						println("\n" + "Operation Number: " + ++rowOperationCount);
						println("Row Operation: " + userInput);						
						println(matrix.getMatrixString());
					}
				}
			}
		}
	}
	
	public void multiplyByScalar(final String userInput, Matrix matrix)
	{
		String[] components   = userInput.split("r");
		if(components.length == 0 || components.length == 1)
		{
			AlertBox.display("ERROR: Input must contain a row index.");
		}
		else
		{
			String   scalarString = components[0];
			try
			{
				int rowNum        = Integer.parseInt(components[1]);
				if(!Fraction.canFraction(scalarString))
				{
					AlertBox.display("ERROR: Input contains invalid scalar value. Must be in form of whole number or fraction.");
				}
				else if(rowNum <= 0 || rowNum > matrix.getRowSize())
				{
					AlertBox.display("ERROR: Input contains invalid size of row attempting to scale.");
				}
				else
				{
					matrix.scaleRow(rowNum - 1, scalarString);
					println("\n" + "Operation Number: " + ++rowOperationCount);
					println("Row Operation: " + userInput);					
					println(matrix.getMatrixString());
				}
			}
			catch(NumberFormatException e)
			{
				AlertBox.display("ERROR: Input contains invalid characters for row.");
			}
		}
	}
	
	//Bane of existence... ugly method
	public void addRows(final String userInput, Matrix matrix)
	{
		String[]   components            = userInput.split("\\+");
		
		if(validateComponents(components))
		{
			String[]   fractionOneComponents = components[0].split("r");
			if(fractionOneComponents.length != 2)
			{
				AlertBox.display("ERROR: Empty row number on the left side of + operator");
			}
			else if(!StringMethods.canParseStringToInt(fractionOneComponents[1]))
			{
				AlertBox.display("ERROR: Invalid row number on the left side of + operator");
			}
			else
			{
				int        rowIndexOne 		     = Integer.parseInt(fractionOneComponents[1]);
				if(rowIndexOne < 1 || rowIndexOne > matrix.getRowSize())
				{
					AlertBox.display("ERROR: Invalid Row Size on the left side of the + operator");
				}
				else
				{
					String     scalarOne             = determineScalarOption(fractionOneComponents);
					if(!scalarOne.isEmpty())
					{
						if(!Fraction.canFraction(scalarOne))
						{
							AlertBox.display("ERROR: Invalid Scalar value on left side of the + opeartor");
							return;
						}
					}
					
					String[]   fractionTwoComponents = components[1].split("r");
					if(fractionTwoComponents.length != 2)
					{
						AlertBox.display("ERROR: Empty row number on the right side of + operator");
					}
					else if(!StringMethods.canParseStringToInt(fractionTwoComponents[1]))
					{
						AlertBox.display("ERROR: Invalid row number on the right side of + operator");
					}
					else
					{
						int        rowIndexTwo 		     = Integer.parseInt(fractionTwoComponents[1]);
						if(rowIndexTwo < 1 || rowIndexTwo > matrix.getRowSize())
						{
							AlertBox.display("ERROR: Invalid Row Size on the right side of the + operator");
						}
						else
						{
							String     scalarTwo             = determineScalarOption(fractionTwoComponents);
							if(!scalarTwo.isEmpty())
							{
								if(!Fraction.canFraction(scalarTwo))
								{
									AlertBox.display("ERROR: Invalid Scalar value on right side of the + opeartor");
									return;
								}
							}
					
							Fraction[] scaledRowOne          = matrix.peekScaleRow(rowIndexOne, scalarOne),
							           scaledRowTwo          = matrix.peekScaleRow(rowIndexTwo, scalarTwo);
							
							Fraction[] sumScaleRow           = new Fraction[scaledRowOne.length];
							
							for(int index = 0; index < sumScaleRow.length; index++)
							{
								sumScaleRow[index] = scaledRowOne[index].add(scaledRowTwo[index]);
							}
							
							matrix.setRow(rowIndexTwo, sumScaleRow);
							println("\n" + "Operation Number: " + ++rowOperationCount);
							println("Row Operation: " + userInput);
							println(matrix.getMatrixString());
						}

					}
				}
			}
		}
	}
	
	public boolean validateComponents(final String[] components)
	{
		boolean result;
		String  alertMsg = "";
		if(components.length != 2)
		{
			alertMsg = "Valid rows (2) must be placed on respective side of plus operator.";
			result = false;
		}
		else if(!components[0].contains("r"))
		{
			alertMsg = "Left side of + operator does not contain a row operator (r)";
			result = false;
		}
		else if(!components[1].contains("r"))
		{
			alertMsg = "Right side of + operator does not contain a row operator (r)";
			result = false;
		}
		else
		{
			result = true;
		}
		if(!result)
		{
			AlertBox.display(alertMsg);
		}
		return result;
	}
	
	public String determineScalarOption(String[] fractionComponents)
	{
		return fractionComponents[0].isEmpty() ? "1" : (fractionComponents[0].contentEquals("-") ? "-1" : fractionComponents[0]);
	}

	public void menu()
	{
		System.out.print("Input --> ");
	}
}