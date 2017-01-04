package demo.sdmxsource.webservice.engine;

import java.io.OutputStream;
import java.io.PrintWriter;

import org.sdmxsource.sdmx.api.engine.StructureWriterEngine;
import org.sdmxsource.sdmx.api.model.beans.SdmxBeans;
import org.sdmxsource.sdmx.api.model.beans.base.MaintainableBean;

public class HtmlStructureWriterEngine implements StructureWriterEngine {
	private PrintWriter pw;
	
	public HtmlStructureWriterEngine(OutputStream out) {
		this.pw = new PrintWriter(out);
		outputStyle(pw);
		printHTMLTable();
	}

	@Override
	public void writeStructures(SdmxBeans beans) {
		for(MaintainableBean maint : beans.getAllMaintainables()) {
			printMaintainable(maint);
		}
		close();
	}
	
	@Override
	public void writeStructure(MaintainableBean maint) {
		printMaintainable(maint);
		close();
	}
	
	private void printHTMLTable() {
		pw.println("<table border='1' width='100%' id='output'>");
		pw.println("<th>Agency Id</th>");
		pw.println("<th>Structure Type</th>");
		pw.println("<th>Structure Name</th>");
		pw.println("<th># Identifiable Composites</th></tr>");
	}
	
	private void printMaintainable(MaintainableBean maint) {
		pw.println("<tr><td>"+maint.getAgencyId()+"</td>");
		pw.println("<td>"+maint.getStructureType().getType()+"</td>");
		pw.println("<td>"+maint.getName()+"</td>");
		pw.println("<td>"+maint.getIdentifiableComposites().size()+"</td></tr>");
	}
	
	private void close() {
		pw.println("</table>");
		pw.close();
	}
	
	private void outputStyle(PrintWriter pw) {
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
