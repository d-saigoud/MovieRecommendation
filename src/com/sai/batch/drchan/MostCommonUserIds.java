package com.sai.batch.drchan;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

public class MostCommonUserIds {

	public static void main(String[] args) throws IOException {
		
		System.out.println("Process Started");
		Map<Integer, Map<Integer, Float>> ratingsMapByMovieId = getRatingsMapByMovieAsKey(
				"C:/Users/sai goud/Documents/Masters/Courses/Data Mining/ml-20m/ratings.csv");
		System.out.println("Ratings Dataset read complete");
		
		Map<Integer, Integer> movieIdsToConsider = getMovieIdsTreeMap("C:/Users/sai goud/Documents/Masters/Courses/Data Mining/ml-20m/Movies split Files/MovieCountsByGenre_Action.csv");
		
		Map<Integer, Integer> userIdOccurrenceMap = new TreeMap<Integer, Integer>();
		
		for(int movieId : movieIdsToConsider.keySet()) {
			
			for(int userId : ratingsMapByMovieId.get(movieId).keySet()) {
				
				int count = 0;
				if(userIdOccurrenceMap.containsKey(userId)) {
					count = userIdOccurrenceMap.get(userId);
				}
				
				count ++;
				userIdOccurrenceMap.put(userId, count);
				
			}
			
		}
		
		System.out.println("userIdOccurrenceMap : " + userIdOccurrenceMap);
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File("C:/Users/sai goud/Documents/Masters/Courses/Data Mining/ml-20m/Movies split Files/UsersToBeConsideredForEvaluation.csv")));
				
		
		for(int userId : userIdOccurrenceMap.keySet()) {
			
			if(userIdOccurrenceMap.get(userId) > 350) {
				bw.write(userId + "," + userIdOccurrenceMap.get(userId));
				bw.newLine();
			}
			
		}
		
		bw.close();
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
	
	public static Map<Integer, Map<Integer, Float>> getRatingsMapByMovieAsKey(String filePath) throws IOException {
		
		Map<Integer, Map<Integer, Float>> ratingsMapByMovieId = new TreeMap<Integer, Map<Integer, Float>>();
		
		BufferedReader br = new BufferedReader(new FileReader(new File(filePath)));
		
		//Skipping firstline with titles
		String line = br.readLine();
		
		while((line=br.readLine()) != null) {
			
			line = line.trim();
			
			if(line.length() > 0) {
				String[] splits = line.trim().split(",");
				int userId = Integer.parseInt(splits[0]);
				int movieId = Integer.parseInt(splits[1]);
				float rating = Float.parseFloat(splits[2]);
				
				if(!ratingsMapByMovieId.containsKey(movieId)) {
					
					ratingsMapByMovieId.put(movieId, new TreeMap<Integer, Float>());
					
				}
				
				ratingsMapByMovieId.get(movieId).put(userId, rating);
			}
			
		}
		
		br.close();
		
		return ratingsMapByMovieId;
		
	}
	
	
}
