package com.sai.batch.drchan;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

public class RootMeanSquareEvaluation {

	public static void main(String[] args) throws IOException {
		
		Map<Integer, Map<Integer, Float>> originalRatingsMapByMovieId = getRatingsMapByMovieAsKey("C:/Users/sai goud/Documents/Masters/Courses/Data Mining/ml-20m/Movies split Files/RemovedData_ForEval_Action.csv");
		Map<Integer, Map<Integer, Float>> predictedRatingsMapByMovieId = getRatingsMapByMovieAsKey("C:/Users/sai goud/Documents/Masters/Courses/Data Mining/ml-20m/Movies split Files/FinalRatingsPredicted_ForFinalEval_Action.csv");
		
		Double mae = getMeanAbsoluteError(originalRatingsMapByMovieId, predictedRatingsMapByMovieId);
		
		System.out.println("MAE : " + mae);
	}
	
	public static Map<Integer, Map<Integer, Float>> getRatingsMapByMovieAsKey(String filePath) throws IOException {

		Map<Integer, Map<Integer, Float>> ratingsMapByMovieId = new TreeMap<Integer, Map<Integer, Float>>();

		BufferedReader br = new BufferedReader(new FileReader(new File(filePath)));

		String line = "";

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
	
	public static double getMeanAbsoluteError(Map<Integer, Map<Integer, Float>> originalRatingsMapByMovieId, Map<Integer, Map<Integer, Float>> predictedRatingsMapByMovieId) {
		
		if(originalRatingsMapByMovieId.size() != predictedRatingsMapByMovieId.size()) {
			System.out.println("Sizes are different");
			return -1;
		}
		
		Double mae = 0.0;
		long size = 0;
		
		for(int movieId : originalRatingsMapByMovieId.keySet()) {
			
			for(int userId : originalRatingsMapByMovieId.get(movieId).keySet()) {
				
				float diff = originalRatingsMapByMovieId.get(movieId).get(userId) - predictedRatingsMapByMovieId.get(movieId).get(userId);
				
				if(diff < 0 ) {
					diff *= -1;
				}
				
				mae += diff;
				
				size++;
				
			}
			
		}
		
		System.out.println(mae);
		
		return mae / size;
		
		
	}
	
}
