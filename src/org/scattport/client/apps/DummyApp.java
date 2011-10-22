package org.scattport.client.apps;

import java.util.HashMap;

import org.scattport.client.App;
import org.scattport.client.Client;
import org.scattport.client.Job;

public class DummyApp extends App {

	/**
	 * Dummy application that just sleeps for 20 seconds.
	 *
	 * @param job
	 */
	public DummyApp(Job job) {
		super(job);
	}

	@Override
	public void run() {
		this.spawn();
		this.submitResults();
	}

	@Override
	public void setup() {
		// this is a dummy. we don't need to set up anything
	}

	@Override
	public void spawn() {
		// this dummy will simply sleep for twenty seconds
		try {
			System.out.println("Start of DummyApp");
			Thread.sleep(20 * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void kill() {
		// not implemented
	}

	@Override
	public void submitResults() {
		System.out.println("Stopping app and deleting job");

		HashMap<String, Object> result = Client.exec("job_done", job.getJobId().toString());

		if (!result.get("success").equals("true")) {
			System.out.println("Job progress could not be stored.");
		}

		Client.deleteJob(job);
	}

}
