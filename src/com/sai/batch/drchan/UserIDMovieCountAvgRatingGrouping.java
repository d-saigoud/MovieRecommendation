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

public class UserIDMovieCountAvgRatingGrouping {

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
		
		Map<Integer, Map<Integer, Float>> ratingsMapByUserID = getRatingsMapByUserAsKey(
				"C:/Users/sai goud/Documents/Masters/Courses/Data Mining/ml-20m/ratings.csv");
		System.out.println("Ratings Dataset By Movie ID as key read complete");
		
		for(File file : inputFiles) {
			
			System.out.println("***************************************************************************************************");
			
			String genre = file.getName().replaceAll("ConvertMatrix_", "").replaceAll(".csv", "");
			
			System.out.println("Genre : " + genre);
			
			if(genre.equals("(no genres listed).csv")) {
				System.out.println("Skipped : " + genre);
				continue;
			}
			
			String genreFile = file.getAbsolutePath().replaceAll("ConvertMatrix_", "");
			String outFile = file.getAbsolutePath().replaceAll("ConvertMatrix_", "UserIDMovieCountAvgRating_");
			
			Map<Integer, Integer> genreMovieIdsMap = getMovieIdsTreeMap(genreFile);
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(outFile)));
			
			for(int userId : ratingsMapByUserID.keySet()) {
				
				int count = 0;
				double sumOfRatings = 0;
				
				for(int movieId : ratingsMapByUserID.get(userId).keySet()) {
					
					if(genreMovieIdsMap.containsKey(movieId)) {
						sumOfRatings += ratingsMapByUserID.get(userId).get(movieId);
						count++;
					}
					
				}
				
				if(count > 0) {
					
					bw.write(userId + "," + count + "," + (sumOfRatings/count));
					bw.newLine();
					
				}
				
			}
			
			bw.close();
			
		}
		
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
