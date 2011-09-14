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

import java.io.File;

/**
 * 
 * @author Karsten Heiken <karsten@disposed.de>
 */
public abstract class App implements Runnable {
	
	public static final int PENDING = 1;
	public static final int RUNNING = 2;
	public static final int FINISHED = 3;

	protected Job job;
	protected int status;
	protected int pid;
	protected Process process;
	protected File workingDir;

	protected App(Job job) {
		this.job = job;
	}

	@Override
	public abstract void run();
	
	/**
	 * Setup the environment.
	 * 
	 * Create required paths, copy the project files there, and so on.
	 */
	public void setup() {
		workingDir = new File(Client.properties.getProperty("calculation.path") + job.getJobId());
		if(!workingDir.mkdirs())
			throw new RuntimeException("The environment could not be set up");
	}
	
	/**
	 * Spawn the actual worker.
	 * 
	 * This will cause the actual application to start the calculation.
	 */
	public abstract void spawn();
	
	/**
	 * Kill the application.
	 * 
	 * If we have reason to believe the process died or chomps away or memory,
	 * we kill it here.
	 */
	public abstract void kill();
	
	/**
	 * Send the results to the server.
	 * 
	 * After the calculation was successfully finished, we send the results
	 * to the server.
	 */
	public abstract void submitResults();

	/**
	 * Get the status of the current calculation.
	 * 
	 * @return 1 - pending, 2 - running, 3 - finished 
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * Returns the process id of this application if it is running.
	 * 
	 * @return process id or -1 if it is not running
	 */
	public int getPid() {
		return pid;
	}
	
	/**
	 * Return the job that this application is working on.
	 * 
	 * @return the job
	 */
	public Job getJob() {
		return job;
	}
}
