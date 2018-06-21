package fer.hr.telegra.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Helper class to wrap a list of datasets. This is used for saving the list of datasets to XML
 * Xml is simple database of datasets
 * Automatically handled with getters and setters in dataset model class
 * @author dmlinaric
 *
 */
@XmlRootElement(name = "dataSets")
public class DataSetListWrapper {

		private List<DataSet> dataSets;
		
		@XmlElement(name = "dataSet")
	    public List<DataSet> getDataSets() {
	        return dataSets;
	    }
		
		public void setDataSets(List<DataSet> dataSets) {
	        this.dataSets = dataSets;
	    }
}
