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

	public static double evaluateOptimizationFunction(EvaluationInfo evalInfo) {

		String optimizationFunction = evalInfo.optimizationFunction;
		System.out.println("original optimization function: "
				+ optimizationFunction);

		String tmpStr;

		for (int i = 0; i < evalInfo.valuesAdjs.length; i++) {
			tmpStr = "d_" + Integer.toString(i + 1);
			optimizationFunction = optimizationFunction.replace(tmpStr, ""
					+ evalInfo.valuesDs[i]);
		}

		for (int i = 0; i < evalInfo.valuesAdjs.length; i++) {
			tmpStr = "\\overline{q_" + Integer.toString(i + 1) + "}";
			optimizationFunction = optimizationFunction.replace(tmpStr, ""
					+ evalInfo.valuesSgnlapBars[i]);

			tmpStr = "\\overline{\\mu_" + Integer.toString(i + 1) + "}";
			optimizationFunction = optimizationFunction.replace(tmpStr, ""
					+ evalInfo.valuesLapBars[i]);

			tmpStr = "\\overline{\\lambda_" + Integer.toString(i + 1) + "}";
			optimizationFunction = optimizationFunction.replace(tmpStr, ""
					+ evalInfo.valuesAdjBars[i]);
		}

		tmpStr = "\\overline{\\chi}";
		optimizationFunction = optimizationFunction.replace(tmpStr,
				evalInfo.valueChiAdjBar.toString());

		tmpStr = "\\chi";
		optimizationFunction = optimizationFunction.replace(tmpStr,
				evalInfo.valueChiAdj.toString());

		tmpStr = "\\overline{\\omega}";
		optimizationFunction = optimizationFunction.replace(tmpStr,
				evalInfo.valueOmegaAdjBar.toString());

		tmpStr = "\\omega";
		optimizationFunction = optimizationFunction.replace(tmpStr,
				evalInfo.valueOmegaAdj.toString());

		tmpStr = "n";
		optimizationFunction = optimizationFunction.replace(tmpStr, ""
				+ evalInfo.numVertices);

		tmpStr = "m";
		optimizationFunction = optimizationFunction.replace(tmpStr, ""
				+ evalInfo.numEdges);

		for (int i = 0; i < evalInfo.valuesAdjs.length; i++) {
			tmpStr = "\\overline{q_" + Integer.toString(i + 1) + "}";
			optimizationFunction = optimizationFunction.replace(tmpStr,
					evalInfo.valuesSgnlapBars[i].toString());

			tmpStr = "\\overline{\\mu_" + Integer.toString(i + 1) + "}";
			optimizationFunction = optimizationFunction.replace(tmpStr,
					evalInfo.valuesLapBars[i].toString());

			tmpStr = "\\overline{\\lambda_" + Integer.toString(i + 1) + "}";
			optimizationFunction = optimizationFunction.replace(tmpStr,
					evalInfo.valuesAdjBars[i].toString());
		}

		boolean haveLap = false;
		boolean haveSgnLap = false;
		boolean haveAdj = false;
		for (int i = 0; i < evalInfo.valuesAdjs.length; i++) {

			int index = (evalInfo.valuesAdjs.length - 1) - i;

			tmpStr = "q_" + Integer.toString(i + 1);
			if (evalInfo.valuesSgnlaps.length > 0) {
				optimizationFunction = optimizationFunction.replace(tmpStr, ""
						+ evalInfo.valuesSgnlaps[index]);
				haveSgnLap = true;
			}

			tmpStr = "\\mu_" + Integer.toString(i + 1);
			if (evalInfo.valuesLaps.length > 0) {
				optimizationFunction = optimizationFunction.replace(tmpStr, ""
						+ evalInfo.valuesLaps[index]);
				haveLap = true;
			}

			tmpStr = "\\lambda_" + Integer.toString(i + 1);
			if (evalInfo.valuesAdjs.length > 0) {
				optimizationFunction = optimizationFunction.replace(tmpStr, ""
						+ evalInfo.valuesAdjs[index]);
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
			convertedAdj = convertToDouble(CsvReader.readFile(adjFile));
		}

		if (job.isLap()) {
			String lapFile = workFolder + File.separator + "inbox"
					+ File.separator + job.getLapFile();
			convertedLap = convertToDouble(CsvReader.readFile(lapFile));
		}

		if (job.isSgnLap()) {
			String sgnlapFile = workFolder + File.separator + "inbox"
					+ File.separator + job.getSgnLapFile();
			convertedSgnLap = convertToDouble(CsvReader.readFile(sgnlapFile));
		}

		if (job.isAdjBar()) {
			String adjBarFile = workFolder + File.separator + "inbox"
					+ File.separator + job.getAdjBarFile();
			convertedAdjBar = convertToDouble(CsvReader.readFile(adjBarFile));
		}

		if (job.isLapBar()) {
			String lapBarFile = workFolder + File.separator + "inbox"
					+ File.separator + job.getLapBarFile();
			convertedLapBar = convertToDouble(CsvReader.readFile(lapBarFile));
		}

		if (job.isSgnLapBar()) {
			String sgnlapBarFile = workFolder + File.separator + "inbox"
					+ File.separator + job.getSgnLapBarFile();
			convertedSgnLapBar = convertToDouble(CsvReader
					.readFile(sgnlapBarFile));
		}

		String[] valuesAdj = new String[convertedAdj.size()];
		valuesAdj = convertedAdj.toArray(valuesAdj);

		String[] valuesLap = new String[convertedLap.size()];
		valuesLap = convertedLap.toArray(valuesLap);

		String[] valuesSgnLap = new String[convertedSgnLap.size()];
		valuesSgnLap = convertedSgnLap.toArray(valuesSgnLap);

		String[] valuesAdjBar = new String[convertedAdjBar.size()];
		valuesAdjBar = convertedAdjBar.toArray(valuesAdjBar);

		String[] valuesLapBar = new String[convertedLapBar.size()];
		valuesLapBar = convertedLapBar.toArray(valuesLapBar);

		String[] valuesSgnLapBar = new String[convertedSgnLapBar.size()];
		valuesSgnLapBar = convertedSgnLapBar.toArray(valuesSgnLapBar);

		String[] valuesD = new String[convertedGreatestDegrees.size()];
		valuesD = convertedGreatestDegrees.toArray(valuesD);

		Double evaluatedValue = evaluateOptimizationFunction(
			new EvaluationInfo(
				job.getOptimizationFunction(), 
				valuesAdj, 
				valuesLap,
				valuesSgnLap, 
				job.getNumVertices(), 
				job.getNumEdges(), 
				valuesD, 
				valuesAdjBar,
				valuesLapBar, 
				valuesSgnLapBar, 
				job.getChi(), 
				job.getChiBar(),
				job.getOmega(), 
				job.getOmegaBar()));

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

		List<String> inputData = CsvReader.readFile(workFolder + File.separator
				+ "sagi_input.txt");
		if (inputData.size() > 1) {
			String header = inputData.get(0); // Get the CSV header

			JobUnity job = new JobUnity();

			for (int x = 1; x < inputData.size(); x++) { // REDUCE read all
															// lines
				String line = inputData.get(x);
				String[] lineData = line.split(",");

				/*
				 * Get tEigSolve file
				 */
				String inputFile = lineData[CsvReader.getIndex("eigsolve",
						header)];

				/*
				 * Get function expression
				 */
				String optimizationFunction = lineData[CsvReader.getIndex(
						"optifunc", header)];

				/*
				 * Get source file reference
				 */
				String g6fileid = lineData[CsvReader.getIndex("g6fileid",
						header)];

				/*
				 * Get maximum results to show
				 */
				String maxResults = lineData[CsvReader.getIndex("maxresults",
						header)];

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

				if (inputFile.contains(".lapb")) {
					job.setLapBarFile(inputFile);
				}
				if (inputFile.contains(".adjb")) {
					job.setAdjBarFile(inputFile);
				}
				if (inputFile.contains(".sgnlapb")) {
					job.setSgnLapBarFile(inputFile);
				}
				if (inputFile.contains(".inv")) {
					job.setInvariantsFile(workFolder, inputFile);
				}

			}

			processJob(job);

		} else {
			System.out.println("Empty input data file.");
		}

	}
}
