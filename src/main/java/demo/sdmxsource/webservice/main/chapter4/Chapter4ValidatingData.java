package demo.sdmxsource.webservice.main.chapter4;

import java.io.File;

import org.sdmxsource.sdmx.api.engine.DataReaderEngine;
import org.sdmxsource.sdmx.api.factory.ReadableDataLocationFactory;
import org.sdmxsource.sdmx.api.manager.retrieval.SdmxBeanRetrievalManager;
import org.sdmxsource.sdmx.api.manager.validation.DataValidationManager;
import org.sdmxsource.sdmx.api.util.ReadableDataLocation;
import org.sdmxsource.sdmx.dataparser.manager.DataReaderManager;
import org.sdmxsource.sdmx.dataparser.model.error.FirstFailureExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Chapter4ValidatingData {

	@Autowired
	SdmxBeanRetrievalManager beanRetrievalManager;

	@Autowired
	private ReadableDataLocationFactory rdlFactory;
	
	@Autowired
	private DataReaderManager dataReaderManager;
	
	private DataValidationManager dataValidationManager;
	
	public void validateData(File dataFile) {
		//Get the DataLocation, and from this the DataReaderEngine
		ReadableDataLocation dataLocation = rdlFactory.getReadableDataLocation(dataFile);
		DataReaderEngine dre = dataReaderManager.getDataReaderEngine(dataLocation, beanRetrievalManager);
		dataValidationManager.validateData(dre, new FirstFailureExceptionHandler());
		System.out.println("Data Valid");
	}
	
	public static void main(String[] args) {
		//Step 1 - Get the Application Context
		ClassPathXmlApplicationContext applicationContext = 
				new ClassPathXmlApplicationContext("spring/spring-beans-chapter4.xml");

		//Step 2 - Get the main class from the Spring beans container
		Chapter4ValidatingData main = 
				applicationContext.getBean(Chapter4ValidatingData.class);

		//Step 3 - Create a Readable Data Location from the File
		File dataFile = new File("src/main/resources/data/chapter3/sample_data.xml");

		main.validateData(dataFile);
		
		applicationContext.close();
	}

	@Required
	public void setDataValidationManager(DataValidationManager dataValidationManager) {
		this.dataValidationManager = dataValidationManager;
	}
}
