package model;

import java.io.Serializable;
import java.util.Date;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;

public class Runner implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String FB_FIELD = "FbID";
	@DatabaseField(generatedId = true)
	private int runnerID;
	@DatabaseField
	private String firstName;
	@DatabaseField
	private String LastName;
	@DatabaseField
	private String birth;
	@DatabaseField
	private boolean isMale;
	@DatabaseField
	private int heigh;
	@DatabaseField
	private int weight;
	@DatabaseField(columnName = FB_FIELD)
	private String fbID;
	@DatabaseField
	private String twID;
	@DatabaseField
	private String backID;
	@ForeignCollectionField
	private ForeignCollection<RunnerStatistics> runnerStatistics;

	public Runner() {
		super();
	}

	public Runner(int runnerID, String firstName, String lastName,
			String birth, boolean isMale, int heigh, int weight, String fbID,
			String twID, String backID,ForeignCollection<RunnerStatistics> runnerStatistics) {
		super();
		this.runnerID = runnerID;
		this.firstName = firstName;
		LastName = lastName;
		this.birth = birth;
		this.isMale = isMale;
		this.heigh = heigh;
		this.weight = weight;
		this.fbID = fbID;
		this.twID = twID;
		this.backID = backID;
		this.runnerStatistics = runnerStatistics;
	}

	public int getRunnerID() {
		return runnerID;
	}

	public void setRunnerID(int runnerID) {
		this.runnerID = runnerID;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return LastName;
	}

	public void setLastName(String lastName) {
		LastName = lastName;
	}

	public String getBirth() {
		return birth;
	}

	public void setBirth(String birth) {
		this.birth = birth;
	}

	public boolean isMale() {
		return isMale;
	}

	public void setMale(boolean isMale) {
		this.isMale = isMale;
	}

	public int getHeigh() {
		return heigh;
	}

	public void setHeigh(int heigh) {
		this.heigh = heigh;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public ForeignCollection<RunnerStatistics> getRunnerStatistics() {
		return runnerStatistics;
	}

	public void setRunnerStatistics(
			ForeignCollection<RunnerStatistics> runnerStatistics) {
		this.runnerStatistics = runnerStatistics;
	}

	public String getFbID() {
		return fbID;
	}

	public void setFbID(String fbID) {
		this.fbID = fbID;
	}

	public String getTwID() {
		return twID;
	}

	public void setTwID(String twID) {
		this.twID = twID;
	}

	public String getBackID() {
		return backID;
	}

	public void setBackID(String backID) {
		this.backID = backID;
	}
	
	

}
