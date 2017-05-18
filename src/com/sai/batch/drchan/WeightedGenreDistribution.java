package com.sai.batch.drchan;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class WeightedGenreDistribution {

	public static void main(String[] args) throws IOException {

		System.out.println("Process Started");

		String outputDirPath = "C:\\Users\\sai goud\\Documents\\Masters\\Courses\\Data Mining\\ml-20m\\Movies split Files\\";
		
		File dir = new File(outputDirPath);
		
		List<File> inputFiles = new ArrayList<File>();
		
		for(File file : dir.listFiles()) {
			
			if(file.getName().startsWith("UserAllMoviesAverageForAGenre_")) {
				
				inputFiles.add(file);
				
			}
			
		}
		
		Map<String, Float> genreAvgRatingByAllUsersMap = new HashMap<String, Float>();
		
		for(File file : inputFiles) {
			
			System.out.println("***************************************************************************************************");
			
			String genre = file.getName().replaceAll("UserAllMoviesAverageForAGenre_", "").replaceAll(".csv", "");
			
			System.out.println("Genre : " + genre);
			
			float avgRating =  getAvgRatingOfAllUsersForGenre(file.getAbsolutePath());
			
			genreAvgRatingByAllUsersMap.put(genre, avgRating);
			
		}
		
		System.out.println("Final genreAvgRatingByAllUsersMap : " + genreAvgRatingByAllUsersMap);
	}

	public static float getAvgRatingOfAllUsersForGenre(String inFilePath)
			throws IOException {

		
		String line = "";

		BufferedReader br = new BufferedReader(new FileReader(new File(inFilePath)));

		double avgRating = 0;
		
		long count = 0;
		
		while((line = br.readLine()) != null) {
			
			line = line.trim();
			
			if(line.length() > 0) {
			
				String [] splits = line.split(",");
				
				avgRating += Float.parseFloat(splits[1]);
				
				count++;
				
			}
			
		}

		br.close();
		
		avgRating = avgRating / count;
		
		avgRating = (float)Math.round(avgRating * 100) / 100;
		
		return (float)avgRating;

	}


}
