package org.tim.databaseSeed;

import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class SeederUtils {


	@SuppressWarnings("unchecked")
	public static ArrayList<LinkedHashMap<String, Object>> getObjectsFromJSON(String path) {
		try {
			FileReader fr = new FileReader(path);
			JSONParser parser = new JSONParser(fr);
			return (ArrayList<LinkedHashMap<String, Object>>) parser.parse();
		} catch (ParseException | FileNotFoundException e) {

			throw new IllegalStateException(e);
		}
	}

}
