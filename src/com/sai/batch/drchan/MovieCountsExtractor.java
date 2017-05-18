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


/* This Class is for extracting Movie, ratings counts on genre basis
 * 
 * 
 */

public class MovieCountsExtractor {

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
		
		Map<Integer, Map<Integer, Float>> ratingsMapByMovieId = getRatingsMapByMovieAsKey(
				"C:/Users/sai goud/Documents/Masters/Courses/Data Mining/ml-20m/ratings.csv");
		
		Map<Integer, Integer> considerableUserIds = getConsiderableUserIds("C:/Users/sai goud/Documents/Masters/Courses/Data Mining/ml-20m/Movies split Files/MovieRatingsCountWithUserID_Cluster_1.csv");
		
		for(File file : inputFiles) {
			
			System.out.println("***************************************************************************************************");
			
			String genre = file.getName().replaceAll("ConvertMatrix_", "");
			
			System.out.println("Genre : " + genre);
			
			if(genre.equals("(no genres listed).csv")) {
				System.out.println("Skipped : " + genre);
				continue;
			}
			
			String genreFile = file.getAbsolutePath().replaceAll("ConvertMatrix_", "");
			
			String outFile = file.getAbsolutePath().replaceAll("ConvertMatrix_", "MovieCountsByGenre_");
			
			Map<Integer, Integer> genreMovieIdsMap = getMovieIdsTreeMap(genreFile);
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(outFile)));
			
			for(int genreMovieId : genreMovieIdsMap.keySet()) {
				
				if(ratingsMapByMovieId.containsKey(genreMovieId)) {
					
					int count = 0;
					
					for(int userId : considerableUserIds.keySet()) {
						
						if(ratingsMapByMovieId.get(genreMovieId).containsKey(userId)) {
							count++;
						}
						
					}
					
					if(count > 500) {
						bw.write(genreMovieId + "," + count);
						//bw.write("" + ratingsMapByMovieId.get(genreMovieId).size());
						bw.newLine();
					}
				}
				
			}
			
			bw.close();
			
			break;
			
		}
		
		
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
	
	public static Map<Integer, Integer> getConsiderableUserIds(String filePath) throws IOException {

		Map<Integer, Integer> userIdsIdsMap = new TreeMap<Integer, Integer>();
		BufferedReader br = new BufferedReader(new FileReader(new File(filePath)));

		String line = null;
		//String line = br.readLine();

		int i = 0;
		
		while ((line = br.readLine()) != null) {

			String[] splits = line.trim().split(",");
			int userId = Integer.parseInt(splits[0]);
			userIdsIdsMap.put(userId, i);
			
			i++;

		}

		br.close();

		return userIdsIdsMap;

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
