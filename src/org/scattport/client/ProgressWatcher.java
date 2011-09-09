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

import javax.management.RuntimeErrorException;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * 
 *
 * @author Karsten Heiken <karsten@disposed.de>
 */
public class ProgressWatcher implements Runnable {

	@Override
	public void run() {
		while(Client.running) {
			try {
				// Is there a job running?
				if(Client.getRunningJobs().size() > 0) {
					System.out.println("There is a job running. Querying it now");
					
				}

				Thread.sleep(Client.PROGRESS_INTERVAL * 1000);
			} catch (InterruptedException ex) {
				Logger.getLogger(ProgressWatcher.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	public static void sendProgress() {
		System.out.println("Sending progress to server");
	}
}
