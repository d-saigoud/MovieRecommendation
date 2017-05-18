package com.sai.batch;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class MovieDescriptionCrawler {

	public static void main(String[] args) throws IOException {

		startMovieDescriptonCrawlingProcess("C:/Users/sai goud/Documents/Masters/Courses/Data Mining/ml-20m/links.csv",
				"C:/Users/sai goud/Documents/Masters/Courses/Data Mining/ml-20m/movieIdDescriptionCrawled.csv");

		
	}

	public static void startMovieDescriptonCrawlingProcess(String filePath1, String filePath2) throws IOException {

		BufferedReader br = new BufferedReader(new FileReader(new File(filePath1)));
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(filePath2)));
		String line = br.readLine();

		WebClient webClient = new WebClient();
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		webClient.setJavaScriptErrorListener(null);
		// webClient.setCssErrorHandler(null);

		int count = 0;

		while ((line = br.readLine()) != null) {

			String[] splits = line.split(",");
			System.out.println("http://www.imdb.com/title/tt" + splits[1] + "/");

			String description = doActualCrawl(webClient,
					"http://www.imdb.com/title/tt" + splits[1] + "/synopsis?ref_=tt_stry_pl", "swiki.2.1");

			bw.write(line);
			bw.newLine();
			
			bw.write(description);
			bw.newLine();
			
			bw.write("==========");
			bw.newLine();
			
			
			

			count++;

			if (count % 100 == 0) {
				bw.close();
				bw = new BufferedWriter(new FileWriter(new File(filePath2), true));
				count = 0;
			}
			
			//break;

		}
		bw.close();
		br.close();
	}

	public static String doActualCrawl(WebClient webClient, String url, String divElementId) {

		try {
			HtmlPage htmlPage = webClient.getPage(url);
			DomElement divDomElement = htmlPage.getElementById(divElementId);
			System.out.println(divDomElement.getTextContent());

			return divDomElement.getTextContent();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}

	}

}
