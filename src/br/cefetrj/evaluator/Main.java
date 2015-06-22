package br.cefetrj.evaluator;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import br.cefetrj.parser.FormulaEvaluator;

public class Main {
	private static String workFolder; // args[0]
	private static List<String> outputData = new ArrayList<String>();

	private static int getIndex(String key, String header) {
		int index = -1;
		String[] headers = header.split(",");
		for (int x = 0; x < headers.length; x++) {
			if (headers[x].equals(key)) {
				index = x;
			}
		}
		return index;
	}

	private static List<Double> convertToDouble(List<String> values)
			throws Exception {
		List<Double> result = new ArrayList<Double>();
		for (String value : values) {
			result.add(Double.valueOf(value));
		}
		return result;
	}

	private static List<Integer> convertToInteger(List<String> values)
			throws Exception {
		List<Integer> result = new ArrayList<Integer>();
		for (String value : values) {
			result.add(Integer.valueOf(value));
		}
		return result;
	}

	public static double evaluateOptimizationFunction(
			String optimizationFunction, Double[] valuesAdj,
			Double[] valuesLap, Double[] valuesSgnlap, Integer valueN,
			Integer valueM, Integer[] valuesD, Double[] valuesAdjBar,
			Double[] valuesLapBar, Double[] valuesSgnlapBar,
			Integer valueChiAdj, Integer valueChiAdjBar, Integer valueOmegaAdj,
			Integer valueOmegaAdjBar) {

		System.out.println("original optimization function: "
				+ optimizationFunction);

		String tmpStr;

		tmpStr = "n";
		optimizationFunction = optimizationFunction
				.replace(tmpStr, "" + valueN);

		tmpStr = "m";
		optimizationFunction = optimizationFunction
				.replace(tmpStr, "" + valueM);

		for (int i = 0; i < valuesAdj.length; i++) {
			tmpStr = "d_" + Integer.toString(i + 1);
			optimizationFunction = optimizationFunction.replace(tmpStr, ""
					+ valuesD[i]);
		}

		for (int i = 0; i < valuesAdj.length; i++) {
			tmpStr = "\\overline{q_" + Integer.toString(i + 1) + "}";
			optimizationFunction = optimizationFunction.replace(tmpStr, ""
					+ valuesSgnlapBar[i]);

			tmpStr = "\\overline{\\mu_" + Integer.toString(i + 1) + "}";
			optimizationFunction = optimizationFunction.replace(tmpStr, ""
					+ valuesLapBar[i]);

			tmpStr = "\\overline{\\lambda_" + Integer.toString(i + 1) + "}";
			optimizationFunction = optimizationFunction.replace(tmpStr, ""
					+ valuesAdjBar[i]);
		}

		tmpStr = "\\overline{\\chi}";
		optimizationFunction = optimizationFunction.replace(tmpStr,
				valueChiAdjBar.toString());

		tmpStr = "\\chi";
		optimizationFunction = optimizationFunction.replace(tmpStr,
				valueChiAdj.toString());

		tmpStr = "\\overline{\\omega}";
		optimizationFunction = optimizationFunction.replace(tmpStr,
				valueOmegaAdjBar.toString());

		tmpStr = "\\omega";
		optimizationFunction = optimizationFunction.replace(tmpStr,
				valueOmegaAdj.toString());

		for (int i = 0; i < valuesAdj.length; i++) {
			tmpStr = "\\overline{q_" + Integer.toString(i + 1) + "}";
			optimizationFunction = optimizationFunction.replace(tmpStr,
					valuesSgnlapBar[i].toString());

			tmpStr = "\\overline{\\mu_" + Integer.toString(i + 1) + "}";
			optimizationFunction = optimizationFunction.replace(tmpStr,
					valuesLapBar[i].toString());

			tmpStr = "\\overline{\\lambda_" + Integer.toString(i + 1) + "}";
			optimizationFunction = optimizationFunction.replace(tmpStr,
					valuesAdjBar[i].toString());
		}

		boolean haveLap = false;
		boolean haveSgnLap = false;
		boolean haveAdj = false;
		for (int i = 0; i < valuesAdj.length; i++) {

			int index = (valuesAdj.length - 1) - i;

			tmpStr = "q_" + Integer.toString(i + 1);
			if (valuesSgnlap.length > 0) {
				optimizationFunction = optimizationFunction.replace(tmpStr, ""
						+ valuesSgnlap[index]);
				haveSgnLap = true;
			}

			tmpStr = "\\mu_" + Integer.toString(i + 1);
			if (valuesLap.length > 0) {
				optimizationFunction = optimizationFunction.replace(tmpStr, ""
						+ valuesLap[index]);
				haveLap = true;
			}

			tmpStr = "\\lambda_" + Integer.toString(i + 1);
			if (valuesAdj.length > 0) {
				optimizationFunction = optimizationFunction.replace(tmpStr, ""
						+ valuesAdj[index]);
				haveAdj = true;
			}
		}

		if (haveSgnLap) {
			System.out.println("using signless Laplacian Matrix");
		} else {
			System.out.println("dont have signless Laplacian Matrix (Q)");
		}
		if (haveLap) {
			System.out.println("using Laplacian Matrix");
		} else {
			System.out.println("dont have Laplacian Matrix (Mu)");
		}
		if (haveAdj) {
			System.out.println("using Adjacency Matrix");
		} else {
			System.out.println("dont have Adjacency Matrix (Lambda)");
		}

		// optimizationFunction = optimizationFunction.replace("\\frac", "");
		// optimizationFunction = optimizationFunction.replaceAll(
		// "[}]{1,1}+[\\s]*+[{]{1,1}", ")/(");
		// optimizationFunction = optimizationFunction.replace("}", ")");
		// optimizationFunction = optimizationFunction.replace("{", "(");

		double result = 0.0;
		try {
			ByteArrayInputStream inputStream = new ByteArrayInputStream(
					optimizationFunction.getBytes());
			FormulaEvaluator eval = new FormulaEvaluator(inputStream);
			result = eval.evaluate();
		} catch (Throwable e) {
			System.out.println("FUNCTION ERROR: " + e.getMessage());
		}

		System.out.println("optimization function result: "
				+ optimizationFunction + " = " + result);

		return result;

	}

	public static void processJob(JobUnity job) throws Exception {
		List<Double> convertedAdj = new ArrayList<Double>();
		List<Double> convertedLap = new ArrayList<Double>();
		List<Double> convertedSgnLap = new ArrayList<Double>();

		List<Double> convertedAdjBar = new ArrayList<Double>();
		List<Double> convertedLapBar = new ArrayList<Double>();
		List<Double> convertedSgnLapBar = new ArrayList<Double>();

		List<Integer> convertedGreatestDegrees = new ArrayList<Integer>();

		if (job.isAdj()) {
			String adjFile = workFolder + File.separator + "inbox"
					+ File.separator + job.getAdjFile();
			convertedAdj = convertToDouble(readFile(adjFile));
		}

		if (job.isLap()) {
			String lapFile = workFolder + File.separator + "inbox"
					+ File.separator + job.getLapFile();
			convertedLap = convertToDouble(readFile(lapFile));
		}

		if (job.isSgnLap()) {
			String sgnlapFile = workFolder + File.separator + "inbox"
					+ File.separator + job.getSgnLapFile();
			convertedSgnLap = convertToDouble(readFile(sgnlapFile));
		}

		if (job.isAdjBar()) {
			String adjBarFile = workFolder + File.separator + "inbox"
					+ File.separator + job.getAdjBarFile();
			convertedAdjBar = convertToDouble(readFile(adjBarFile));
		}

		if (job.isLapBar()) {
			String lapBarFile = workFolder + File.separator + "inbox"
					+ File.separator + job.getLapBarFile();
			convertedLapBar = convertToDouble(readFile(lapBarFile));
		}

		if (job.isSgnLapBar()) {
			String sgnlapBarFile = workFolder + File.separator + "inbox"
					+ File.separator + job.getSgnLapBarFile();
			convertedSgnLapBar = convertToDouble(readFile(sgnlapBarFile));
		}

		if (job.isUsingGreatestDegrees()) {
			String greatestDegreesFile = workFolder + File.separator + "inbox"
					+ File.separator + job.getGreatestDegreesFile();
			convertedGreatestDegrees = convertToInteger(readFile(greatestDegreesFile));
		}

		if (job.isUsingLargestCliqueSize()) {
			String largestCliqueSizeFile = workFolder + File.separator + "inbox"
					+ File.separator + job.getLargestCliqueSizeFile();
			convertedLargestCliqueSize = convertToInteger(readFile(largestCliqueSizeFile));
		}

		if (job.isUsingChromaticNumber()) {
		}

		if (job.isUsingLargestCliqueSizeBar()) {
		}

		if (job.isUsingChromaticNumberBar()) {
		}

		if (job.isUsingNumberOfVerticesAdj()) {
		}

		if (job.isUsingNumberOfEdgesAdj()) {
		}

		Double[] valuesAdj = new Double[convertedAdj.size()];
		valuesAdj = convertedAdj.toArray(valuesAdj);

		Double[] valuesLap = new Double[convertedLap.size()];
		valuesLap = convertedLap.toArray(valuesLap);

		Double[] valuesSgnLap = new Double[convertedSgnLap.size()];
		valuesSgnLap = convertedSgnLap.toArray(valuesSgnLap);

		Double[] valuesAdjBar = new Double[convertedAdjBar.size()];
		valuesAdjBar = convertedAdjBar.toArray(valuesAdjBar);

		Double[] valuesLapBar = new Double[convertedLapBar.size()];
		valuesLapBar = convertedLapBar.toArray(valuesLapBar);

		Double[] valuesSgnLapBar = new Double[convertedSgnLapBar.size()];
		valuesSgnLapBar = convertedSgnLapBar.toArray(valuesSgnLapBar);

		Integer valueM;
		Integer valueN;

		Integer[] valuesD = new Integer[convertedGreatestDegrees.size()];
		valuesD = convertedGreatestDegrees.toArray(valuesD);

		Integer valueChiAdjBar;
		Integer valueChiAdj;
		Integer valueOmegaAdjBar;
		Integer valueOmegaAdj;

		Double evaluatedValue = evaluateOptimizationFunction(
				job.getOptimizationFunction(), valuesAdj, valuesLap,
				valuesSgnLap, valueN, valueM, valuesD, valuesAdjBar,
				valuesLapBar, valuesSgnLapBar, valueChiAdj, valueChiAdjBar,
				valueOmegaAdj, valueOmegaAdjBar);

		// Send back original data plus file name
		outputData.add("optifunc,g6fileid,evaluatedvalue,maxresults");
		outputData.add(job.getOptimizationFunction() + "," + job.getG6fileid()
				+ "," + evaluatedValue + "," + job.getMaxResults());
		saveOutput();

	}

	public static void saveOutput() throws FileNotFoundException {
		PrintWriter pw = new PrintWriter(new FileOutputStream(workFolder
				+ "/sagi_output.txt"));
		for (String line : outputData) {
			pw.println(line);
		}
		pw.close();
	}

	public static void saveFile(List<String> data, String fileName)
			throws FileNotFoundException {
		PrintWriter pw = new PrintWriter(new FileOutputStream(workFolder
				+ File.separator + "outbox" + File.separator + fileName));
		for (String line : data) {
			pw.println(line);
		}
		pw.close();
	}

	public static void main(String[] args) throws Exception {
		workFolder = args[0];

		List<String> inputData = readFile(workFolder + File.separator
				+ "sagi_input.txt");
		if (inputData.size() > 1) {
			String header = inputData.get(0); // Get the CSV header

			JobUnity job = new JobUnity();

			for (int x = 1; x < inputData.size(); x++) { // REDUCE read all
															// lines
				String line = inputData.get(x);
				String[] lineData = line.split(",");
				String inputFile = lineData[getIndex("eigsolve", header)]; // Get
																			// the
																			// eigsolve
																			// file
				String optimizationFunction = lineData[getIndex("optifunc",
						header)]; // Get the function
				String g6fileid = lineData[getIndex("g6fileid", header)]; // Get
																			// the
																			// source
																			// file
																			// reference
				String maxResults = lineData[getIndex("maxresults", header)]; // Get
																				// Maximun
																				// results
																				// to
																				// show

				job.setOptimizationFunction(optimizationFunction);
				job.setHeader(header);
				job.setG6fileid(g6fileid);
				job.setMaxResults(maxResults);

				if (inputFile.contains(".lap")) {
					job.setLapFile(inputFile);
				}
				if (inputFile.contains(".adj")) {
					job.setAdjFile(inputFile);
				}
				if (inputFile.contains(".sgnlap")) {
					job.setSgnLapFile(inputFile);
				}

			}

			processJob(job);

		} else {
			System.out.println("Empty input data file.");
		}

	}

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

}
