package com.properties;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;

import com.models.DBTest;
import com.models.URLTest;
import com.models.ChainTest;

public interface Loader {
	public List<URLTest> loadURLTests(String machine);
	public List<DBTest> loadDBTests(String machine);
	public List<ChainTest> loadChainTests(String machine) throws JsonParseException, JsonMappingException, IOException;
	public List<String> getMachineNames();
	public List<Map<String,Object>> getTestNames();
	public Map<String,Object> loadTest(String id);
}
