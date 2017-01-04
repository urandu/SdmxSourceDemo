package demo.sdmxsource.webservice.builder;

import java.util.Set;

import org.apache.log4j.Logger;
import org.sdmxsource.sdmx.api.builder.DataQueryBuilder;
import org.sdmxsource.sdmx.api.model.beans.base.DataProviderBean;
import org.sdmxsource.sdmx.api.model.beans.datastructure.DataStructureBean;
import org.sdmxsource.sdmx.api.model.beans.datastructure.DataflowBean;
import org.sdmxsource.sdmx.api.model.data.query.DataQuery;
import org.sdmxsource.sdmx.api.model.data.query.DataQuerySelection;
import org.sdmxsource.sdmx.api.model.data.query.DataQuerySelectionGroup;
import org.sdmxsource.util.ObjectUtil;
import org.springframework.stereotype.Service;

import demo.sdmxsource.webservice.model.SqlQuery;

@Service
public class SqlQueryBuilder implements DataQueryBuilder<SqlQuery> {
	private Logger LOG = Logger.getLogger(SqlQueryBuilder.class);
	
	
	@Override
	/**
	 * This is example code and incomplete.
	 * 
	 * Builds a SqlQuery from a DataQuery instance
	 * 
	 * This method needs to be implemented to return a String which is the SQL query string that has been created from the DataQuery object.
	 * The DataQuery contains a DataStructure and Dataflow for the query, and other optional parameters
	 * 
	 * This method has been implemented to the degree that it iterates through the user selections but does not generate any SQL, this needs to be 
	 * implemented to create a query relevant to the database that is being queried.
	 * 
	 * Any parameters that can not be implemented for any reason, can be handled by the data writer (for example it can handle a pivot on dimension) - 
	 * this additional support for providing automatic filtering has not yet been fully implemented.
	 * 
	 */
	public SqlQuery buildDataQuery(DataQuery buildFrom) {
		LOG.debug("Execute Query Request : " + buildFrom.toString());
		//TODO IMPLEMENT ME IF data.discovery is streamingDataSource
		
		//1. CONSTRUCT UserQuery object to return - 
		SqlQuery sqlQuery = new SqlQuery();
		
		
		//2. EXTRACT AND STORE HIGH LEVEL PARAMETERS FROM QUERY
		DataStructureBean dataStructure = buildFrom.getDataStructure();					//Mandatory
		String databaseTable = dataStructure.getAgencyId() + "_"+dataStructure.getId();
		sqlQuery.appendSql("select * from " + databaseTable);
		
		DataflowBean dataflow = buildFrom.getDataflow();                    //Mandatory
		sqlQuery.appendSql(" where dataflow=?",dataflow.getId());
		
		Set<DataProviderBean> dataProviders = buildFrom.getDataProvider();  //Optional
		Integer lastNObs = buildFrom.getLastNObservations();                //Optional
		Integer firstNObs = buildFrom.getFirstNObservations();                //Optional
		String dimensionAtObs = buildFrom.dimensionAtObservation();         //Optional

		//3. LOOP THROUGH DATA PROVIDERS ON QUERY - CAN IGNORE IF NO CONCEPT OF THIS IN DATABASE
		if(ObjectUtil.validCollection(dataProviders)) {          
			boolean hasMultipleDataProviders = buildFrom.getDataProvider().size() > 1;
			if(hasMultipleDataProviders) {
				for(DataProviderBean currentDataProvider : buildFrom.getDataProvider()) {
					//DO A FILTER ON DATA PROVIDERS e.g WHERE PROVIDER IN(IMF,ECB,FAO)
				}
			} else {
				DataProviderBean provider = (DataProviderBean)buildFrom.getDataProvider().toArray()[0];
				//DO A FILTER ON DATA PROVIDERS e.g WHERE PROVIDER=IMF
			}
		}
		
		//4. Loop through the selection groups
		for(DataQuerySelectionGroup selectionGroup : buildFrom.getSelectionGroups()) {
			//5. ADD FILTER ON CODE SELECTIONS FOR DIMENSIONS THAT HAVE SELECTIONS - OF WHICH THERE MAY NOT BE ANY
			for(DataQuerySelection selection : selectionGroup.getSelections()) {
				String dimensionId = selection.getComponentId();
				if(selection.hasMultipleValues()) {
					sqlQuery.appendSql(" and "+dimensionId+" IN (");
					//DO A FILTER ON THE VALUES SELECTED IN THIS DIMENSION  e.g. WHERE COUNTRY IN(UK,FR,NZ)
					for(String currentSelectionValue : selection.getValues()) {
						sqlQuery.appendSql("?, ", currentSelectionValue);
					}
				} else {
					//DO A FILTER ON THE VALUE SELECTED IN THIS DIMENSION  e.g. WHERE COUNTRY=UK
					sqlQuery.appendSql(" and "+dimensionId+" = ?", selection.getValue());
				}
			}
			//6. ADD DATE FILTERS (BOTH OPTIONAL)
			if(selectionGroup.getDateFrom() != null) {
				
			}
			if(selectionGroup.getDateTo() != null) {
				
			}
		}
		
		//7. OPTIONALLY THERE IS SOME ADDITIONAL DETAIL THAT MAY BE NEEDED TO FILTER THE QUERY RESULT
		switch(buildFrom.getDataQueryDetail()) {
		case DATA_ONLY :
			break;
		case FULL :
			break;
		case NO_DATA : 
			break;
		case SERIES_KEYS_ONLY :
			break;
		}
		return sqlQuery;
	}
	
}
