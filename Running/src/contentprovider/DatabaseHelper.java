package contentprovider;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Runner;
import model.RunnerStatistics;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

	private static final String DATABASE_NAME = "running.db";
	private static final int DATABASE_VERSION = 2;
	private Dao<Runner, Integer> daoRunner;
	private Dao<RunnerStatistics, Integer> daoStatistics;
	private RuntimeExceptionDao<Runner, Integer> simpleRuntimeRunnerDao = null;
	private RuntimeExceptionDao<RunnerStatistics, Integer> simpleRuntimeStatDao = null;

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase sqliteDatabase,
			ConnectionSource connectionSource) {
		try {
			Log.i(DatabaseHelper.class.getName(), "onCreate");
			Log.i("test", "name = " + DatabaseHelper.class.getName());
			TableUtils.createTable(connectionSource, Runner.class);
			TableUtils.createTable(connectionSource, RunnerStatistics.class);
		} catch (SQLException e) {
			Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
			throw new RuntimeException(e);
		}

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connection,
			int oldVersion, int newVersion) {
		List<String> allSql = new ArrayList<String>();
		switch (oldVersion) {
		case 1:
			// allSql.add("alter table AdData add column `new_col` VARCHAR");
			// allSql.add("alter table AdData add column `new_col2` VARCHAR");
		}
		for (String sql : allSql) {
			db.execSQL(sql);
		}

	}

	public Dao<Runner, Integer> getDaoRunner() {
		if (null == daoRunner) {
			try {
				daoRunner = getDao(Runner.class);
			} catch (java.sql.SQLException e) {
				e.printStackTrace();
			}
		}
		return daoRunner;
	}

	public Dao<RunnerStatistics, Integer> getDaoStatistics() {
		if (null == daoStatistics) {
			try {
				daoStatistics = getDao(RunnerStatistics.class);
			} catch (java.sql.SQLException e) {
				e.printStackTrace();
			}
		}
		return daoStatistics;
	}

	/*
	 * Insert a runner
	 */

	public void insertRunner(Runner runner) {
		RuntimeExceptionDao<Runner, Integer> dao = getSimpleDataRunnerDao();
		// Create a data in the database through solid objects, successful
		// return 1, insert a data description
		Log.i("test", "dao = " + dao + "  runner= " + runner);
		int returnValue = dao.create(runner);
		Log.i("test", "The return value is inserted into the data: "
				+ returnValue);
	}

	/*
	 * get list runners
	 */

	public List<Runner> findAllRunner() {
		RuntimeExceptionDao<Runner, Integer> dao = getSimpleDataRunnerDao();
		return dao.queryForAll();
	}

	public RuntimeExceptionDao<Runner, Integer> getSimpleDataRunnerDao() {
		if (simpleRuntimeRunnerDao == null) {
			simpleRuntimeRunnerDao = getRuntimeExceptionDao(Runner.class);
		}
		Log.i("test", "simpleRuntimeDao ======= " + simpleRuntimeRunnerDao);
		return simpleRuntimeRunnerDao;
	}

	/*
	 * Insert Satistics
	 */
	public void insertStataistics(RunnerStatistics statistics) {
		RuntimeExceptionDao<RunnerStatistics, Integer> dao = getSimpleDataStatisticsDao();
		// Create a data in the database through solid objects, successful
		// return 1, insert a data description
		Log.i("test", "dao = " + dao + "  statistics= " + statistics);
		int returnValue = dao.create(statistics);
		Log.i("test", "The return value is inserted into the data: "
				+ returnValue);
	}

	/*
	 * get List statistics
	 */
	public List<RunnerStatistics> findAllStat() {
		RuntimeExceptionDao<RunnerStatistics, Integer> dao = getSimpleDataStatisticsDao();
		return dao.queryForAll();

	}

	public RuntimeExceptionDao<RunnerStatistics, Integer> getSimpleDataStatisticsDao() {
		if (simpleRuntimeStatDao == null) {
			simpleRuntimeStatDao = getRuntimeExceptionDao(RunnerStatistics.class);
		}
		Log.i("test", "simpleRuntimeDao ======= " + simpleRuntimeStatDao);
		return simpleRuntimeStatDao;
	}

	/*
	 * Get runner by FBID
	 */
	public Runner getByFB(String FbId) throws SQLException
	{
		
		List<Runner> runners  = daoRunner.queryBuilder().where().eq(Runner.FB_FIELD, FbId).query();
		Runner runner = runners.get(0);
		return runner;
	}
	public void updateRunner(Runner runner) {
		RuntimeExceptionDao<Runner, Integer> dao = getSimpleDataRunnerDao();
		dao.update(runner);

	}
	
	public ArrayList<Cursor> getData(String Query){
		//get writable database
		SQLiteDatabase sqlDB = this.getWritableDatabase();
		String[] columns = new String[] { "mesage" };
		//an array list of cursor to save two cursors one has results from the query 
		//other cursor stores error message if any errors are triggered
		ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
		MatrixCursor Cursor2= new MatrixCursor(columns);
		alc.add(null);
		alc.add(null);
		
		
		try{
			String maxQuery = Query ;
			//execute the query results will be save in Cursor c
			Cursor c = sqlDB.rawQuery(maxQuery, null);
			

			//add value to cursor2
			Cursor2.addRow(new Object[] { "Success" });
			
			alc.set(1,Cursor2);
			if (null != c && c.getCount() > 0) {

				
				alc.set(0,c);
				c.moveToFirst();
				
				return alc ;
			}
			return alc;
		} catch(Exception ex){

			Log.d("printing exception", ex.getMessage());

			//if any exceptions are triggered save the error message to cursor an return the arraylist
			Cursor2.addRow(new Object[] { ""+ex.getMessage() });
			alc.set(1,Cursor2);
			return alc;
		}

		
	}
}
