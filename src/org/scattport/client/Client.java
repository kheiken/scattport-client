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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.client.XmlRpcCommonsTransportFactory;

/**
 * Daemon that checks for new jobs and runs them.
 *
 * @author Karsten Heiken <karsten@disposed.de>
 */
public class Client {

	public static final int HEARTBEAT_INTERVAL = 60;
	public static final int JOBFETCHER_INTERVAL = 10;
	/** The secret needed to talk to the server. */
	private static String secret = "d769926f814ba466736875ed4ff4da2b4c53b3e7"; //TODO: Move to external file
	/** Set to false when we want to quit. */
	public static Boolean running = true;

	static XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
	static XmlRpcClient client = new XmlRpcClient();

	/**
	 * Main loop.
	 * @param args not used
	 */
	public static void main(String[] args) throws MalformedURLException, XmlRpcException {
		Thread heartbeat = new Thread(new Heartbeat());
		Thread jobfetcher = new Thread(new JobFetcher());

		heartbeat.start();
		jobfetcher.start();
	}

	public static HashMap exec(String function) {
		return exec(function, new Object[]{});
	}

	public static HashMap exec(String function, Object[] params) {
		HashMap result = new HashMap();
		try {
			//TODO: Get server from an external file
			config.setServerURL(new URL("http://127.0.0.1/ScattPort/xmlrpc"));
			client.setTransportFactory(new XmlRpcCommonsTransportFactory(client));
			client.setConfig(config);

			params = (new Object[]{secret, params});
			result = (HashMap) client.execute(function, params);
		} catch (XmlRpcException ex) {
			Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
		} catch (MalformedURLException ex) {
			Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
		}

		return result;
	}
}
