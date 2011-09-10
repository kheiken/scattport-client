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

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @author Karsten Heiken <karsten@disposed.de>
 */
public class App implements Runnable {

	private int pid;
	private boolean finished;
	private final Job job;
	private Thread thread;
	private String jobId;

	App(Job job) {
		this.job = job;

	}

	@Override
	public void run() {
		System.out.println("Starte App");

		try {
			Thread.sleep(20000);
		} catch (InterruptedException ex) {
			Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
		}
		System.out.println("Stopping app and deleting job");

		HashMap result = Client.exec("job_done", job.getJobId().toString());

		if (!result.get("success").equals("true")) {
			System.out.println("Job progress could not be stored.");
		}

		Client.deleteJob(job);
	}
}
