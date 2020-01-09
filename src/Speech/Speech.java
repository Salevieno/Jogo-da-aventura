package Speech;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Speech 
{
	public String OpeningText(String cat, int pos)
	{
		String fileName = "Text-PT-br.txt";
		String Text = "";
		try
		{	
			FileReader fileReader = new FileReader (fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader); 					
			String Line = bufferedReader.readLine();
			
			while (!Line.equals(cat))
			{
				Line = bufferedReader.readLine();
            }
			for (int i = 0; i <= pos - 2; ++i)
			{
				Line = bufferedReader.readLine();	
			}
			Text = bufferedReader.readLine();
			bufferedReader.close();
		}
		catch(FileNotFoundException ex) 
		{
            System.out.println("Unable to find file '" + fileName + "'");                
        }		
		catch(IOException ex) 
		{
            System.out.println("Error reading file '" + fileName + "'");                  
        }
		return Text;
	}
}
