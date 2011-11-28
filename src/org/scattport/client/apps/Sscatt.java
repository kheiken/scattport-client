package org.scattport.client.apps;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

import org.jibble.simpleftp.SimpleFTP;
import org.scattport.client.App;
import org.scattport.client.Client;
import org.scattport.client.Job;

public class Sscatt extends App {

	/**
	 * The files that we need to start the calculation.
	 */
	private final String[] inputFiles = {
		"default.calc",
		"default.obj",
		"param_dsm.dat"
	};

	/**
	 * The files we get when the calculation is finished.
	 */
	private final String[] outputFiles = {
		"default.out",
		"default.log",
		"default.tma"
	};

	/**
	 * Dummy application that just sleeps for 20 seconds.
	 *
	 * @param job
	 */
	public Sscatt(Job job) {
		super(job);
	}

	@Override
	public void run() {
		if(!Client.properties.containsKey("sscatt.path")) {
			System.err.println("We received a job for sscatt, but it is not configured!");
			// TODO: notify the server that we screwed up
		} else {
			try {
				this.setup();
			} catch (IOException e) {
				System.err.println("Environment could not be set up:");
				e.printStackTrace();
			}
			this.spawn();
			this.submitResults();
		}
	}

	@Override
	public void setup() throws IOException {
		super.setup();

		for(String file : inputFiles) {
	        try {
	            URL url = new URL(Client.properties.getProperty("server.base") + "uploads/" + job.getProjectId() + "/" + job.getExperimentId() + "/" + file);
	            URLConnection con = url.openConnection(); // open the url connection.
	            DataInputStream dis = new DataInputStream(con.getInputStream()); // get a data stream from the url connection.
	            byte[] fileData = new byte[con.getContentLength()]; // determine how many byes the file size is and make array big enough to hold the data
	            for (int x = 0; x < fileData.length; x++) { // fill byte array with bytes from the data input stream
	                fileData[x] = dis.readByte();
	            }
	            dis.close(); // close the data input stream
	            FileOutputStream fos = new FileOutputStream(new File(workingDir + "/" + file));  //create an object representing the file we want to save
	            fos.write(fileData);  // write out the file we want to save.
	            fos.close(); // close the output stream writer
	        }
	        catch(MalformedURLException m) {
	            System.out.println(m);
	        }
	        catch(IOException io) {
	            System.out.println(io);
	        }
		}
	}

	@Override
	public void spawn() {
		try {
			System.out.println("Spawning worker");
			ProcessBuilder builder;

			// TODO: this needs to be improved
			builder = new ProcessBuilder(Client.properties.get("sscatt.path").toString());

		    builder.directory(this.workingDir);
		    Process p = builder.start();
		    try {
				p.waitFor();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		    //Scanner s = new Scanner( p.getInputStream() ).useDelimiter( "\n" );
		    //System.out.println( s.next() );
		    System.out.println("Job done");
		    Client.deleteJob(job);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void kill() {
		// not implemented
	}

	@Override
	public void submitResults() {
		System.out.println("Stopping app and deleting job");
		System.out.println("Uploading results");

		boolean filesUploaded;

		// upload results
		try {

			SimpleFTP ftp = new SimpleFTP();

			// Connect to an FTP server on port 21.
			ftp.connect(
					Client.properties.getProperty("ftp.host"),
					Integer.parseInt(Client.properties.getProperty("ftp.port")),
					Client.properties.getProperty("ftp.user"),
					Client.properties.getProperty("ftp.pass"));

			// Set binary mode.
			ftp.bin();

			// Change to a new working directory on the FTP server.
			if (!ftp.cwd("incoming")) {
				ftp.mkdir("incoming");
				ftp.cwd("incoming");
			}

			if (!ftp.cwd(job.getJobId())) {
				ftp.mkdir(job.getJobId());
				ftp.cwd(job.getJobId());
			}

			if (!ftp.pwd().endsWith("incoming/" + job.getJobId())) {
				System.out.println("The server could not change to the correct working directory.");
			}

			// okay, we seem to be in the correct directory. go for it.
			for(String file : outputFiles) {
				try {
					ftp.stor(new File(this.workingDir + "/" + file));
				} catch(FileNotFoundException e) {
					System.err.println("Required output-file " + file + " was not found.");
				}
			}

			// Quit from the FTP server.
			ftp.disconnect();
			filesUploaded = true;
		} catch (IOException e) {
			e.printStackTrace();
			filesUploaded = false;
		}

		System.out.println("Upload done");

		// tell the server we are done
		HashMap<String, Object> result = Client.exec("job_done", job.getJobId().toString(), filesUploaded ? "true" : "false");

		if (!result.get("success").equals("true")) {
			System.out.println("Job progress could not be stored.");
		}

		// delete the job
		Client.deleteJob(job);
	}
}
