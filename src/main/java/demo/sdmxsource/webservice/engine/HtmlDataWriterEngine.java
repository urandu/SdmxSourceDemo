package demo.sdmxsource.webservice.engine;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sdmxsource.sdmx.api.constants.TIME_FORMAT;
import org.sdmxsource.sdmx.api.engine.DataWriterEngine;
import org.sdmxsource.sdmx.api.manager.retrieval.SdmxSuperBeanRetrievalManager;
import org.sdmxsource.sdmx.api.model.beans.base.AnnotationBean;
import org.sdmxsource.sdmx.api.model.beans.datastructure.DataStructureBean;
import org.sdmxsource.sdmx.api.model.beans.datastructure.DataflowBean;
import org.sdmxsource.sdmx.api.model.beans.datastructure.DimensionBean;
import org.sdmxsource.sdmx.api.model.beans.datastructure.PrimaryMeasureBean;
import org.sdmxsource.sdmx.api.model.header.DatasetHeaderBean;
import org.sdmxsource.sdmx.api.model.header.HeaderBean;
import org.sdmxsource.sdmx.api.model.superbeans.base.ComponentSuperBean;
import org.sdmxsource.sdmx.api.model.superbeans.codelist.CodelistSuperBean;
import org.sdmxsource.sdmx.api.model.superbeans.datastructure.DataStructureSuperBean;

public class HtmlDataWriterEngine implements DataWriterEngine {
	private SdmxSuperBeanRetrievalManager superBeanRetrievalManager;
	private DataStructureSuperBean superBean;
	private PrintWriter pw;


	private Map<String, String> keyValue;
	private List<String> keys = new ArrayList<String>();

	public HtmlDataWriterEngine(SdmxSuperBeanRetrievalManager superBeanRetrievalManager, OutputStream out) {
		this.superBeanRetrievalManager =superBeanRetrievalManager;
		this.pw = new PrintWriter(out);
	}
	
	@Override
	public void startDataset(DataflowBean dataflow, DataStructureBean dataStructureBean, DatasetHeaderBean header, AnnotationBean... annotations) {
		this.superBean = superBeanRetrievalManager.getDataStructureSuperBean(dataStructureBean.asReference().getMaintainableReference());
	
		outputStyle();
		pw.write("<body><table id='output'><tr>");
		for(ComponentSuperBean dim : superBean.getComponents()) {
			keys.add(dim.getId());
			pw.write("<td>"+dim.getConcept().getName()+"</td>");
			
		}
	}

	@Override
	public void writeObservation(String observationConceptId,String obsConceptValue, String obsValue,AnnotationBean... annotations) {
		writeObservation(obsConceptValue, obsValue);
	}


	@Override
	public void writeObservation(Date obsTime, String obsValue,TIME_FORMAT sdmxTimeFormat, AnnotationBean... annotations) {}

	@Override
	public void writeHeader(HeaderBean header) {}
	
	@Override
	public void startGroup(String groupId, AnnotationBean... annotations) {}

	public void writeGroupKeyValue(String id, String value) {}

	@Override
	public void startSeries(AnnotationBean... annotations) {
		keyValue = new HashMap<String, String>();		
	}

	public void writeSeriesKeyValue(String id, String value) {
		keyValue.put(id, value);
	}

	public void writeAttributeValue(String id, String value) {
		keyValue.put(id, value);
	}

	@Override
	public void writeObservation(String obsConceptValue, String obsValue, AnnotationBean... annotations) {
		outputKey();
		obsId = obsConceptValue;
		this.obsValue = obsValue;
	}

	private String obsId;
	private String obsValue;
	boolean alt = false;
	private void outputKey() {
		if(obsId != null) {
			if(alt) {
				pw.write("<tr>");
				alt = false;
			} else {
				alt = true;
				pw.write("<tr class='alt'>");
			}
			for(String currentKey : keys) {
				if(currentKey.equals(DimensionBean.TIME_DIMENSION_FIXED_ID)) {
					pw.write("<td>"+obsId+"</td>");
				} else if(currentKey.equals(PrimaryMeasureBean.FIXED_ID)) {
					pw.write("<td>"+obsValue+"</td>");
				} else {
					String value = keyValue.get(currentKey);
					if(value == null) {
						value = "-";
					} else {
						ComponentSuperBean comp = superBean.getComponentById(currentKey);
						if(comp != null) {
							CodelistSuperBean codelist = comp.getCodelist(true);
							if(codelist != null && codelist.getCodeByValue(value) != null) {
								value = codelist.getCodeByValue(value).getName();
							}
						}
					}
					pw.write("<td>"+value+"</td>");
				}
			}
			pw.write("</tr>");
		}
	}


	@Override
	public void close(FooterMessage... footer) {
		outputKey();
		pw.write("</tr></table></body></html>");
		pw.close();
	}
	
	private void outputStyle() {
		pw.println("<html>");
		pw.println("<head>");
		pw.println("<style type='text/css'>");
		pw.println("#output");
		pw.println("{");
		pw.println("font-family:Arial, Helvetica, sans-serif;");
		pw.println("width:100%;");
		pw.println("border-collapse:collapse;");
		pw.println("}");
		pw.println("#output td, #customers th ");
		pw.println("{");
		pw.println("font-size:1em;");
		pw.println("border:1px solid #98bf21;");
		pw.println("padding:3px 3px 2px 3px;");
		pw.println("}");
		pw.println("#output th ");
		pw.println("{");
		pw.println("font-size:1.1em;");
		pw.println("text-align:left;");
		pw.println("padding-top:5px;");
		pw.println("padding-bottom:4px;");
		pw.println("background-color:#A7C942;");
		pw.println("color:#ffffff;");
		pw.println("}");
		pw.println("#output tr.alt td ");
		pw.println("{");
		pw.println("color:#000000;");
		pw.println("background-color:#EAF2D3;");
		pw.println("}");
		pw.println("</style>");
		pw.println("</head>");
	}
}
