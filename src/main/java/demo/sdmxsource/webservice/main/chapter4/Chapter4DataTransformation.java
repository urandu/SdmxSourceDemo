package demo.sdmxsource.webservice.main.chapter4;

import java.io.ByteArrayOutputStream;
import java.io.File;

import org.sdmxsource.sdmx.api.constants.DATA_TYPE;
import org.sdmxsource.sdmx.api.engine.DataReaderEngine;
import org.sdmxsource.sdmx.api.engine.DataWriterEngine;
import org.sdmxsource.sdmx.api.factory.ReadableDataLocationFactory;
import org.sdmxsource.sdmx.api.manager.parse.StructureParsingManager;
import org.sdmxsource.sdmx.api.manager.retrieval.SdmxBeanRetrievalManager;
import org.sdmxsource.sdmx.api.model.StructureWorkspace;
import org.sdmxsource.sdmx.api.model.beans.SdmxBeans;
import org.sdmxsource.sdmx.api.util.ReadableDataLocation;
import org.sdmxsource.sdmx.dataparser.manager.DataReaderManager;
import org.sdmxsource.sdmx.dataparser.manager.DataWriterManager;
import org.sdmxsource.sdmx.dataparser.transform.DataReaderWriterTransform;
import org.sdmxsource.sdmx.sdmxbeans.model.data.SdmxDataFormat;
import org.sdmxsource.sdmx.structureretrieval.manager.InMemoryRetrievalManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class Chapter4DataTransformation {
	@Autowired
	private StructureParsingManager structureParsingManager;

	@Autowired
	private ReadableDataLocationFactory rdlFactory;
	
	@Autowired
	private DataReaderManager dataReaderManager;
	
	@Autowired
	private DataWriterManager dataWriterManager;
	
	@Autowired
	private DataReaderWriterTransform transform;

	private void transformData(File structureFile, File dataFile) {
		//Parse Structures into SdmxBeans and build a SdmxBeanRetrievalManager
		ReadableDataLocation rdl = rdlFactory.getReadableDataLocation(structureFile);
		StructureWorkspace workspace = structureParsingManager.parseStructures(rdl);
		SdmxBeans beans = workspace.getStructureBeans(false);
		SdmxBeanRetrievalManager retreivalManager = new InMemoryRetrievalManager(beans);
		
		//Get a DataReaderEngine
		ReadableDataLocation dataLocation = rdlFactory.getReadableDataLocation(dataFile);
		DataReaderEngine dre = dataReaderManager.getDataReaderEngine(dataLocation, retreivalManager);
		
		//Get A DataWriterEngine
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		DataWriterEngine dwe = dataWriterManager.getDataWriterEngine(new SdmxDataFormat(DATA_TYPE.GENERIC_2_1), out);
		
		//Perform transformation (copy from the reader to the writer)
		transform.copyToWriter(dre, dwe, true, true);
		
		System.out.println(new String(out.toByteArray()));
	}

	public static void main(String[] args) {
		//Step 1 - Get the Application Context
		ClassPathXmlApplicationContext applicationContext = 
				new ClassPathXmlApplicationContext("spring/spring-beans-chapter1.xml");

		//Step 2 - Get the main class from the Spring beans container
		Chapter4DataTransformation main = 
				applicationContext.getBean(Chapter4DataTransformation.class);

		//Step 3 - Create a Readable Data Location from the File
		File structureFile = new File("src/main/resources/structures/chapter2/structures_full.xml");
		File dataFile = new File("src/main/resources/data/chapter3/sample_data.xml");
		
		main.transformData(structureFile, dataFile);
		
		applicationContext.close();
	}
}
