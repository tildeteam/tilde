package scripts;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class LabelingScript {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.println("enter the path to the file");
		String path = scanner.nextLine();
		System.out.println("Path entered ="+path);
		System.out.println("enter the label to write");
		String label = scanner.nextLine();
		System.out.println("Label entered ="+label);
		scanner.close();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(path));
			BufferedWriter writer = new BufferedWriter(new FileWriter(path+"Labeled"));
			String line;
			while ((line = reader.readLine())!= null) {
				
				writer.write(line+",\""+label+"\""+"\n");
				
			}
			
			
			writer.close();
			reader.close();
		} catch (IOException e) {
			
			e.printStackTrace();
		}

		
	}

}
