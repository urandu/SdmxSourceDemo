package demo.sdmxsource.webservice.manager.impl;

import org.sdmxsource.sdmx.api.engine.DataWriterEngine;
import org.sdmxsource.sdmx.api.manager.retrieval.data.SdmxDataRetrievalWithWriter;
import org.sdmxsource.sdmx.api.model.data.query.DataQuery;
import org.springframework.beans.factory.annotation.Autowired;

import demo.sdmxsource.webservice.builder.SqlQueryBuilder;
import demo.sdmxsource.webservice.model.SqlQuery;

public class DemoDataRetrievalManager implements SdmxDataRetrievalWithWriter {

	@Autowired
	private SqlQueryBuilder sqlQueryBuilder;
	
	
	@Override
	public void getData(DataQuery dataQuery, DataWriterEngine dataWriter) {
		SqlQuery query = sqlQueryBuilder.buildDataQuery(dataQuery);
		System.out.println(query.toString());
	} 
}
