package mathRepresentation;
import java.math.BigDecimal;

import stringUtils.StringMethods;

public class Fraction
{
	//fields
	private int numerator;
	private int denomenator;
	
	//EVC
	public Fraction(final String stringNum)
	{
		String[] fractionComponents = stringNum.split("/");
		StringMethods.trimAllElements(fractionComponents);
		
		try
		{
			this.numerator              = Integer.parseInt(fractionComponents[0]);
			this.denomenator            = fractionComponents.length == 1 ? 1 : Integer.parseInt(fractionComponents[1]);
		}
		catch(NumberFormatException e)
		{
			throw new IllegalArgumentException("Cannot create fraction with invalid values");
		}
	}
	
	public Fraction(final int numerator, final int denomerator)
	{
		this.numerator   = numerator;
		this.denomenator = denomerator;
	}
	
	//Getters and Setters
	public int getNumerator()
	{
		return this.numerator;
	}
	
	public int getDenomenator()
	{
		return this.denomenator;
	}
	
	//Non-Instance Methods
	public static Fraction[] stringArrayToFractionArr(String[] stringArr)
	{
		Fraction[] fractionArr = new Fraction[stringArr.length];
		for(int index = 0; index < stringArr.length; index++)
		{
			fractionArr[index] = new Fraction(stringArr[index]);
		}
		return fractionArr;
	}
	
	public static Fraction[] stringArrayToFractionArr(int[] stringArr)
	{
		Fraction[] fractionArr = new Fraction[stringArr.length];
		for(int index = 0; index < stringArr.length; index++)
		{
			fractionArr[index] = new Fraction(stringArr[index] + "");
		}
		return fractionArr;
	}
	
	public static boolean canFraction(final String fractionString)
	{
		boolean result = true;
		try
		{
			new Fraction(fractionString);
		}
		catch(NumberFormatException e)
		{
			result = false;
		}
		return result;
	}
	
	//Instance methods
	public void simplify()
	{
		int divisor      = numerator < denomenator ? findDivisor(denomenator, numerator) : findDivisor(numerator, denomenator);
		this.numerator   = this.numerator   / divisor;
		this.denomenator = this.denomenator / divisor;
	}
	
	public Fraction add(final Fraction otherFraction)
	{
		int numeratorOne      = this.numerator          * otherFraction.denomenator;
		int numeratorTwo      = otherFraction.numerator * this.denomenator;
		int sharedDenomenator = this.denomenator        * otherFraction.denomenator;
		
		int sumNumerator   = numeratorOne + numeratorTwo;
		
		Fraction sumFraction = new Fraction(sumNumerator, sharedDenomenator);
		sumFraction.simplify();
		
		return sumFraction;
	}
	
	public Fraction multiply(final Fraction otherFraction)
	{
		int newNumerator   = this.getNumerator()   * otherFraction.getNumerator();
		int newDenomenator = this.getDenomenator() * otherFraction.getDenomenator();	
		
		Fraction mulitpliedFraction = new Fraction(newNumerator, newDenomenator);
		mulitpliedFraction.simplify();
		
		return mulitpliedFraction;
	}
	
	public double getDoubleRepresentation()
	{
		return this.numerator / this.denomenator;
	}
	
	public BigDecimal getBigDecimalRepresentation()
	{
		BigDecimal bdNumerator = new BigDecimal(this.numerator);
		BigDecimal bdDecimal   = new BigDecimal(this.denomenator);
		return bdNumerator.divide(bdDecimal);
	}
	
	private int findDivisor(int largerValue, int smallerValue)
	{
		int divisor = 1;
		
		for(int counter = 1; counter <= largerValue; counter++)
		{
			if(largerValue % counter == 0 && smallerValue % counter == 0)
			{
				divisor = counter;
			}
		}
		
		return divisor;
	}
	
	//Overwritten Methods
	@Override
	public boolean equals(Object otherFraction)
	{
		boolean result;
		if(otherFraction == null || ! (otherFraction instanceof Fraction) )
		{
			result = false;
		}
		else
		{
			Fraction otherFractionConv = (Fraction) otherFraction;
			result = this.numerator == otherFractionConv.getNumerator() && this.denomenator == otherFractionConv.getDenomenator();
		}
		return result;
	}
		
	@Override
	public String toString()
	{
		return this.denomenator == 1 ? this.numerator + "" : this.numerator + "/" + this.denomenator;
	}
}
