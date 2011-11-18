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

package org.scattport.setup;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;

/**
 * This class will help you setup the client.
 *
 * @author Karsten Heiken, karsten@disposed.de
 */
public class Wizard {

	/**
	 * Run the setup wizard.
	 */
	public Wizard() {

		Scanner scanner = new Scanner(System.in);
		Properties properties = new Properties();

		System.out.println("-- Authentication settings --");
		System.out.println();

		// Client's secret
		System.out.println("Server secret:");
		properties.put("client.secret", scanner.nextLine());

		System.out.println("-- Settings for connecting to the server --");
		System.out.println();


		// Base url, that we need for downloading models, configurations, and such
		while(!properties.containsKey("server.base")) {
			System.out.println("Base url for the server, eg. http://example.com/ScattPort/");
			String tmp = scanner.nextLine();
			if(!tmp.endsWith("/"))
				tmp += "/";

			properties.put("server.base", tmp);
		}


		// URL that leads us to the rpc api
		while(!properties.containsKey("server.url")) {
			System.out.println("XML-RPC API url for the server, eg. http://example.com/ScattPort/xmlrpc");
			String tmp = scanner.nextLine();
			if(!tmp.endsWith("/"))
				tmp += "/";

			properties.put("server.url", tmp);
		}

		System.out.println("-- Performance settings --");
		System.out.println();

		// Interval for sending heartbeats
		System.out.println("How often (in seconds) should we send a heartbeat? Minimum 5, Default: 10");
		try {
			int heartbeatInterval = Integer.parseInt(scanner.nextLine());

			// sanity checking
			if(heartbeatInterval < 5)
				heartbeatInterval = 5;

			properties.put("heartbeat.interval", String.valueOf(heartbeatInterval));
		} catch (java.lang.NumberFormatException nfe) {
			// if the user entered something illegal, default to 10
			properties.put("heartbeat.interval", "10");
		}

		// Interval for checking for new jobs
		System.out.println("How often (in seconds) should we check for new jobs? Minimum: 5, Default: 15");
		try {
			int jobInterval = Integer.parseInt(scanner.nextLine());

			// sanity checking
			if(jobInterval < 5)
				jobInterval = 5;

			properties.put("fetchjob.interval", String.valueOf(jobInterval));
		} catch (java.lang.NumberFormatException nfe) {
			// if the user entered something illegal, default to 10
			properties.put("fetchjob.interval", "10");
		}

		System.out.println("-- Calculation settings --");
		System.out.println();
		// set working directory
		while (!properties.containsKey("calculations.basedir")) {
			System.out.println("Enter the directory where the jobs should be run, eg. /tmp/");

			File directory = new File(scanner.nextLine());
			if (!directory.isDirectory()){
				System.out.println("The directory does not exist. Create it now? ([Y]/n)");
				if(scanner.nextLine().equalsIgnoreCase("n"))
					continue;
				else {
					directory.mkdirs();
				}
			}

			// paths need to end with "/"
			String tmp = directory.toString();
			if(!tmp.endsWith("/"))
				tmp += "/";

			// the default path is /tmp
			if(tmp.equals("/"))
				tmp = "/tmp/";

			properties.put("calculations.basedir", tmp);
		}

		// settings for the ftp-server
		System.out.println();
		System.out.println("-- Connection details for FTP --");

		System.out.println("Hostname:");
		properties.put("ftp.host", scanner.nextLine());
		System.out.println("Port:");
		properties.put("ftp.port", scanner.nextLine());
		System.out.println("Username:");
		properties.put("ftp.user", scanner.nextLine());
		System.out.println("Password:");
		properties.put("ftp.pass", scanner.nextLine());

		// configure the actual applications
		System.out.println();
		System.out.println("-- Simulators --");

		System.out.println("Do you want to configure \"sscatt\"? ([y]/n)");
		String configure = scanner.nextLine();

		boolean sscattIsConfigured = false;
		String sscattPath = "";

		if(!configure.equalsIgnoreCase("n")) {

			while(!sscattIsConfigured) {
				System.out.println("Enter the path to the executable of \"sscatt\" (including the actual program:");
				sscattPath = scanner.nextLine();

				// the user changed his mind about configuring sscatt.
				if(sscattPath.equalsIgnoreCase("cancel"))
					break;

				File file = new File(sscattPath);

				// check if there is a file at the given path
				if(!file.exists()) {
					System.out.println("There is no application at the given path. Please try again or enter \"cancel\".");
					continue;
				}

				// check if the application is executable
				if(!file.canExecute()) {
					System.out.println("That application is not executable (chmod correct?). Please try again or enter \"cancel\".");
					continue;
				}

				sscattIsConfigured = true;
			}
		}

		if(sscattIsConfigured) {
			properties.put("sscatt.path", sscattPath);
		}

		// write the new settings to settings.properties
		try {
			properties.store(new FileOutputStream(new File("settings.properties")), "Generated by ScattPortClient");
		} catch (FileNotFoundException e) {
			System.out.println("Could not write to settings.properties.");
		} catch (IOException e) {
			System.out.println("Could not write to settings.properties.");
		}

	}
}
