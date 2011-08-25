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

import java.io.PrintStream;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Report to the server.
 *
 * Update the workload for this server and send a general heartbeat so that
 * the server knows which clients are online.
 *
 * @author Karsten Heiken <karsten@disposed.de>
 */
public class Heartbeat implements Runnable {

	@Override
	public void run() {

		// TODO: get workload
		while (Client.running) {
			try {
				System.out.println("Sending heartbeat");
				Object[] params = new Object[]{"Linux", "0.5"};
				HashMap result = Client.exec("heartbeat", params);

				if (!result.get("success").equals("true")) {
					System.out.println("Heartbeat was not successful.");
				}

				// sleep for a while. then send another heartbeat
				Thread.sleep(Client.HEARTBEAT_INTERVAL * 1000);
			} catch (InterruptedException ex) {
				Logger.getLogger(Heartbeat.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}
}
