package com.sai.batch;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileLinesExtractor {

	public static void main(String[] args) throws IOException {
		
		extractLines("C:\\Users\\sai goud\\Documents\\Masters\\Courses\\Data Mining\\ml-20m\\Movies split Files\\FinalRatingsPredicted_Action.csv", "C:\\Users\\sai goud\\Documents\\Masters\\Courses\\Data Mining\\ml-20m\\Movies split Files\\FinalRatingsPredicted_Action_50000.csv", 50000); 
		
	}
	
	public static void extractLines(String inFilePath, String outFilePath, int linesToExtract) throws IOException {
		
		BufferedReader br = new BufferedReader(new FileReader(new File(inFilePath)));
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(outFilePath)));
		
		String line = "";
		
		while(linesToExtract > 0 && (line=br.readLine()) != null) {
			
			bw.write(line.trim());
			bw.newLine();
			linesToExtract--;
			
		}
		
		bw.close();
		br.close();
		
	}
	
}
