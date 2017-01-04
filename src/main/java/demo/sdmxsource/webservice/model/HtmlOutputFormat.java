package demo.sdmxsource.webservice.model;

import org.sdmxsource.sdmx.api.constants.DATA_TYPE;
import org.sdmxsource.sdmx.api.constants.STRUCTURE_OUTPUT_FORMAT;
import org.sdmxsource.sdmx.api.model.data.DataFormat;
import org.sdmxsource.sdmx.api.model.format.StructureFormat;

public class HtmlOutputFormat implements StructureFormat, DataFormat {
	private static final long serialVersionUID = 1L;

	@Override
	public STRUCTURE_OUTPUT_FORMAT getSdmxOutputFormat() {
		return null;
	}

	@Override
	public DATA_TYPE getSdmxDataFormat() {
		return null;
	}

	@Override
	public String getFormatAsString() {
		return "HTML";
	}
}

