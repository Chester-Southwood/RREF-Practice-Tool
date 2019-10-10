package linearAlgebra;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import mathRepresentation.Fraction;

public class Matrix
{
	//Fields
	private Fraction[][] matrix;
	
	//EVC
	public Matrix(String fileName) throws FileNotFoundException //Need to do better, terrible solution at moment
	{
		matrix = fileToFractionArray(fileName);
	}
	
	public Matrix(Fraction[][] matrix)
	{
		this.matrix = matrix;
	}
	
	//Getters and Setters
	public void setRow(final int rowIndex, final Fraction[] row)
	{
		matrix[rowIndex - 1] = row;
	}
	
	public int getRowSize()
	{
		return this.matrix.length;
	}
	
	public int getColumnSize()
	{
		return this.matrix[0].length;
	}
	
	public String getMatrixString(Fraction[][] matrix)
	{
		String matrixString = "";
		
		for(int rowIndex = 0; rowIndex < matrix.length; rowIndex++)
		{
			String rowString = "[";
			
			for(int columnIndex = 0; columnIndex < matrix[0].length; columnIndex++)
			{
				int spacingSize =  columnIndex == 0 ? 6 : 10;
				rowString       += String.format("%" + spacingSize + "s", matrix[rowIndex][columnIndex]);
			}
			
			rowString    += "]";
			matrixString += rowIndex != matrix.length - 1 ? rowString + "\n" : rowString;
		}
		
		return matrixString;
	}
	
	public Fraction[][] getFractionalCopy()
	{
		Fraction[][] fracCopy = new Fraction[matrix.length][matrix[0].length];
		for(int row = 0; row < matrix.length; row++)
		{
			for(int column = 0; column < matrix[0].length; column++)
			{
				fracCopy[row][column] = new Fraction(matrix[row][column] + "");
			}
		}
		return fracCopy;
	}
	
	//Non-Instance Methods
	private static Fraction[][] fileToFractionArray(String fileName) throws FileNotFoundException
	{
		File file        = new File(fileName);
		if(!file.exists())
		{
			throw new FileNotFoundException("File does not exist");
		}
		Fraction[][] arr = null;
		
		try
		{
			Scanner  sc              = new Scanner(file);
			String[] firstLine       = sc.nextLine().split(" ");
			
			int      rowSize         = Integer.parseInt(firstLine[0]),
				      columnSize      = Integer.parseInt(firstLine[1]);
             
			arr = new Fraction[rowSize][columnSize];
			
			for(int rowIndex = 0; rowIndex < rowSize; rowIndex++)
			{
				String[] rowElements = sc.nextLine().split(" ");
				
				for(int columnIndex = 0; columnIndex < columnSize; columnIndex++)
				{
					arr[rowIndex][columnIndex] = new Fraction(rowElements[columnIndex]);
				}
			}
			
			sc.close();
		} 
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
			arr = new Fraction[0][0];
			//TODO 
			//Not sure if ok idea...
		}
		return arr;
	}
	
	//Instance Methods	
	public boolean isInRowRange(final int value)
	{
		return 1 <= value && value <= matrix.length;
	}
	
	public boolean isInColumnRange(final int value)
	{
		return 1 <= value && value <= matrix[0].length;
	}
	
	public Matrix dotProduct(final Matrix passedInMatrix) //thisMatrix O otherMatrix
	{
		Fraction[][] passedInMatrixFrac = passedInMatrix.getFractionalCopy();
		if(matrix[0].length != passedInMatrixFrac.length)
		{
			throw new IllegalArgumentException("Cannot dot product matrices without the same column number x same row number");
		}
		else
		{
			Fraction[][] dotMatrix = new Fraction[matrix.length][passedInMatrixFrac[0].length];
			for(int dotRow = 0; dotRow < dotMatrix.length; dotRow++)
			{
				for(int dotColumn = 0; dotColumn < dotMatrix[0].length; dotColumn++)
				{
					Fraction sum = new Fraction("0");
					
					for(int counter = 0; counter < this.matrix[0].length; counter++)
					{
						sum = sum.add(this.matrix[dotRow][counter].multiply(passedInMatrixFrac[counter][dotColumn]));
					}
					
					dotMatrix[dotRow][dotColumn] = sum;
				}
			}
			return new Matrix(dotMatrix);
		}
	}
	
	public void swapRows(int rowOne, int rowTwo)
	{
		Fraction[] tempRow = matrix[rowOne];
		matrix[rowOne]     = matrix[rowTwo];
		matrix[rowTwo]     = tempRow;
	}
	
	public void scaleRow(final int rowIndex, final String scalarString)
	{
		Fraction scalarFraction = new Fraction(scalarString);
		Fraction[] scaledRow    = matrix[rowIndex];
		
		for(int index = 0; index < scaledRow.length; index++)
		{
			scaledRow[index] = scaledRow[index].multiply(scalarFraction);
		}
	}
	
	public Fraction[] peekScaleRow(final int rowIndex, final String scalarString)
	{
		Fraction scalarFraction = new Fraction(scalarString);
		Fraction[] scaledRow    = new Fraction[matrix[rowIndex - 1].length];
		
		for(int index = 0; index < matrix[rowIndex - 1].length; index++)
		{
			scaledRow[index] = matrix[rowIndex - 1][index].multiply(scalarFraction);
		}
		
		return scaledRow;
	}
	
	public void addRow(final int rowOneIndex, final int rowTwoIndex, int rowDestinationIndex)
	{
		Fraction[] addedRow = new Fraction[matrix[rowOneIndex].length];
		
		for(int index = 0; index < matrix[rowOneIndex].length; index++)
		{
			addedRow[index] = matrix[rowOneIndex][index].add(matrix[rowTwoIndex][index]);
		}
		
		this.setRow(rowDestinationIndex, addedRow);
	}
	
	public Fraction[] peekAddRow(final int rowOneIndex, final int rowTwoIndex)
	{
		Fraction[] addedRow = new Fraction[matrix[rowOneIndex].length];
		
		for(int index = 0; index < matrix[rowOneIndex].length; index++)
		{
			addedRow[index] = matrix[rowOneIndex][index].add(matrix[rowTwoIndex][index]);
		}
		
		return addedRow;
	}
	
	
	public Fraction calculateDeterminate()
	{
		if(matrix == null)
		{
			throw new IllegalStateException("Matrix has not yet been initilized");
		}
		else if(matrix.length != matrix[0].length)
		{
			throw new IllegalStateException("Cannot find determinate of a non n x n matrix");
		}
		else if(matrix.length == 1)
		{
			return matrix[0][0];
		}
		else if(matrix.length == 2)
		{
			return calculateTwoByTwoDeterminate(matrix);
		}
		else
		{
			//TODO //only works for 3 x 3, needs recursion for 4n + 1
			Fraction total = new Fraction("0");
			for(int index = 0; index < matrix.length; index++)
			{
				Fraction pivotValue = matrix[0][index];
				if(index % 2 != 0)
				{
					pivotValue = pivotValue.multiply(new Fraction("-1"));
				}
				pivotValue = pivotValue.multiply(calculateTwoByTwoDeterminate(generateSubMatrix(0, 0, index)));
				total = total.add(pivotValue);
			}
			return total;
		}
	}
	
	public Fraction[][] generateSubMatrix(final int pivotRow, final int notToRow, final int notToColumn)
	{
		Fraction[][] subMatrix = new Fraction[matrix.length - (pivotRow + 1)][matrix.length - (pivotRow + 1)];
		for(int rowIndex = pivotRow + 1, subRowIndex = 0; rowIndex < matrix.length; rowIndex++)
		{
			for(int columnIndex = 0, subColumnIndex = 0; columnIndex < matrix.length; columnIndex++)
			{
				if(rowIndex != notToRow && columnIndex != notToColumn)
				{
					subMatrix[subRowIndex][subColumnIndex] = matrix[rowIndex][columnIndex];
					subColumnIndex++;
				}
			}
			subRowIndex++;
		}
		return subMatrix;
	}
	
	private Fraction calculateTwoByTwoDeterminate(Fraction[][] arr)
	{
		return arr[0][0].multiply(arr[1][1]).add((new Fraction("-1")).multiply(arr[0][1].multiply(arr[1][0])));
	}
	
	public String getMatrixString()
	{
		return getMatrixString(matrix);
	}
	
	@Override
	public String toString()
	{
		String matrixString = "";
		for(int row = 0; row < matrix.length; row++)
		{
			matrixString += "[";
			for(int column = 0; column < matrix[0].length; column++)
			{
				matrixString = String.format( "%s%8s", matrixString, matrix[row][column] );
				if(column != matrix[0].length - 1)
				{
					matrixString = String.format( "%s%8s", matrixString, "," );
				}
			}
			matrixString = String.format( "%s%8s", matrixString, "]" );
			if(row != matrix.length - 1)
			{
				matrixString += "\n";
			}
		}
		return matrixString;
	}
	
	
}