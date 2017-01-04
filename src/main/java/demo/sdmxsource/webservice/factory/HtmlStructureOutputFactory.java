package demo.sdmxsource.webservice.factory;

import java.io.OutputStream;

import org.sdmxsource.sdmx.api.engine.StructureWriterEngine;
import org.sdmxsource.sdmx.api.factory.StructureWriterFactory;
import org.sdmxsource.sdmx.api.model.format.StructureFormat;
import org.springframework.stereotype.Service;

import demo.sdmxsource.webservice.engine.HtmlStructureWriterEngine;
import demo.sdmxsource.webservice.model.HtmlOutputFormat;

@Service
public class HtmlStructureOutputFactory implements StructureWriterFactory {

	@Override
	public StructureWriterEngine getStructureWriterEngine(StructureFormat structureFormat,
														  OutputStream out) {
		if(structureFormat instanceof HtmlOutputFormat) {
			return new HtmlStructureWriterEngine(out);
		}
		return null;
	}
}

