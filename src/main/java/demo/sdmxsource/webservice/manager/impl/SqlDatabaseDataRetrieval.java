package demo.sdmxsource.webservice.manager.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.sdmxsource.sdmx.api.constants.SDMX_STRUCTURE_TYPE;
import org.sdmxsource.sdmx.api.engine.DataWriterEngine;
import org.sdmxsource.sdmx.api.manager.retrieval.data.SdmxDataRetrievalWithWriter;
import org.sdmxsource.sdmx.api.model.beans.datastructure.AttributeBean;
import org.sdmxsource.sdmx.api.model.beans.datastructure.DataStructureBean;
import org.sdmxsource.sdmx.api.model.beans.datastructure.DimensionBean;
import org.sdmxsource.sdmx.api.model.data.query.DataQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import demo.sdmxsource.webservice.builder.SqlQueryBuilder;
import demo.sdmxsource.webservice.model.SqlQuery;

/**
 * Class retrieves data from a SQL database
 */
public class SqlDatabaseDataRetrieval  extends JdbcDaoSupport implements SdmxDataRetrievalWithWriter {

	@Autowired
	private SqlQueryBuilder sqlQueryBuilder;
	
	
	@Override
	public void getData(DataQuery dataQuery, DataWriterEngine dataWriter) {
		//Build a representation of the query in SQL
		SqlQuery query = sqlQueryBuilder.buildDataQuery(dataQuery);
	
		//Create a call back handler
		ResultSetExtractor rsCallbackHandler = new ResultSetExtractor(dataQuery.getDataStructure(), dataWriter);
		
		//Execute the query
		super.getJdbcTemplate().query(query.getSql(), query.getParameters(), rsCallbackHandler);
	}
	
	
	private class ResultSetExtractor implements RowCallbackHandler {
		private DataWriterEngine dataWriter;
		private DataStructureBean dsd;
		private String lastKey = null;
		
		public ResultSetExtractor(DataStructureBean dsd, DataWriterEngine dataWriter) {
			this.dataWriter = dataWriter;
			this.dsd = dsd;
		}

		@Override
		public void processRow(ResultSet rs) throws SQLException {
			String seriesKey = rs.getString("seriesKey");
			//If this is a new Series key, write the series key, and the series attributes
			if(!seriesKey.equals(lastKey)) {
				lastKey = seriesKey;
				for(DimensionBean dim : dsd.getDimensions(SDMX_STRUCTURE_TYPE.DIMENSION)) {
					dataWriter.writeSeriesKeyValue(dim.getId(), rs.getString(dim.getId()));
				}
				for(AttributeBean attr : dsd.getSeriesAttributes(DimensionBean.TIME_DIMENSION_FIXED_ID)) {
					dataWriter.writeAttributeValue(attr.getId(), rs.getString(attr.getId()));
				}
			}
			//Write Observation
			dataWriter.writeObservation(rs.getString("obstime"), rs.getString("obsvalue"));
			
			//Write Observation Attributes
			for(AttributeBean attr : dsd.getObservationAttributes(DimensionBean.TIME_DIMENSION_FIXED_ID)) {
				dataWriter.writeAttributeValue(attr.getId(), rs.getString(attr.getId()));
			}
		}
	}
}


