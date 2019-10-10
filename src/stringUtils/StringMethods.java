package stringUtils;

public class StringMethods
{
	public static void trimAllElements(String[] arr)
	{
		for(String str : arr)
		{
			str = str.trim();
		}
	}
	
	public static String[] removeEmptyElements(String[] arr)
	{
		int counter = 0; 
		
		for(int index = 0; index < arr.length; index++)
		{
			if(!arr[index].trim().isEmpty())
			{
				counter++;
			}
		}
		
		String[] subsetArr = new String[counter];
		int subsetArrCounter = 0;
		
		for(int index = 0; subsetArrCounter != counter; index++)
		{
			if(!arr[index].trim().isEmpty())
			{
				subsetArr[subsetArrCounter] = arr[index];
				subsetArrCounter++;
			}
		}
		
		return subsetArr;
	}
	
	
	
	public static boolean canParseStringToInt(final String str)
	{
		boolean canParse = true;
		try
		{
			Integer.parseInt(str);
		}
		catch(Exception e)
		{
			canParse = false;
		}
		return canParse;
	}
}