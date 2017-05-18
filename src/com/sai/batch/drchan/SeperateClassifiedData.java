package com.sai.batch.drchan;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

public class SeperateClassifiedData {

	public static void main(String[] args) throws IOException {
		
		//mergeColumns("C:\\Users\\sai goud\\Documents\\Masters\\Courses\\Data Mining\\ml-20m\\Movies split Files\\UserIDMovieRatedCount_Action.csv", "C:\\Users\\sai goud\\Documents\\Masters\\Courses\\Data Mining\\ml-20m\\Movies split Files\\UserMovieRatedCountClusterAssignments.csv", "C:\\Users\\sai goud\\Documents\\Masters\\Courses\\Data Mining\\ml-20m\\Movies split Files\\MovieRatingsCountWithClusterNumber.csv");
		//mergeColumns("C:\\Users\\sai goud\\Documents\\Masters\\Courses\\Data Mining\\ml-20m\\Movies split Files\\UserIDAvgRating_Action.csv", "C:\\Users\\sai goud\\Documents\\Masters\\Courses\\Data Mining\\ml-20m\\Movies split Files\\UserMovieAvgRatingClusterAssignments.csv", "C:\\Users\\sai goud\\Documents\\Masters\\Courses\\Data Mining\\ml-20m\\Movies split Files\\MovieRatingsAverageWithClusterNumber.csv");
		
		//<Integer, Map<Integer, String>> clusterNumberToUserIDsMap = getClusterToUserIdsMap("C:\\Users\\sai goud\\Documents\\Masters\\Courses\\Data Mining\\ml-20m\\Movies split Files\\MovieRatingsCountWithClusterNumber.csv");
		//writeClustersToDifferentFiles(clusterNumberToUserIDsMap, "C:\\Users\\sai goud\\Documents\\Masters\\Courses\\Data Mining\\ml-20m\\Movies split Files\\MovieRatingsCountWithUserID_Cluster_####.csv");
		Map<Integer, Map<Integer, String>> clusterNumberToUserIDsMap = getClusterToUserIdsMap("C:\\Users\\sai goud\\Documents\\Masters\\Courses\\Data Mining\\ml-20m\\Movies split Files\\MovieRatingsAverageWithClusterNumber.csv");
		writeClustersToDifferentFiles(clusterNumberToUserIDsMap, "C:\\Users\\sai goud\\Documents\\Masters\\Courses\\Data Mining\\ml-20m\\Movies split Files\\MovieRatingsAvgWithUserID_Cluster_####.csv");
	
	}
	
	public static void mergeColumns(String filePath1, String filePath2, String outFilePath) throws IOException {
	
		BufferedReader br1 = new BufferedReader(new FileReader(new File(filePath1)));
		BufferedReader br2 = new BufferedReader(new FileReader(new File(filePath2)));
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(outFilePath)));
		
		String line;
		
		while((line=br1.readLine()) != null) {
			
			line = line.trim() + "," + br2.readLine().trim();
			
			bw.write(line);
			bw.newLine();
		
		}
		
		bw.close();
		
		br1.close();
		br2.close();
	}
	
	public static Map<Integer, Map<Integer, String>> getClusterToUserIdsMap(String filePath) throws IOException {
		
		Map<Integer, Map<Integer, String>> clusterNumberToUserIDsMap =  new TreeMap<Integer, Map<Integer, String>>();
		
		BufferedReader br = new BufferedReader(new FileReader(new File(filePath)));
		
		
		String line;
		
		while((line=br.readLine()) != null) {
			
			line = line.trim();
			
			if(line.length() > 0) {
				
				String[] splits = line.split(",");
				
				int clusterNo = Integer.parseInt(splits[2]);
				int userID = Integer.parseInt(splits[0]);
				
				if(!clusterNumberToUserIDsMap.containsKey(clusterNo)) {
					clusterNumberToUserIDsMap.put(clusterNo, new TreeMap<Integer, String>());
				}
				
				clusterNumberToUserIDsMap.get(clusterNo).put(userID, splits[1]);
				
			}
			
		}
		
		
		br.close();
		
		
		return clusterNumberToUserIDsMap;
		
	}
	
	public static void writeClustersToDifferentFiles(Map<Integer, Map<Integer, String>> clusterNumberToUserIDsMap, String outFilePath) throws IOException {
		
		
		for(int clusterNumber : clusterNumberToUserIDsMap.keySet()) {
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(outFilePath.replaceAll("####", "" + clusterNumber))));
			
			for(int userId :clusterNumberToUserIDsMap.get(clusterNumber).keySet()) {
				bw.write(userId + "," + clusterNumberToUserIDsMap.get(clusterNumber).get(userId));
				bw.newLine();
			}
			
			bw.close();
			
		}
		
		
		
	}
	
}
