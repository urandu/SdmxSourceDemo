package demo.sdmxsource.test.webservice.builder;

import org.junit.Assert;
import org.junit.Test;
import org.sdmxsource.sdmx.api.model.beans.base.AgencySchemeBean;
import org.sdmxsource.sdmx.api.model.beans.codelist.CodelistBean;
import org.sdmxsource.sdmx.api.model.beans.conceptscheme.ConceptSchemeBean;
import org.sdmxsource.sdmx.api.model.beans.datastructure.DataStructureBean;
import org.sdmxsource.sdmx.api.model.beans.datastructure.DataflowBean;

import demo.sdmxsource.webservice.main.chapter1.builder.AgencySchemeBuilder;
import demo.sdmxsource.webservice.main.chapter1.builder.CodelistBuilder;
import demo.sdmxsource.webservice.main.chapter1.builder.ConceptSchemeBuilder;
import demo.sdmxsource.webservice.main.chapter1.builder.DataStructureBuilder;
import demo.sdmxsource.webservice.main.chapter1.builder.DataflowBuilder;

/**
 * Tests all the builds in the system, responsible for creating structures
 */
public class TestBuilders {

	@Test
	public void testBuildCountryCodelist() {
		CodelistBean countryCodelist = new CodelistBuilder().buildCountryCodelist();
		Assert.assertNotNull(countryCodelist);
	}
	
	@Test
	public void testBuildIndicatorCodelist() {
		CodelistBean indicatorCodelist = new CodelistBuilder().buildIndicatorCodelist();
		Assert.assertNotNull(indicatorCodelist);
	}
	
	@Test
	public void testBuildAgencyScheme() {
		AgencySchemeBean agencyScheme = new AgencySchemeBuilder().buildAgencyScheme();
		Assert.assertNotNull(agencyScheme);
	}
	
	@Test
	public void testBuildConceptScheme() {
		ConceptSchemeBean conceptScheme = new ConceptSchemeBuilder().buildConceptScheme();
		Assert.assertNotNull(conceptScheme);
	}
	
	@Test
	public void testBuildDataStructure() {
		DataStructureBean dsd = new DataStructureBuilder().buildDataStructure();
		Assert.assertNotNull(dsd);
	}
	
	@Test
	public void testBuildDataFlow() {
		DataStructureBean dsd = new DataStructureBuilder().buildDataStructure();
		DataflowBean flow = new DataflowBuilder().buildDataflow("TEST", "Test Flow", dsd);
		Assert.assertNotNull(flow);
	}
}
