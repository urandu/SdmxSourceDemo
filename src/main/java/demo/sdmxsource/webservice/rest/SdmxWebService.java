package demo.sdmxsource.webservice.rest;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.sdmxsource.sdmx.api.constants.DATA_TYPE;
import org.sdmxsource.sdmx.api.manager.retrieval.rest.RestDataQueryManager;
import org.sdmxsource.sdmx.api.manager.retrieval.rest.RestStructureQueryManager;
import org.sdmxsource.sdmx.api.model.data.DataFormat;
import org.sdmxsource.sdmx.api.model.format.StructureFormat;
import org.sdmxsource.sdmx.api.model.query.RESTDataQuery;
import org.sdmxsource.sdmx.api.model.query.RESTStructureQuery;
import org.sdmxsource.sdmx.sdmxbeans.model.beans.reference.RESTDataQueryImpl;
import org.sdmxsource.sdmx.sdmxbeans.model.beans.reference.RESTStructureQueryImpl;
import org.sdmxsource.sdmx.sdmxbeans.model.data.SdmxDataFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import demo.sdmxsource.webservice.model.HtmlOutputFormat;

@Configurable
public class SdmxWebService extends HttpServlet {
	private Logger LOG = Logger.getLogger(SdmxWebService.class);
	
	private static final long serialVersionUID = 2714223057996032594L;

	@Autowired
	private RestDataQueryManager dataQueryManager;
	
	@Autowired
	private RestStructureQueryManager structureQueryManager;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String restQuery = req.getPathInfo();
		Map<String, String> params = new HashMap<String, String>();
		Enumeration<String> paramNames = req.getParameterNames();
		while(paramNames.hasMoreElements()) {
			String paramName = paramNames.nextElement();
			params.put(paramName, req.getParameter(paramName));
		}
		LOG.info(restQuery);
		
		if(restQuery.toLowerCase().startsWith("/data/")) {
			RESTDataQuery dataQueryRest = new RESTDataQueryImpl(restQuery, params);
//			DataFormat dataformat = new SdmxDataFormat(DATA_TYPE.GENERIC_1_0);
			DataFormat dataformat = new HtmlOutputFormat();

			LOG.info("Data Query: " + dataQueryRest);
			LOG.info("Response Format: " + dataformat);
			
			dataQueryManager.executeQuery(dataQueryRest, dataformat, resp.getOutputStream());
		} else {
			RESTStructureQuery structureQueryRest = new RESTStructureQueryImpl(restQuery, params);
			//StructureFormat structureFormat = new SdmxStructureFormat(STRUCTURE_OUTPUT_FORMAT.SDMX_V21_STRUCTURE_DOCUMENT);
			StructureFormat structureFormat = new HtmlOutputFormat();
			structureQueryManager.getStructures(structureQueryRest, resp.getOutputStream(), structureFormat);
		}
	}
}
