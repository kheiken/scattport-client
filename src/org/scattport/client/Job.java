/*
 * Copyright (c) 2011 Karsten Heiken <karsten@disposed.de>
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is 
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package org.scattport.client;

/**
 * 
 * @author Karsten Heiken <karsten@disposed.de>
 */
public class Job {
	
	public static final int PENDING = 1;
	public static final int RUNNING = 2;
	public static final int FINISHED = 3;

	private String application;
	private String jobId;
	private String jobName;
	private String experimentId;
	private String projectId;
	private int runtime = 0;
	private int status = PENDING;

	public Job(String id, String experimentId, String projectId) {
		this.jobId = id;
		this.experimentId = experimentId;
		this.projectId = projectId;
	}

	/**
	 * @return the jobId
	 */
	public String getJobId() {
		return jobId;
	}

	/**
	 * @return the jobName
	 */
	public String getJobName() {
		return jobName;
	}

	/**
	 * @return the runtime
	 */
	public int getRuntime() {
		return runtime;
	}

	public int getStatus() {
		return status;
	}

	public synchronized void setStatus(int status) {
		this.status = status;
	}

	public String getApplication() {
		return application;
	}
	
	public String getExperimentId() {
		return experimentId;
	}
	
	public String getProjectId() {
		return projectId;
	}
}
