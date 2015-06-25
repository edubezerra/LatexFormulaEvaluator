package br.cefetrj.evaluator;

import java.io.File;
import java.util.List;

public class JobUnity {
	private String lapFile;
	private String adjFile;
	private String sgnLapFile;

	private String lapBarFile;
	private String adjBarFile;
	private String sgnLapBarFile;

	private String header;
	private String optimizationFunction;
	private String g6fileid;
	private String maxResults;

	private String invariantsFile;
	private String chi;
	private String chiBar;
	private String omega;
	private String omegaBar;
	private String numVertices;
	private String numEdges;

	public void setMaxResults(String maxResults) {
		this.maxResults = maxResults;
	}

	public String getMaxResults() {
		return maxResults;
	}

	public void setG6fileid(String g6fileid) {
		this.g6fileid = g6fileid;
	}

	public String getG6fileid() {
		return g6fileid;
	}

	public boolean isLap() {
		return (lapFile != null) && (!lapFile.equals(""));
	}

	public boolean isAdj() {
		return (adjFile != null) && (!adjFile.equals(""));
	}

	public boolean isSgnLap() {
		return (sgnLapFile != null) && (!sgnLapFile.equals(""));
	}

	public void setLapFile(String lapFile) {
		this.lapFile = lapFile;
	}

	public void setAdjFile(String adjFile) {
		this.adjFile = adjFile;
	}

	public void setSgnLapFile(String sgnLapFile) {
		this.sgnLapFile = sgnLapFile;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public void setOptimizationFunction(String optimizationFunction) {
		this.optimizationFunction = optimizationFunction;
	}

	public String getOptimizationFunction() {
		return optimizationFunction;
	}

	public String getLapFile() {
		return lapFile;
	}

	public String getAdjFile() {
		return adjFile;
	}

	public String getSgnLapFile() {
		return sgnLapFile;
	}

	public String getHeader() {
		return header;
	}

	public boolean isAdjBar() {
		return (adjBarFile != null) && (!adjBarFile.equals(""));
	}

	public boolean isLapBar() {
		return (lapBarFile != null) && (!lapBarFile.equals(""));
	}

	public boolean isSgnLapBar() {
		return (sgnLapBarFile != null) && (!sgnLapBarFile.equals(""));
	}

	public String getAdjBarFile() {
		return adjBarFile;
	}

	public String getLapBarFile() {
		return lapBarFile;
	}

	public String getSgnLapBarFile() {
		return sgnLapBarFile;
	}

	public void setLapBarFile(String lapBarFile) {
		this.lapBarFile = lapBarFile;
	}

	public void setAdjBarFile(String adjBarFile) {
		this.adjBarFile = adjBarFile;
	}

	public void setSgnLapBarFile(String sgnlapBarFile) {
		this.sgnLapBarFile = sgnlapBarFile;
	}

	public boolean isUsingGreatestDegrees() {
		return this.omega != null;
	}

	public void setInvariantsFile(String workFolder, String inputFile)
			throws Exception {
		this.invariantsFile = inputFile;
		List<String> inputData = CsvReader.readFile(workFolder + File.separator
				+ "geni.inv");
		if (inputData.size() > 1) {
			String header = inputData.get(0); // Get the CSV header

			for (int x = 1; x < inputData.size(); x++) {
				String line = inputData.get(x);
				String[] lineData = line.split(",");

				int index = CsvReader.getIndex("chi", header);
				if (index >= 0) {
					this.chi = lineData[index];
				}

				index = CsvReader.getIndex("chiBar", header);
				if (index >= 0) {
					this.chiBar = lineData[index];
				}

				index = CsvReader.getIndex("omega", header);
				if (index >= 0) {
					this.omega = lineData[index];
				}

				index = CsvReader.getIndex("omegaBar", header);
				if (index >= 0) {
					this.omegaBar = lineData[index];
				}

				index = CsvReader.getIndex("numVertices", header);
				if (index >= 0) {
					this.numVertices = lineData[index];
				}

				index = CsvReader.getIndex("numEdges", header);
				if (index >= 0) {
					this.numEdges = lineData[index];
				}
			}
		}
	}

	public String getInvariantsFile() {
		return invariantsFile;
	}

	public String getNumVertices() {
		return numVertices;
	}

	public String getNumEdges() {
		return numEdges;
	}

	public String getChi() {
		return chi;
	}

	public String getChiBar() {
		return chiBar;
	}

	public String getOmega() {
		return omega;
	}

	public String getOmegaBar() {
		return omegaBar;
	}
}
