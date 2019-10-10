package linearAlgebra;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import mathRepresentation.Fraction;

public class Vector
{
	//fields
	private Fraction[] vector;
	
	//EVC
	public Vector(String fileName)
	{
		//TODO
		throw new IllegalStateException("Not yet implemented.");
	}
	
	public Vector(Fraction[] passedInVector)
	{
		this.vector = passedInVector;
	}
	
	//instance methods
	public int size()
	{
		return vector.length;
	}
	
	public Vector add(final Vector otherVector)
	{
		if(otherVector == null || otherVector.size() != vector.length)
		{
			throw new IllegalArgumentException("Passed in vector must be of same length to sum.");
		}
		else
		{
			Fraction[] otherVectorFractionArr = otherVector.getFracArrRepresentation();
			Fraction[] sumFractionArr = new Fraction[vector.length];
			for(int index = 0; index < sumFractionArr.length; index++)
			{
				sumFractionArr[index] = otherVectorFractionArr[index].add(vector[index]);
			}
			return new Vector(sumFractionArr);
		}
	}
	
	public Fraction dotProduct(final Vector otherVector)
	{
		if(otherVector == null || otherVector.size() != vector.length)
		{
			throw new IllegalArgumentException("Passed in vector must be of same length to calculate dot product.");
		}
		else
		{
			Fraction sum = new Fraction("0");
			Fraction[] otherVectorFractions = otherVector.getFracArrRepresentation();
			for(int index = 0; index < vector.length; index++)
			{
				sum = sum.add(vector[index].multiply(otherVectorFractions[index]));
			}
			return sum;
		}
	}
	
	public boolean isVectorsOrthagonal(final Vector otherVector)
	{
		if(otherVector == null)
		{
			return false;
		}
		else
		{
			return this.dotProduct(otherVector).equals(new Fraction("0"));
		}
	}
	
	public boolean isVectorsParallel(final Vector otherVector)
	{
		//Lazy way to do, wasn't able to use BigDecimal due to Java 8 not supporting sqrt until Java 9
		//Works ok for whole numbers, does not work for actual fractions....
		DecimalFormat df = new DecimalFormat("#.#####");
		df.setRoundingMode(RoundingMode.FLOOR);
		System.out.println(this.getMagnitude() * otherVector.getMagnitude());
		System.out.println(this.dotProduct(otherVector).getDoubleRepresentation());
		double roundedDotProduct = Double.parseDouble(df.format(this.getMagnitude() * otherVector.getMagnitude()));

		return (this.dotProduct(otherVector).getDoubleRepresentation() == roundedDotProduct);
	}
	
	public Vector multiplyByScalar(final Fraction scalar)
	{
		Fraction[] scaleVector = new Fraction[vector.length];
		for(int index = 0; index < vector.length; index++)
		{
			scaleVector[index] = vector[index].multiply(scalar);
		}
		return new Vector(scaleVector);
	}
	
	public double getMagnitude()
	{
		double magnitude = 0;
		for(int index = 0; index < vector.length; index++)
		{
			magnitude += Math.pow(vector[index].getDoubleRepresentation(), 2);
		}
		magnitude = Math.sqrt(magnitude);
		return magnitude;
	}
	
	public Fraction[] getFracArrRepresentation()
	{
		Fraction[] fractionArrCopy = new Fraction[vector.length];
		for(int index = 0; index < fractionArrCopy.length; index++)
		{
			fractionArrCopy[index] = new Fraction(vector[index].toString());
		}
		return fractionArrCopy;
	}
	
	//Overwritten Methods
	@Override
	public String toString()
	{
		String vectorString = "[";
		for(int index = 0; index < vector.length; index++)
		{
			vectorString = String.format( "%s%8s", vectorString, vector[index]);
			if(index != vector.length - 1)
			{
				vectorString = String.format( "%s%8s", vectorString, "," );
			}
		}
		vectorString = String.format( "%s%8s", vectorString, "]" );

		return vectorString;
	}
}
