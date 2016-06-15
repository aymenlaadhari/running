package model;

import java.util.Date;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class RunnerStatistics {
	@DatabaseField(generatedId = true)
	private int statisticsID;
	@DatabaseField
	private Date runningDate;
	@DatabaseField
	private double calories;
	@DatabaseField
	private double speed;
	@DatabaseField
	private String time;
	@DatabaseField
	private double distance;
	@DatabaseField(foreign = true, foreignAutoRefresh = true)
	private Runner runner;
	
	

	public RunnerStatistics() {
		super();
	}
	
	

	public RunnerStatistics(int statisticsID, Date runningDate,
			double calories, double speed, String time, double distance,
			Runner runner) {
		super();
		this.statisticsID = statisticsID;
		this.runningDate = runningDate;
		this.calories = calories;
		this.speed = speed;
		this.time = time;
		this.distance = distance;
		this.runner = runner;
	}



	public int getStatisticsID() {
		return statisticsID;
	}

	public void setStatisticsID(int statisticsID) {
		this.statisticsID = statisticsID;
	}

	public Date getRunningDate() {
		return runningDate;
	}

	public void setRunningDate(Date runningDate) {
		this.runningDate = runningDate;
	}

	public double getCalories() {
		return calories;
	}

	public void setCalories(double calories) {
		this.calories = calories;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public Runner getRunner() {
		return runner;
	}

	public void setRunner(Runner runner) {
		this.runner = runner;
	}

}