package scripts;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DecimalFormat;
import java.util.Scanner;

public class TimeStampChanger {

	public static void main(String[] args) {

		Scanner scanner = new Scanner(System.in);

		System.out.println("Enter the file path");
		String filePath = scanner.nextLine();
		System.out.println("Enter the desired time");
		String timeToWrite = scanner.nextLine();
		System.out.println("Enter the desired time delta");
		String timeDelta = scanner.nextLine();
		scanner.close();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filePath));
			BufferedWriter writer = new BufferedWriter(new FileWriter(filePath + "Timed"));
			String line;
			String modifiedLine = "";
			int j = 1 ;
			while ((line = reader.readLine()) != null) {
				modifiedLine = "";
				String[] seperatedLine = line.split(",");
				System.out.print(seperatedLine[1]+" ------> ");
				DecimalFormat df = new DecimalFormat("#");
				df.setMaximumFractionDigits(10);
				String finalNumber = df.format((Double.parseDouble(timeToWrite) + (j)*Double.parseDouble(timeDelta)));
				seperatedLine[1] = finalNumber;
				System.out.println(seperatedLine[1]);
				for (int i = 0; i < seperatedLine.length; i++) {
					if (i != seperatedLine.length - 1) {
						modifiedLine = modifiedLine + seperatedLine[i] + ",";
					}
					else
					{
						modifiedLine = modifiedLine + seperatedLine[i];
					}
				}

				writer.write(modifiedLine+"\n");
				writer.flush();
				j++;
			}

			writer.close();
			reader.close();
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

}
