package demo.sdmxsource.webservice.factory;

import org.sdmxsource.sdmx.api.builder.DataQueryBuilder;
import org.sdmxsource.sdmx.api.factory.DataQueryFactory;
import org.sdmxsource.sdmx.api.model.format.DataQueryFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import demo.sdmxsource.webservice.builder.SqlQueryBuilder;
import demo.sdmxsource.webservice.model.SqlQuery;

@Service
public class SqlDataQueryFactory implements DataQueryFactory {

	@Autowired
	private SqlQueryBuilder sqlQueryBuilder;
	
	@Override
	@SuppressWarnings("unchecked")
	public <T> DataQueryBuilder<T> getDataQueryBuilder(DataQueryFormat<T> format) {
		if(format instanceof SqlQuery) {
			return (DataQueryBuilder<T>)sqlQueryBuilder;
		}
		return null;
	}
}
