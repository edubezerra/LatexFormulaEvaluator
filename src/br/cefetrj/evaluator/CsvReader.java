package br.cefetrj.evaluator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class CsvReader {

	/**
	 * This is a method to read the CSV data
	 * 
	 * @param file
	 * @return StringBuilder : The file data as a list of lines.
	 * @throws Exception
	 */
	public static List<String> readFile(String file) throws Exception {
		String line = "";
		ArrayList<String> list = new ArrayList<String>();
		BufferedReader br = new BufferedReader(new FileReader(file));
		while ((line = br.readLine()) != null) {
			list.add(line);
		}
		if (br != null) {
			br.close();
		}
		return list;
	}

	public static int getIndex(String key, String header) {
		int index = -1;
		String[] headers = header.split(",");
		for (int x = 0; x < headers.length; x++) {
			if (headers[x].equals(key)) {
				index = x;
			}
		}
		return index;
	}
}
