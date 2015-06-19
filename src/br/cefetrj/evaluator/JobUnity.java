package br.cefetrj.evaluator;

public class JobUnity {
	private String lapFile;
	private String adjFile;
	private String sgnlapFile;
	private String header;
	private String optimizationFunction;
	private String g6fileid;
	private String maxResults;
	
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
		return (lapFile != null) && ( !lapFile.equals(""));
	}

	public boolean isAdj() {
		return (adjFile != null) && ( !adjFile.equals(""));
	}

	public boolean isSgnlap() {
		return (sgnlapFile != null) && ( !sgnlapFile.equals(""));
	}

	public void setLapFile(String lapFile) {
		this.lapFile = lapFile;
	}

	public void setAdjFile(String adjFile) {
		this.adjFile = adjFile;
	}

	public void setSgnlapFile(String sgnlapFile) {
		this.sgnlapFile = sgnlapFile;
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

	public String getSgnlapFile() {
		return sgnlapFile;
	}

	public String getHeader() {
		return header;
	}
	
}
