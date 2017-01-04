package demo.sdmxsource.webservice.model;

import java.util.ArrayList;
import java.util.List;

import org.sdmxsource.sdmx.api.model.format.DataQueryFormat;

public class SqlQuery implements DataQueryFormat<SqlQuery> {

	private StringBuilder sb = new StringBuilder();
	private List<String> queryParameters = new ArrayList<String>();
	
	
	public void appendSql(String sql, String... parameters) {
		sb.append(sql);
		if(parameters != null) {
			for(String currentParam : parameters) {
				queryParameters.add(currentParam);
			}
		}
	}
	
	public String getSql() {
		return sb.toString().toLowerCase();
	}
	
	public Object[] getParameters() {
		return queryParameters.toArray();
	}

	@Override
	public String toString() {
		StringBuilder params = new StringBuilder();
		String lineSep = System.getProperty("line.separator");
		params.append(lineSep+"With Parameters:");
		int i = 1;
		for(String currentParam : queryParameters) {
			params.append(lineSep+i + "-" + currentParam);
			i++;
		}
		return "SQL:" + getSql() + params.toString();
	}
	
	
}
