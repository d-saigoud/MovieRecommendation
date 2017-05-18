package com.sai.batch.drchan;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class GenreTagMatrix {

	public static void main(String[] args) {
		
		
		
	}
	
	public static void startBuildingGenreTagMatrix(String inFilePath) throws IOException {
		
		BufferedReader br = new BufferedReader(new FileReader(new File(inFilePath)));
		
		int previousMovieId = 0;
		
		String line = "";
		
		
		
		while((line = br.readLine()) != null) {
			
			String [] splits = line.trim().split(",");
			
			int presentMovieId = Integer.parseInt(splits[0]);
			
			if(previousMovieId == 0) {
				
				
				
			}
			else if(previousMovieId != presentMovieId) {
				
				
				
			}
			
			previousMovieId = presentMovieId;
			
		}
		
		br.close();
		
	}
	
	public static void getGenreIds(String inFilePath) throws IOException {
		
		BufferedReader br = new BufferedReader(new FileReader(new File(inFilePath)));
		
		int previousMovieId = 0;
		
		String line = "";
		
		
		
		while((line = br.readLine()) != null) {
			
			String [] splits = line.trim().split(",");
			
			int presentMovieId = Integer.parseInt(splits[0]);
			
			if(previousMovieId == 0) {
				
				
				
			}
			else if(previousMovieId != presentMovieId) {
				
				
				
			}
			
			previousMovieId = presentMovieId;
			
		}
		
		br.close();
		
	}
	
}
