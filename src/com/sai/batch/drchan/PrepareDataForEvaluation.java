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
import java.util.Random;
import java.util.TreeMap;

public class PrepareDataForEvaluation {

	public static void main(String[] args) throws IOException {
		
		System.out.println("Process Started");
		
		String outputDirPath = "C:\\Users\\sai goud\\Documents\\Masters\\Courses\\Data Mining\\ml-20m\\Movies split Files\\";

		String considerableMoviesFile = outputDirPath + "MovieCountsByGenre_Action.csv";
		
		Map<Integer, Integer> movieIdsMap = getMovieIdsTreeMap(considerableMoviesFile);
		Map<Integer, Integer> considerableUserIdsMap = getConsiderableUserIdsTreeMap(
				outputDirPath + "\\UsersToBeConsideredForEvaluation.csv");
		
		
		Map<Integer, Map<Integer, Float>> ratingsMapByUserId = getRatingsMapByUserAsKey(
				"C:/Users/sai goud/Documents/Masters/Courses/Data Mining/ml-20m/ratings.csv", considerableUserIdsMap);
		System.out.println("Ratings Dataset read complete");
		
		long size = 0;
		
		for(int userId : ratingsMapByUserId.keySet()) {
			
			size += ratingsMapByUserId.get(userId).size();
			
		}
		
		
		long tenPercentOfSize = size / 10;
		
		System.out.println(size + " : " + tenPercentOfSize);

		
		List<Integer> userIdList = new ArrayList<Integer>();
		List<Integer> movieIdList = new ArrayList<Integer>();
		
		for(int userId : considerableUserIdsMap.keySet()) {
			userIdList.add(userId);
		}
		
		for(int movieId : movieIdsMap.keySet()) {
			movieIdList.add(movieId);
		}
		
		Random rand = new Random();
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(outputDirPath + "RemovedData_ForEval_Action.csv")));
		
		for(;tenPercentOfSize > 0;tenPercentOfSize--) {
			
			while(true) {
				int userId = userIdList.get(rand.nextInt(userIdList.size()));
				int movieId = movieIdList.get(rand.nextInt(movieIdList.size()));
			
				if(ratingsMapByUserId.containsKey(userId) && ratingsMapByUserId.get(userId).containsKey(movieId)) {
					
					bw.write(userId + "," + movieId + "," + ratingsMapByUserId.get(userId).get(movieId) );
					bw.newLine();
					ratingsMapByUserId.get(userId).remove(movieId);
					break;
					
				}
				
			}
			
		}
		
		bw.close();
		
		appendPredictedDataToRatingsMapByUserAsKey(outputDirPath + "FinalRatingsPredicted_ForEval_Action.csv", considerableUserIdsMap, ratingsMapByUserId);
		writeDataOfRatingsMapByUserAsKey(outputDirPath + "TestRatingsDatasetForEval_Action.csv", ratingsMapByUserId);
		
	}
	
	public static Map<Integer, Integer> getMovieIdsTreeMap(String filePath) throws IOException {

		Map<Integer, Integer> movieIdsMap = new TreeMap<Integer, Integer>();
		BufferedReader br = new BufferedReader(new FileReader(new File(filePath)));

		String line = br.readLine();

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

	public static Map<Integer, Integer> getConsiderableUserIdsTreeMap(String filePath) throws IOException {

		Map<Integer, Integer> considerableUserIdsMap = new TreeMap<Integer, Integer>();
		BufferedReader br = new BufferedReader(new FileReader(new File(filePath)));

		String line = br.readLine();

		while ((line = br.readLine()) != null) {

			String[] splits = line.trim().split(",");
			int userId = Integer.parseInt(splits[0]);
			considerableUserIdsMap.put(userId, userId);

		}

		br.close();

		return considerableUserIdsMap;

	}
	
	public static void appendPredictedDataToRatingsMapByUserAsKey(String filePath, Map<Integer, Integer> considerableUserIdsMap, Map<Integer, Map<Integer, Float>> ratingsMapByUserId) throws IOException {

		BufferedReader br = new BufferedReader(new FileReader(new File(filePath)));

		String line = "";

		while ((line = br.readLine()) != null) {

			line = line.trim();

			if (line.length() > 0) {
				String[] splits = line.trim().split(",");
				int userId = Integer.parseInt(splits[0]);
				int movieId = Integer.parseInt(splits[1]);
				float rating = Float.parseFloat(splits[2]);

				if(considerableUserIdsMap.containsKey(userId)) {
	
					if(ratingsMapByUserId.get(userId).containsKey(movieId)) {
						
						System.out.println("Error Data Already Present");
						return;
					}
					
					ratingsMapByUserId.get(userId).put(movieId, rating);
				}
			}

		}

		br.close();

	}

	public static Map<Integer, Map<Integer, Float>> getRatingsMapByUserAsKey(String filePath, Map<Integer, Integer> considerableUserIdsMap) throws IOException {

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

				if(considerableUserIdsMap.containsKey(userId)) {
					if (!ratingsMapByUserId.containsKey(userId)) {
	
						ratingsMapByUserId.put(userId, new TreeMap<Integer, Float>());
	
					}
	
					ratingsMapByUserId.get(userId).put(movieId, rating);
				}
			}

		}

		br.close();

		return ratingsMapByUserId;
	}

	public static void writeDataOfRatingsMapByUserAsKey(String filePath, Map<Integer, Map<Integer, Float>> ratingsMapByUserId) throws IOException {

		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(filePath)));

		for(int userId : ratingsMapByUserId.keySet()) {
			
			for(int movieId : ratingsMapByUserId.get(userId).keySet()) {
				
				bw.write(userId + "," + movieId + "," + ratingsMapByUserId.get(userId).get(movieId));
				bw.newLine();
			}
			
		}

		bw.close();

	}
	
	
	
}
