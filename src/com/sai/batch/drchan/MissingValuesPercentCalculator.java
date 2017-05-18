package com.sai.batch.drchan;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class MissingValuesPercentCalculator {

	public static void main(String[] args) throws IOException {
		
		System.out.println("Process Started");
		
		//Map<Integer, Map<Integer, Float>> ratingsMapByUserId = null;
		
		
		String outputDirPath = "C:\\Users\\sai goud\\Documents\\Masters\\Courses\\Data Mining\\ml-20m\\Movies split Files\\";
		
		File dir = new File(outputDirPath);
		
		List<File> inputFiles = new ArrayList<File>();
		
		for(File file : dir.listFiles()) {
			
			if(file.getName().startsWith("ConvertMatrix_")) {
				
				inputFiles.add(file);
				
			}
			
		}
		
		Map<Integer, Map<Integer, Float>> ratingsMapByMovieId = getRatingsMapByMovieAsKey(
				"C:/Users/sai goud/Documents/Masters/Courses/Data Mining/ml-20m/ratings.csv");
		System.out.println("Ratings Dataset By Movie ID as key read complete");
		
		Map<String, Integer> genreRatedUserCountMap = new HashMap<String, Integer>();
		Map<String, Integer> genreRatedMoviesCountMap = new HashMap<String, Integer>();
		
		for(File file : inputFiles) {
			
			System.out.println("***************************************************************************************************");
			
			String genre = file.getName().replaceAll("ConvertMatrix_", "");
			
			System.out.println("Genre : " + genre);
			
			if(genre.equals("(no genres listed).csv")) {
				System.out.println("Skipped : " + genre);
				continue;
			}
			
			String genreFile = file.getAbsolutePath().replaceAll("ConvertMatrix_", "");
			
			Map<Integer, Integer> genreMovieIdsMap = getMovieIdsTreeMap(genreFile);
			
			genreRatedUserCountMap.put(genre, getRatedUserCount(ratingsMapByMovieId, genreMovieIdsMap));
			
			genreRatedMoviesCountMap.put(genre, genreMovieIdsMap.size());
			
			long expectedCount = genreRatedUserCountMap.get(genre) * genreRatedMoviesCountMap.get(genre);
			
			System.out.println("Total Movies available under this genre : " + genreRatedMoviesCountMap.get(genre));
			
			System.out.println("Number of users who rated for movies under this genre : " + genreRatedUserCountMap.get(genre));
			
			
			System.out.println("Expected Ratings Count (number of users * number of movies) : " + expectedCount);
			
			long actualCount = 0;
			
			for(int genreMovieId : genreMovieIdsMap.keySet()) {
				
				if(ratingsMapByMovieId.containsKey(genreMovieId)) {
					
					actualCount += ratingsMapByMovieId.get(genreMovieId).size();
					
				}
				
			}
			
			System.out.println("Available Ratings Count : " + actualCount);
			
			long missingValuesCount = expectedCount - actualCount;
			
			Double missingValuesPercent = Math.round((((double)(missingValuesCount))/((double)(expectedCount))) * 10000) / 100.0;
			
			System.out.println("Missing Values Percent : " + missingValuesPercent + " %");
			
			
		}
		
		System.out.println("genreRatedUserCountMap : " + genreRatedUserCountMap);
		
		System.out.println("genreRatedMoviesCountMap:" + genreRatedMoviesCountMap);
		
		
		
		System.out.println("Process Completed");
		
		
		
	
	}
	
	private static Integer getRatedUserCount(Map<Integer, Map<Integer, Float>> ratingsMapByMovieId,
			Map<Integer, Integer> genreMovieIdsMap) {
		
		Map<Integer, Integer> genreMovieRatedUserIds = new HashMap<Integer, Integer>();
		for(int genreMovieId : genreMovieIdsMap.keySet()) {
			
			if(ratingsMapByMovieId.containsKey(genreMovieId)) {
				
				
				for(int ratedUserId : ratingsMapByMovieId.get(genreMovieId).keySet()) {
					
					if(!genreMovieRatedUserIds.containsKey(ratedUserId)) {
						
						genreMovieRatedUserIds.put(ratedUserId, ratedUserId);
						
					}
						
					
				}
				
				
			}
			
		}
		
		return genreMovieRatedUserIds.size();
		
		
	}

	
	
	public static Map<Integer, Integer> getMovieIdsTreeMap(String filePath) throws IOException {

		Map<Integer, Integer> movieIdsMap = new TreeMap<Integer, Integer>();
		BufferedReader br = new BufferedReader(new FileReader(new File(filePath)));

		String line = null;
		//String line = br.readLine();

		int i = 0;
		
		while ((line = br.readLine()) != null) {

			String[] splits = line.trim().split(",");
			int movieId = Integer.parseInt(splits[0]);
			movieIdsMap.put(movieId, i);
			
			i++;

		}

		br.close();

		return movieIdsMap;

	}
	
	public static Map<Integer, Map<Integer, Float>> getRatingsMapByUserAsKey(String filePath) throws IOException {

		Map<Integer, Map<Integer, Float>> ratingsMapByUserId = new TreeMap<Integer, Map<Integer, Float>>();

		BufferedReader br = new BufferedReader(new FileReader(new File(filePath)));

		String line = br.readLine();

		while ((line = br.readLine()) != null) {

			line = line.trim();

			if (line.length() > 0) {
				String[] splits = line.trim().split(",");
				int userId = Integer.parseInt(splits[0]);
				int movieId = Integer.parseInt(splits[1]);
				float rating = Float.parseFloat(splits[2]);

				if (!ratingsMapByUserId.containsKey(userId)) {

					ratingsMapByUserId.put(userId, new TreeMap<Integer, Float>());

				}

				ratingsMapByUserId.get(userId).put(movieId, rating);
			}

		}

		br.close();

		return ratingsMapByUserId;
	}
	
	public static Map<Integer, Map<Integer, Float>> getRatingsMapByMovieAsKey(String filePath) throws IOException {

		Map<Integer, Map<Integer, Float>> ratingsMapByMovieId = new TreeMap<Integer, Map<Integer, Float>>();

		BufferedReader br = new BufferedReader(new FileReader(new File(filePath)));

		// Skipping firstline with titles
		String line = br.readLine();

		while ((line = br.readLine()) != null) {

			line = line.trim();

			if (line.length() > 0) {
				String[] splits = line.trim().split(",");
				int userId = Integer.parseInt(splits[0]);
				int movieId = Integer.parseInt(splits[1]);
				float rating = Float.parseFloat(splits[2]);

				if (!ratingsMapByMovieId.containsKey(movieId)) {

					ratingsMapByMovieId.put(movieId, new TreeMap<Integer, Float>());

				}

				ratingsMapByMovieId.get(movieId).put(userId, rating);
			}

		}

		br.close();

		return ratingsMapByMovieId;

	}
	
}
