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

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Simple Watchdog.
 *
 * If a thread dies, we simply restart it.
 * TODO: Implement an actual error handling.
 * 
 * @author Karsten Heiken <karsten@disposed.de>
 */
public class Watchdog implements Runnable {

	@Override
	public void run() {

		// TODO: get workload
		while (Client.running) {
			try {
				if(!Client.heartbeat.isAlive()) {
					System.out.println("Heartbeat-Thread died. Restarting.");
					Client.heartbeat = new Thread(new Heartbeat());
					Client.heartbeat.start();
				}
				
				if(!Client.jobfetcher.isAlive()) {
					System.out.println("JobFetcher-Thread died. Restarting.");
					Client.jobfetcher = new Thread(new JobFetcher());
					Client.jobfetcher.start();	
				}
				
				if(!Client.progresswatcher.isAlive()) {
					System.out.println("ProgressWatcher-Thread died. Restarting.");
					Client.progresswatcher.start();	
				}

				// sleep for a while.
				Thread.sleep(5000);
			} catch (InterruptedException ex) {
				Logger.getLogger(Watchdog.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}
}
