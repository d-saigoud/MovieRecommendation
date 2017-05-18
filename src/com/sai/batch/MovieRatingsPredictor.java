package com.sai.batch;

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
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class MovieRatingsPredictor {

	//Iterates through list of movies in a genre and Prints/Writes Complete Matrix - User Ids (Rows) by Movies (Columns)
	public static void main(String[] args) throws IOException {
		
		System.out.println("Process Started");
		Map<Integer, Map<Integer, Float>> ratingsMapByUserId = getRatingsMapByUserAsKey(
				"C:/Users/sai goud/Documents/Masters/Courses/Data Mining/ml-20m/ratings.csv");
		System.out.println("Ratings Dataset read complete");
		
		String outputDirPath = "C:\\Users\\sai goud\\Documents\\Masters\\Courses\\Data Mining\\ml-20m\\Movies split Files\\";
		
		File dir = new File(outputDirPath);
		
		List<File> inputFiles = new ArrayList<File>();
		
		for(File file : dir.listFiles()) {
			
			if(!file.getName().startsWith("ConvertMatrix")) {
				inputFiles.add(file);
			}
			
		}
		
		for(File file : inputFiles) {
			
			String outFilePath = outputDirPath + "\\ConvertMatrixFull_" + file.getName();
			
			Map<Integer, Integer> movieIdsMap = getMovieIdsTreeMap(file.getAbsolutePath());
			
			System.out.println(file.getName() + " Dataset read complete");
			
			convertMatrix(ratingsMapByUserId, outFilePath, movieIdsMap);
			
			System.out.println(file.getName().replaceAll(".csv", "") + " Output Matrix Printed");
		}
		
	}
	
	//For Printing/Writing Complete Matrix - User Ids (Rows) by Movies (Columns)
	public static void main1(String[] args) throws IOException {
		
		//Set<Integer> movieIdsSet = getMovieIdsTreeSet("C:/Users/sai goud/Documents/Masters/Courses/Data Mining/ml-20m/movies.csv");
		//convertMatrix("C:/Users/sai goud/Documents/Masters/Courses/Data Mining/ml-20m/ratings.csv", "C:/Users/sai goud/Documents/Masters/Courses/Data Mining/ml-20m/ConvertedMatrix.csv", movieIdsSet);
		System.out.println("Process Started");
		Map<Integer, Map<Integer, Float>> ratingsMapByUserId = getRatingsMapByMovieAsKey("C:/Users/sai goud/Documents/Masters/Courses/Data Mining/ml-20m/ratings.csv");
		System.out.println("Ratings Dataset read successfully");
		Map<Integer, Integer> movieIdsMap = getMovieIdsTreeMap("C:/Users/sai goud/Documents/Masters/Courses/Data Mining/ml-20m/movies.csv");
		System.out.println("Movies Dataset read successfully");
		convertMatrix(ratingsMapByUserId, "C:/Users/sai goud/Documents/Masters/Courses/Data Mining/ml-20m/ConvertedMatrix.csv", movieIdsMap);
		System.out.println("Process Completed");
	}
	
	public static void convertMatrix(String inFilePath, String outFilePath, Set<Integer> movieIdsSet) throws NumberFormatException, IOException {
		
		BufferedReader br = new BufferedReader(new FileReader(new File(inFilePath)));
		BufferedWriter bw = new BufferedWriter(new BufferedWriter(new FileWriter(new File(outFilePath))));
		
		String line = br.readLine();
		String[] splits = line.trim().split(",");
		
		int presentUserId = 0;
		int previousUserId = -1;
		int outLinesCount = 0;
		
		Map<Integer, String> singleUserMovieRatingsMap = new HashMap<Integer, String>();
		
		while((line=br.readLine()) != null) {
			
			splits = line.trim().split(",");
			presentUserId = Integer.parseInt(splits[0]);
			int presentMovieId = Integer.parseInt(splits[1]);
			
			//if(presentUserId > 21) {
			//	break;
			//}
			
			
			if(presentUserId != previousUserId && previousUserId != -1) {
				
				String outLine = "";
				
				for(int movieId : movieIdsSet) {
					
					if(outLine.length() > 0) {
						outLine += ",";
					}
					
					if(singleUserMovieRatingsMap.containsKey(movieId)) {
						
						outLine += singleUserMovieRatingsMap.get(movieId);
						
					}
					else {
						outLine += "-1";
					}
					
				}
				
				bw.write(previousUserId + "," + outLine);
				bw.newLine();
				outLinesCount++;
				
				if(outLinesCount == 10) {
					bw.close();
					bw = new BufferedWriter(new BufferedWriter(new FileWriter(new File(outFilePath), true)));
					outLinesCount = 0;
					System.out.println("Writter next 10");
				}
				
				singleUserMovieRatingsMap = new HashMap<Integer, String>();
				singleUserMovieRatingsMap.put(presentMovieId, splits[2]);
				
				
			}
			else{
				singleUserMovieRatingsMap.put(presentMovieId, splits[2]);
			}
			
			previousUserId = presentUserId;
			
			
		}
		
		String outLine = "";
		
		for(int movieId : movieIdsSet) {
			
			if(outLine.length() > 0) {
				outLine += ",";
			}
			
			if(singleUserMovieRatingsMap.containsKey(movieId)) {
				
				outLine += singleUserMovieRatingsMap.get(movieId);
				
			}
			else {
				
				outLine += "-1";
				
			}
			
		}
		
		bw.write(previousUserId + "," + outLine);
		bw.newLine();
		
		bw.close();
		br.close();
		
	}

	public static void convertMatrix(Map<Integer, Map<Integer, Float>> ratingsMapByUserId, String outFilePath, Map<Integer, Integer> movieIdsMap) throws NumberFormatException, IOException {
		
		BufferedWriter bw = new BufferedWriter(new BufferedWriter(new FileWriter(new File(outFilePath))));
		
		String outLine = ",";
		
		for(int movieId : movieIdsMap.keySet()) {
			
			outLine += ",m" + movieId;
			
		}
		
		bw.write(outLine);
		bw.newLine();
		
		for(int userId: ratingsMapByUserId.keySet()) {
			
			
			
			for(int movieId : movieIdsMap.keySet()) {
				
				if(outLine.length() > 0) {
					outLine += ",";
				}
				
				if(ratingsMapByUserId.get(userId).containsKey(movieId)) {
					
					outLine += ratingsMapByUserId.get(userId).containsKey(movieId);
					
				}
				else {
					
					outLine += "-1";
					
				}
				
			}
			
			bw.write(userId + "," + outLine);
			bw.newLine();
			
			
		}
		
	}

	
	public static Map<Integer, Integer> getMovieIdsTreeMap(String filePath) throws IOException {
		
		Map<Integer, Integer> movieIdsMap = new TreeMap<Integer, Integer>();
		BufferedReader br = new BufferedReader(new FileReader(new File(filePath)));
		
		String line = br.readLine();
		
		while((line=br.readLine()) != null) {
			
			String[] splits = line.trim().split(",");
			int movieId = Integer.parseInt(splits[0]);
			movieIdsMap.put(movieId, movieId);
			
		}
		
		br.close();
		
		return movieIdsMap;
		
	}
	
	public static Map<Integer, Map<Integer, Float>> getRatingsMapByUserAsKey(String filePath) throws IOException {
		
		Map<Integer, Map<Integer, Float>> ratingsMapByUserId = new TreeMap<Integer, Map<Integer, Float>>();
		
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
				
				if(!ratingsMapByUserId.containsKey(userId)) {
					
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
