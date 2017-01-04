package demo.sdmxsource.webservice.factory;

import java.io.OutputStream;

import org.sdmxsource.sdmx.api.engine.DataWriterEngine;
import org.sdmxsource.sdmx.api.factory.DataWriterFactory;
import org.sdmxsource.sdmx.api.manager.retrieval.SdmxBeanRetrievalManager;
import org.sdmxsource.sdmx.api.model.data.DataFormat;
import org.sdmxsource.sdmx.structureretrieval.manager.SdmxSuperBeanRetrievalManagerImpl;
import org.springframework.beans.factory.annotation.Required;

import demo.sdmxsource.webservice.engine.HtmlDataWriterEngine;
import demo.sdmxsource.webservice.model.HtmlOutputFormat;

public class HtmlDataWriterFactory implements DataWriterFactory {
	
	private SdmxBeanRetrievalManager beanRetrievalManager;
	
	@Override
	public DataWriterEngine getDataWriterEngine(DataFormat dataFormat, OutputStream out) {
		if(dataFormat instanceof HtmlOutputFormat) {
			return new HtmlDataWriterEngine(new SdmxSuperBeanRetrievalManagerImpl(beanRetrievalManager), out);
		}
		return null;
	}

	
	@Required
	public void setBeanRetrievalManager(SdmxBeanRetrievalManager beanRetrievalManager) {
		this.beanRetrievalManager = beanRetrievalManager;
	}
}
