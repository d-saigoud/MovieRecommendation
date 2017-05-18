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
import java.util.Set;
import java.util.TreeMap;

public class MovieSimilarityNew {

	// Write Vectors/Similarity to Files for movies by Genre
	public static void main(String[] args) throws IOException {

		/*
		 * String inFilePath =
		 * "C:\\Users\\sai goud\\Documents\\Masters\\Courses\\Data Mining\\ml-20m\\ConvertedMatrix.csv"
		 * ;
		 * 
		 * String outFilePath =
		 * "C:\\Users\\sai goud\\Documents\\Masters\\Courses\\Data Mining\\ml-20m\\SimilarityMatrix.csv"
		 * ;
		 * 
		 * 
		 * Set<Integer> movieIdsSet = getMovieIdsTreeSet(
		 * "C:\\Users\\sai goud\\Documents\\Masters\\Courses\\Data Mining\\ml-20m\\movies.csv"
		 * ); System.out.println(movieIdsSet);
		 * 
		 * findSimilarities(inFilePath, outFilePath, movieIdsSet);
		 */

		System.out.println("Process Started");
		//Map<Integer, Map<Integer, Float>> ratingsMapByMovieId = getRatingsMapByMovieAsKey(
		//		"C:/Users/sai goud/Documents/Masters/Courses/Data Mining/ml-20m/ratings.csv");
		Map<Integer, Map<Integer, Float>> ratingsMapByMovieId = getRatingsMapByMovieAsKey(
				"C:/Users/sai goud/Documents/Masters/Courses/Data Mining/ml-20m//Movies split Files/TestRatingsDatasetForEval_Action.csv");
		System.out.println("Ratings Dataset read complete");

		String outputDirPath = "C:\\Users\\sai goud\\Documents\\Masters\\Courses\\Data Mining\\ml-20m\\Movies split Files\\";

		String outFilePath = outputDirPath + "\\SimilarityMatrix_ForFinalEval_action.csv";

		Map<Integer, Integer> movieIdsMap = getMovieIdsTreeMap(outputDirPath + "\\MovieCountsByGenre_Action.csv");
		
		Map<Integer, Integer> considerableUserIdsMap = getConsiderableUserIdsTreeMap(outputDirPath + "\\UsersToBeConsideredForEvaluation.csv");

		// printConvertedMatrixForEachGenre(outFilePath, ratingsMapByMovieId,
		// movieIdsMap);

		findSimilarities(ratingsMapByMovieId, outFilePath, movieIdsMap, considerableUserIdsMap);

		/*
		 * String outFilePath =
		 * "C:\\Users\\sai goud\\Documents\\Masters\\Courses\\Data Mining\\ml-20m\\Movies split Files\\SimilarityMatrix.csv"
		 * ; Map<Integer, Integer> movieIdsMap = getMovieIdsTreeMap(
		 * "C:\\Users\\sai goud\\Documents\\Masters\\Courses\\Data Mining\\ml-20m\\Movies split Files\\movies.csv"
		 * ); System.out.println("Movies Dataset read complete"); Map<Integer,
		 * Map<Integer, Float>> ratingsMapByMovieId = getRatingsMapByMovieAsKey(
		 * "C:/Users/sai goud/Documents/Masters/Courses/Data Mining/ml-20m/ratings.csv"
		 * ); System.out.println("Ratings Dataset read complete"); //
		 * System.out.println(ratingsMapByMovieId);
		 * findSimilarities(ratingsMapByMovieId, outFilePath, movieIdsMap);
		 */
	}

	public static void findSimilarities(String inFilePath, String outFilePath, Set<Integer> movieIdsSet)
			throws IOException {

		String line = "";

		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(outFilePath)));

		for (int i = 1; i <= movieIdsSet.size(); i++) {

			line = "";

			for (int j = 1; j < i; j++) {

				if (line.length() > 0) {
					line += ",";
				}

				line += "0";

			}

			if (line.length() > 0) {
				line += ",";
			}

			line += 1;

			for (int j = i + 1; j <= movieIdsSet.size(); j++) {

				line += ",";

				List<List<Float>> listOfMovieVectors = prepareMovieVectors(inFilePath, i, j);
				float similarity = getSimilarity(listOfMovieVectors);

				line += similarity;

				System.out.println("Similarity " + i + "-" + j + ":" + similarity);

				// System.out.println(listOfMovieVectors);

				// System.out.println(similarity);
			}

			bw.write(line);
			bw.newLine();
			bw.close();

			//bw = new BufferedWriter(new FileWriter(new File(outFilePath), true));

		}

		bw.close();

	}

	public static void findSimilarities(Map<Integer, Map<Integer, Float>> ratingsMapByMovieId, String outFilePath,
			Map<Integer, Integer> movieIdsMap, Map<Integer, Integer> considerableUserIdsMap) throws IOException {

		String line = "";

		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(outFilePath)));

		for (int movie1 : movieIdsMap.keySet()) {

			line = "";

			for (int movie2 : movieIdsMap.keySet()) {

				if (movie2 >= movie1) {
					break;
				}

				if (line.length() > 0) {
					line += ",";
				}

				line += "0";

			}

			if (line.length() > 0) {
				line += ",";
			}

			line += 1;

			for (int movie2 : movieIdsMap.keySet()) {

				if (movie1 >= movie2) {
					continue;
				}

				line += ",";
				// System.out.println("movieIdsMap.get(i) : " +
				// movieIdsMap.get(i));

				System.out.println("Similarity " + movie1 + "-" + movie2 + ":");

				float similarity = 0;

				if (ratingsMapByMovieId.get(movie1) != null && ratingsMapByMovieId.get(movie2) != null) {
					List<List<Float>> listOfMovieVectors = prepareMovieVectors(ratingsMapByMovieId, movie1, movie2, considerableUserIdsMap);
					similarity = getSimilarity(listOfMovieVectors);
				}

				line += similarity;

				System.out.println("Similarity " + movie1 + "-" + movie2 + ":" + similarity);
				// System.out.println(ratingsMapByMovieId.get(i) + " : " +
				// ratingsMapByMovieId.get(j));

				// System.out.println(listOfMovieVectors);

				// System.out.println(similarity);
			}

			bw.write(line);
			bw.newLine();
			bw.close();

			bw = new BufferedWriter(new FileWriter(new File(outFilePath), true));

		}

		bw.close();

	}

	public static float getSimilarity(List<List<Float>> listOfMovieVectors) {

		List<Float> listVectorForMovieIndex1 = listOfMovieVectors.get(0);
		List<Float> listVectorForMovieIndex2 = listOfMovieVectors.get(1);

		double numerator = 0f;
		double denominator1 = 0f;
		double denominator2 = 0f;

		for (int i = 0; i < listVectorForMovieIndex1.size(); i++) {

			numerator += (listVectorForMovieIndex1.get(i) * listVectorForMovieIndex2.get(i));
			denominator1 += (listVectorForMovieIndex1.get(i) * listVectorForMovieIndex1.get(i));
			denominator2 += (listVectorForMovieIndex2.get(i) * listVectorForMovieIndex2.get(i));
		}

		return listVectorForMovieIndex1.size() > 1
				? (float) Math.round((numerator * 100 / Math.sqrt(denominator1 * denominator2))) / 100 : 0;

	}

	public static List<List<Float>> prepareMovieVectors(String filePath, int movieIndex1, int movieIndex2)
			throws IOException {

		List<List<Float>> listOfMovieVectors = new ArrayList<List<Float>>();
		List<Float> listVectorForMovieIndex1 = new ArrayList<Float>();
		List<Float> listVectorForMovieIndex2 = new ArrayList<Float>();

		BufferedReader br = new BufferedReader(new FileReader(new File(filePath)));

		String line = "";

		while ((line = br.readLine()) != null) {

			String[] splits = line.trim().split(",");

			if (!splits[movieIndex1].equals("-1") && !splits[movieIndex2].equals("-1")) {

				Float movieRating1 = Float.parseFloat(splits[movieIndex1]);
				Float movieRating2 = Float.parseFloat(splits[movieIndex2]);

				listVectorForMovieIndex1.add(movieRating1);
				listVectorForMovieIndex2.add(movieRating2);

			}

		}

		listOfMovieVectors.add(listVectorForMovieIndex1);
		listOfMovieVectors.add(listVectorForMovieIndex2);

		br.close();

		return listOfMovieVectors;

	}

	public static List<List<Float>> prepareMovieVectors(Map<Integer, Map<Integer, Float>> ratingsMapByMovieId, int i,
			int j, Map<Integer, Integer> considerableUserIdsMap) {

		List<List<Float>> listOfMovieVectors = new ArrayList<List<Float>>();
		List<Float> listVectorForMovieIndex1 = new ArrayList<Float>();
		List<Float> listVectorForMovieIndex2 = new ArrayList<Float>();

		for (int userId : ratingsMapByMovieId.get(i).keySet()) {

			// System.out.println("j : " + j);
			// System.out.println(ratingsMapByMovieId.get(j));

			if (considerableUserIdsMap.containsKey(userId) && ratingsMapByMovieId.get(j).containsKey(userId)) {
				listVectorForMovieIndex1.add(ratingsMapByMovieId.get(i).get(userId));
				listVectorForMovieIndex2.add(ratingsMapByMovieId.get(j).get(userId));
			}

		}

		listOfMovieVectors.add(listVectorForMovieIndex1);
		listOfMovieVectors.add(listVectorForMovieIndex2);

		return listOfMovieVectors;
	}

	public static Map<Integer, Integer> getMovieIdsTreeMap(String filePath) throws IOException {

		Map<Integer, Integer> movieIdsMap = new TreeMap<Integer, Integer>();
		BufferedReader br = new BufferedReader(new FileReader(new File(filePath)));

		String line = br.readLine();

		while ((line = br.readLine()) != null) {

			String[] splits = line.trim().split(",");
			int movieId = Integer.parseInt(splits[0]);
			movieIdsMap.put(movieId, movieId);

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

	public static void printConvertedMatrixForEachGenre(String outFilePath,
			Map<Integer, Map<Integer, Float>> ratingsMapByMovieId, Map<Integer, Integer> movieIdsMap)
			throws IOException {

		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(outFilePath)));

		for (int movieId : movieIdsMap.keySet()) {

			if (ratingsMapByMovieId.containsKey(movieId)) {
				String line = "m" + movieId;

				for (int userId : ratingsMapByMovieId.get(movieId).keySet()) {

					line += "," + ratingsMapByMovieId.get(movieId).get(userId) + "u" + userId;

				}

				bw.write(line);
				bw.newLine();

			}

		}

		bw.close();

	}

}
