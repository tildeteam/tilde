package scripts;

import java.util.Scanner;

import util.DatasetConverter;

public class ConvertingScript {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		String inputFilePath, outputFilePath;
		int windowSize;
		
		System.out.println("enter the input file's path: ");
		inputFilePath = scanner.nextLine();
		
		System.out.println("enter the output file's path: ");
		outputFilePath = scanner.nextLine();
		
		System.out.println("enter the window size: ");
		windowSize = scanner.nextInt();
		
		DatasetConverter converter = new DatasetConverter(inputFilePath, outputFilePath, windowSize);
		converter.convert();

				
		scanner.close();
	}

}
