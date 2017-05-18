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

public class UserUnratedMoviesRatingPrediction {

	public static void main(String[] args) throws IOException {

		System.out.println("Process Started");
		
		//Map<Integer, Map<Integer, Float>> ratingsMapByUserId = null;
		Map<Integer, Map<Integer, Float>> ratingsMapByUserId = getRatingsMapByUserAsKey(
				"C:/Users/sai goud/Documents/Masters/Courses/Data Mining/ml-20m/ratings.csv");
		System.out.println("Ratings Dataset read complete");
		
		String outputDirPath = "C:\\Users\\sai goud\\Documents\\Masters\\Courses\\Data Mining\\ml-20m\\Movies split Files\\";
		
		File dir = new File(outputDirPath);
		
		List<File> inputFiles = new ArrayList<File>();
		
		for(File file : dir.listFiles()) {
			
			if(file.getName().startsWith("ConvertMatrix_")) {
				
				inputFiles.add(file);
				
			}
			
		}
		
		Map<String, Float> genreAvgRatingByAllUsersMap = new HashMap<String, Float>();
		
		for(File file : inputFiles) {
			
			System.out.println("***************************************************************************************************");
			
			String genre = file.getName().replaceAll("ConvertMatrix_", "");
			
			System.out.println("Genre : " + genre);
			
			if(genre.equals("(no genres listed).csv")) {
				continue;
			}
			
			String genreFile = file.getAbsolutePath().replaceAll("ConvertMatrix_", "");
			
			
			
			String similarityFile = file.getAbsolutePath().replaceAll("ConvertMatrix_", "SimilarityMatrix_");
			
			System.out.println("Similarity File : " + similarityFile);
			
			Map<Integer, Integer> movieIdsMap = getMovieIdsTreeMap(genreFile);
			float[][] movieSimilarityMatrix = getSimilarityMovieMatrixForGenre(similarityFile, movieIdsMap);
			
			//writeOutSimilarityMatrix(movieSimilarityMatrix, "" + outputDirPath + "Duplicate_" + genre);
			
			predictForUnRatedMovies(ratingsMapByUserId, movieIdsMap, movieSimilarityMatrix, "" + outputDirPath + "FinalRatingsPredicted_" + genre);
			
		}
		
		System.out.println("Final genreAvgRatingByAllUsersMap : " + genreAvgRatingByAllUsersMap);
	}

	private static void predictForUnRatedMovies(Map<Integer, Map<Integer, Float>> ratingsMapByUserId,
			Map<Integer, Integer> movieIdsMap, float[][] movieSimilarityMatrix, String outFilePath) throws IOException {
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(outFilePath)));
		
		for(int userId : ratingsMapByUserId.keySet()) {
			
			for(int movieId : movieIdsMap.keySet()) {
				
				if(!ratingsMapByUserId.get(userId).containsKey(movieId)) {
					
					float numerator = 0;
					float denominator = 0;
					
					for(int userRatedMovieId : ratingsMapByUserId.get(userId).keySet()) {
						
						if(movieIdsMap.containsKey(userRatedMovieId) && movieId != userRatedMovieId) {
							
							float rating = ratingsMapByUserId.get(userId).get(userRatedMovieId);
							float similarity = 0;
							
							if(movieId > userRatedMovieId) {
								similarity = movieSimilarityMatrix[movieIdsMap.get(userRatedMovieId)][movieIdsMap.get(movieId)];
							}
							else {
								similarity = movieSimilarityMatrix[movieIdsMap.get(movieId)][movieIdsMap.get(userRatedMovieId)];
							}
							
							numerator += (rating * similarity);
							denominator += similarity;
							
							//System.out.println("Inner iter : " + " rating : " + rating + " sim : " + similarity + " sim1 : " + movieSimilarityMatrix[movieIdsMap.get(userRatedMovieId)][movieIdsMap.get(movieId)] + " sim2 : " + movieSimilarityMatrix[movieIdsMap.get(movieId)][movieIdsMap.get(userRatedMovieId)]);
						}
					}
					
					if(denominator != 0) {
						float ratingPredicted = numerator/denominator;
						
						bw.write("" + userId + "," + movieId + "," + ratingPredicted);
						bw.newLine();
					}
					
					
					
					//System.out.println("u" + userId + ",m" + movieId + ",r" + ratingPredicted);
					
					//System.out.println(numerator + "," + denominator);
					
				}
				
				
				
			}
			
		}
		
		bw.close();
		
		
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
	
	public static Map<Integer, Map<Integer, Float>> getRatingsMapByUserAsKey(String filePath) throws IOException {
		
		Map<Integer, Map<Integer, Float>> ratingsMapByUserId = new TreeMap<Integer, Map<Integer, Float>>();
		
		BufferedReader br = new BufferedReader(new FileReader(new File(filePath)));
		
		String line = br.readLine();
		
		while((line=br.readLine()) != null) {
			
			line = line.trim();
			
			if(line.length() > 0) {
				String[] splits = line.trim().split(",");
				int userId = Integer.parseInt(splits[0]);
				int movieId = Integer.parseInt(splits[1]);
				float rating = Float.parseFloat(splits[2]);
				
				if(!ratingsMapByUserId.containsKey(userId)) {
					
					ratingsMapByUserId.put(userId, new TreeMap<Integer, Float>());
					
				}
				
				ratingsMapByUserId.get(userId).put(movieId, rating);
			}
			
		}
		
		br.close();
		
		return ratingsMapByUserId;
	}
	
	public static float[][] getSimilarityMovieMatrixForGenre(String inFilePath, Map<Integer, Integer> movieIdsMap)
			throws IOException {

		float[][] similarityMat = new float[movieIdsMap.size()][movieIdsMap.size()];
		
		String line = "";

		BufferedReader br = new BufferedReader(new FileReader(new File(inFilePath)));
		int i=0;
		while((line = br.readLine()) != null) {
			
			line = line.trim();
			
			if(line.length() > 0) {
			
				String [] splits = line.split(",");
				
				
				for(int j=0; j<splits.length; j++) {
					
					
					
					float movieSim = Float.parseFloat(splits[j]);
					
					
					similarityMat[i][j] = movieSim;
					
				}
				
				i++;
				
				
			}
			
		}

		br.close();
		
		return similarityMat;

	}
	
	public static void writeOutSimilarityMatrix(float[][] sim, String outFile) throws IOException {
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(outFile)));
		
		for(int i=0; i<sim.length; i++) {
			
			String line = "" + sim[i][0];
			
			for(int j=1; j<sim[i].length; j++) {
				
				line += "," + sim[i][j];
				
			}
			bw.write(line);
			bw.newLine();
			
		}
		
		bw.close();
		
	}
	


}
