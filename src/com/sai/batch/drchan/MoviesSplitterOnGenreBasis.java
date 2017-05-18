package com.sai.batch.drchan;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class MoviesSplitterOnGenreBasis {
	
	public static void main(String[] args) throws IOException {
		
		Map<Integer, Map<String, String>> movieIdAndGenresMap = getMovieIdAndGenresMap("C:\\Users\\sai goud\\Documents\\Masters\\Courses\\Data Mining\\ml-20m\\movies-genre1.csv");
		//System.out.println(movieIdAndGenresMap);
		readAndSplitMovieBasedOnGenres("C:\\Users\\sai goud\\Documents\\Masters\\Courses\\Data Mining\\ml-20m\\movies.csv", movieIdAndGenresMap);
		
	}
	
	public static void readAndSplitMovieBasedOnGenres(String inFilePath, Map<Integer, Map<String, String>> movieIdAndGenresMap) throws IOException{
		
		BufferedReader br = new BufferedReader(new FileReader(new File(inFilePath)));
		
		String line = "";
		
		//Skip Titles line
		line=br.readLine();
		
		while((line=br.readLine()) != null) {
			
			line = line.trim();
			
			if(line.length() > 0) {
				String[] splits = line.split(",");
				int movieId = Integer.parseInt(splits[0].trim());
				
				for(String genre : movieIdAndGenresMap.get(movieId).keySet()) {
					writeToAppropriateFile(genre, line);
				}
				
				System.out.println(movieId);
				
			}
			
		}
		
		br.close();
		
		
	}
	
	public static void writeToAppropriateFile(String genre, String movieDetails) throws IOException {
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File("C://Users//sai goud//Documents//Masters//Courses//Data Mining//ml-20m//Movies split Files//" + genre + ".csv"), true));
		
		
		
		bw.write(movieDetails);
		bw.newLine();
		
		
		
		bw.close();
		
	}
	
	public static Map<Integer, Map<String, String>> getMovieIdAndGenresMap(String inFilePath) throws IOException{
		
		Map<Integer, Map<String, String>> movieIdAndGenresMap = new TreeMap<Integer, Map<String, String>>();
		
		BufferedReader br = new BufferedReader(new FileReader(new File(inFilePath)));
		
		String line = "";
		
		while((line=br.readLine()) != null) {
			
			line = line.trim();
			
			if(line.length() > 0) {
				
				String[] splits = line.split(",");
				
				int movieId = Integer.parseInt(splits[0].trim());
				
				if(!movieIdAndGenresMap.containsKey(movieId)) {
					
					movieIdAndGenresMap.put(movieId, new HashMap<String, String>());
					
				}
				
				if(!movieIdAndGenresMap.get(movieId).containsKey(splits[1].trim())) {
					
					movieIdAndGenresMap.get(movieId).put(splits[1].trim(), "");
					
				}
				
			}
			
			
			
		}
		
		br.close();
		
		return movieIdAndGenresMap;
		
		
	}

}
