package testClass;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import net.verza.jdict.utils.Utility;

import com.sleepycat.je.Cursor;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.je.EnvironmentLockedException;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;

public class SleepyCatDbExample {

    Environment environment = null;
    Database database = null;

    public SleepyCatDbExample() {
    }

    private void open() throws EnvironmentLockedException, DatabaseException {
	EnvironmentConfig envConfig = new EnvironmentConfig();
	envConfig.setAllowCreate(true);
	environment = new Environment(new File("data/users"), envConfig);

	DatabaseConfig dbConfig = new DatabaseConfig();
	// dbConfig.setTransactional(false);
	dbConfig.setAllowCreate(true);
	// dbConfig.setTemporary(true);
	database = environment.openDatabase(null, "users", dbConfig);

    }

    private String read(Integer key) throws DatabaseException,
	    UnsupportedEncodingException {
	DatabaseEntry keyEntry = new DatabaseEntry(Utility.intToByteArray(key));
	DatabaseEntry valueEntry = new DatabaseEntry();
	String dato = null;

	database.get(null, keyEntry, valueEntry, LockMode.READ_COMMITTED);
	byte[] retData = valueEntry.getData();
	dato = new String(retData, "UTF-8");

	return dato;
    }

    private void write(Integer key, String value) throws DatabaseException {
	DatabaseEntry keyEntry = new DatabaseEntry(Utility.intToByteArray(key));
	DatabaseEntry valueEntry = new DatabaseEntry(value.getBytes());

	database.put(null, keyEntry, valueEntry);

    }

    private void load() throws DatabaseException {
	Integer[] keys = { new Integer(4), new Integer(3), new Integer(1),
		new Integer(2) };
	String[] values = { "quattro", "tre", "uno", "due" };

	for (int i = 0; i < keys.length; i++) {
	    System.out
		    .println("loading key:" + keys[i] + " value:" + values[i]);
	    DatabaseEntry keyEntry = new DatabaseEntry(Utility
		    .intToByteArray(keys[i]));
	    DatabaseEntry valueEntry = new DatabaseEntry(values[i].getBytes());
	    database.put(null, keyEntry, valueEntry);
	}

	environment.sync();
    }

    public void dumpReverse() throws DatabaseException, IOException {
	Cursor cursor = database.openCursor(null, null);
	DatabaseEntry keyEntry = new DatabaseEntry();
	DatabaseEntry valueEntry = new DatabaseEntry();

	while (cursor.getPrev(keyEntry, valueEntry, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
	    byte[] retKey = keyEntry.getData();
	    byte[] retData = valueEntry.getData();
	    Integer key = Utility.byteArrayToInt(retKey);
	    String data = new String(retData, "UTF-8");
	    System.out.println("Key | Data : " + key + " | " + data + "");
	}
    }

    public void dump() throws DatabaseException, IOException {
	Cursor cursor = database.openCursor(null, null);
	DatabaseEntry keyEntry = new DatabaseEntry();
	DatabaseEntry valueEntry = new DatabaseEntry();

	while (cursor.getNext(keyEntry, valueEntry, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
	    byte[] retKey = keyEntry.getData();
	    byte[] retData = valueEntry.getData();
	    Integer key = Utility.byteArrayToInt(retKey);
	    String data = new String(retData, "UTF-8");
	    System.out.println("Key | Data : " + key + " | " + data + "");
	}
    }

    public static void main(String[] args) {

	SleepyCatDbExample example = new SleepyCatDbExample();
	try {
	    example.open();
	    // example.load();
	    System.out
		    .println("<------------------------------- DUMP ------------------------->");
	    example.dump();
	    System.out
		    .println("<------------------------------- REVERSE DUMP ------------------------->");
	    example.dumpReverse();
	    System.out
		    .println("<---------------------------------------------------------->");
	    example.write(5, "cinque");
	    System.out.println("query :" + example.read(5));
	} catch (EnvironmentLockedException e) {
	    e.printStackTrace();
	} catch (DatabaseException e) {
	    e.printStackTrace();
	} catch (UnsupportedEncodingException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	}

    }

}
