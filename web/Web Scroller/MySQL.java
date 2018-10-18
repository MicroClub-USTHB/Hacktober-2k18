import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;

public class MySQL {

	public static void main(String[] args) throws IOException, SQLException {
		String ligne; int i=0;
		BufferedReader entree;
		 Connection c = (Connection) DriverManager.getConnection("jdbc:mysql://127.0.0.1/Extraction","root","");
		 
		 
		 try {
			entree = new BufferedReader (new InputStreamReader (new FileInputStream (new File("dict.dic")),"UTF-16"));
			ligne=entree.readLine();
			do{
			
			i++;
			System.out.println(i+"   "+ligne);
			String req = "INSERT INTO `medicament`(`Id`, `Substance`) VALUES ("+i+",'"+ligne+"');";
		//	String req1 = "DELETE FROM `extraction`.`medicament` WHERE `medicament`.`Id` = 2";
		//	Statement s = (Statement) c.createStatement();
		//	s.executeUpdate(req);
			ligne=entree.readLine();
			}while(ligne!=null);
			//s.executeUpdate(req1);
			entree.close();
		
		
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
        
	}

}
