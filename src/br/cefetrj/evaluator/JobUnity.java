package br.cefetrj.evaluator;

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
	
	private boolean usingGreatestDegrees;
	private String greatestDegreesFile;

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

	public void setSgnlapBarFile(String sgnlapBarFile) {
		this.sgnLapBarFile = sgnlapBarFile;
	}

	public boolean isUsingGreatestDegrees() {
		return usingGreatestDegrees;
	}

	public String getGreatestDegreesFile() {
		return this.greatestDegreesFile;
	}
	
	public void setGreatestDegreesFile(String greatestDegreesFile) {
		this.greatestDegreesFile = greatestDegreesFile;
	}

}
