package com.sai.batch.drchan;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class UserAllMoviesAverageForAGenre {

	public static void main(String[] args) throws IOException {

		System.out.println("Process Started");

		String outputDirPath = "C:\\Users\\sai goud\\Documents\\Masters\\Courses\\Data Mining\\ml-20m\\Movies split Files\\";
		
		File dir = new File(outputDirPath);
		
		List<File> inputFiles = new ArrayList<File>();
		
		for(File file : dir.listFiles()) {
			
			if(file.getName().startsWith("ConvertMatrix_")) {
				
				inputFiles.add(file);
				
			}
			
		}
		
		for(File file : inputFiles) {
			
			System.out.println("***************************************************************************************************");
			
			String outFilePath = outputDirPath + "\\UserAllMoviesAverageForAGenre_" + file.getName().replaceAll("ConvertMatrix_", "");
			
			Map<Integer, List<Float>> userIdRatingsMap = getUserIdRatingsForGivenGenreMoviesMap(file.getAbsolutePath());
			
			System.out.println(file.getName() + " Dataset read complete");
			
			calculateAndWriteAverageMovieRatingsByUserForGenreMap(userIdRatingsMap, outFilePath);
			
			System.out.println(file.getName().replaceAll(".csv", "") + " Genre out file created");
		}
		
		/*
		String outFilePath = "C:\\Users\\sai goud\\Documents\\Masters\\Courses\\Data Mining\\ml-20m\\Movies split Files\\SimilarityMatrix.csv";
		Map<Integer, Integer> movieIdsMap = getMovieIdsTreeMap(
				"C:\\Users\\sai goud\\Documents\\Masters\\Courses\\Data Mining\\ml-20m\\Movies split Files\\movies.csv");
		System.out.println("Movies Dataset read complete");
		Map<Integer, Map<Integer, Float>> ratingsMapByMovieId = getRatingsMapByMovieAsKey(
				"C:/Users/sai goud/Documents/Masters/Courses/Data Mining/ml-20m/ratings.csv");
		System.out.println("Ratings Dataset read complete");
		// System.out.println(ratingsMapByMovieId);
		findSimilarities(ratingsMapByMovieId, outFilePath, movieIdsMap);
		*/
	}

	public static Map<Integer, List<Float>> getUserIdRatingsForGivenGenreMoviesMap(String inFilePath)
			throws IOException {

		Map<Integer, List<Float>> userIdRatingsMap = new TreeMap<Integer, List<Float>>();
		
		String line = "";

		BufferedReader br = new BufferedReader(new FileReader(new File(inFilePath)));

		while((line = br.readLine()) != null) {
			
			line = line.trim();
			
			if(line.length() > 0) {
			
				String [] splits = line.split(",");
				
				for(int i=1; i<splits.length; i++) {
					
					String[] splits2 = splits[i].split("u");
					
					float movieRating = Float.parseFloat(splits2[0]);
					int userId = Integer.parseInt(splits2[1]);
					
					if(!userIdRatingsMap.containsKey(userId)) {
						
						userIdRatingsMap.put(userId, new ArrayList<Float>());
						
					}
					
					userIdRatingsMap.get(userId).add(movieRating);
					
				}
				
				
			}
			
		}

		br.close();
		
		return userIdRatingsMap;

	}


	public static void calculateAndWriteAverageMovieRatingsByUserForGenreMap(Map<Integer, List<Float>> userIdRatingsMap, String outFilePath) throws IOException {

	
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(outFilePath)));
		
		for(int userId : userIdRatingsMap.keySet()) {
			
			float avg = 0;
			
			for(float rating : userIdRatingsMap.get(userId)) {
				
				avg += rating;
				
			}
			
			avg = avg / userIdRatingsMap.get(userId).size();
			
			avg = (float)Math.round(avg * 100) / 100;
			
			bw.write("u" + userId + "," + avg);
			bw.newLine();
			
		}
		
		bw.close();
		
		

	}


}
