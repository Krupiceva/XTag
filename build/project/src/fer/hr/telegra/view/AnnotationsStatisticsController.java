package fer.hr.telegra.view;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import fer.hr.telegra.MainApp;
import fer.hr.telegra.model.DataSet;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.XYChart;

public class AnnotationsStatisticsController {

	@FXML
    private BarChart<String, Integer> barChart;

    @FXML
    private CategoryAxis xAxis;
    private MainApp mainApp;
	private DataSet dataSet;
	
	Map<String, Integer> stat = new HashMap<String, Integer>();
    
    private void initialize() {;
    }
    
    public void setDataSet (DataSet dataSet) {
    	this.dataSet = dataSet;
    }
    
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        xAxis.setCategories(mainApp.getAnnotations());
    	for(String ant: mainApp.getAnnotations()) {
    		stat.put(ant, 0);
    	}
    	
    	try {
    		File folder = new File(dataSet.getDataSetAnnotationsLocation());
    		for(File file: folder.listFiles(XML_FILTER)) {
    			 DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    	         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
    	         Document doc = dBuilder.parse(file);
    	         doc.getDocumentElement().normalize();
    	         NodeList nList = doc.getElementsByTagName("object");
    	         for(int i= 0; i <nList.getLength(); i++) {
    	        	 Node nNode = nList.item(i);
    	        	 if (nNode.getNodeType() == Node.ELEMENT_NODE){
    	        		 Element eElement = (Element) nNode;
    	        		 System.out.println(eElement);
    	        		 if(eElement.getElementsByTagName("name").item(0).getTextContent() != null && stat.containsKey(eElement.getElementsByTagName("name").item(0).getTextContent())) {
    	        			 stat.put(eElement.getElementsByTagName("name").item(0).getTextContent(), stat.get(eElement.getElementsByTagName("name").item(0).getTextContent())+1);
    	        		 }
    	        	 }
    	         }
    		}
    		XYChart.Series<String, Integer> series = new XYChart.Series<>();
    		for(String key: stat.keySet()) {
    			series.getData().add(new XYChart.Data<>(key, stat.get(key)));
    		}
    		barChart.getData().add(series);
    		
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
        
    }
    
 // array of supported extensions (use a List if you prefer)
    static final String[] EXTENSIONS = new String[]{
        "xml" // and other formats you need
    };
    // filter to identify images based on their extensions
    static final FilenameFilter XML_FILTER = new FilenameFilter() {

        @Override
        public boolean accept(final File dir, final String name) {
            for (final String ext : EXTENSIONS) {
                if (name.endsWith("." + ext)) {
                    return (true);
                }
            }
            return (false);
        }
    };
	
}
