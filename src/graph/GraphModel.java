package graph;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.ListIterator;
import java.util.Vector;

import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import patient.PatientModel;
import patientModelSet.PatientModelSet;

public class GraphModel extends JPanel implements PropertyChangeListener {
	/**
	 * Logger log;
	 */
	private static final Logger log = Logger.getLogger(GraphModel.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 9186449122376956878L;

	private static final String PROPERTYNAME_SURVIVAL = "Survival";

	private static final String PROPERTYNAME_HAZARD = "Hazard";

	protected ChartPanel chartPanel = new ChartPanel(null);

	private XYSeriesCollection survivalGraphDataSet;

	private XYSeriesCollection hazardGraphDataSet;

	protected XYPlot survivalPlot = null;

	protected XYPlot hazardPlot = null;

	protected JFreeChart survivalChart = null;

	protected JFreeChart hazardChart = null;

	private String graphType;

	public GraphModel() {
		log.debug("GraphModel");
		this.setLayout(new BorderLayout());

		graphType = PROPERTYNAME_SURVIVAL;

		survivalGraphDataSet = new XYSeriesCollection();
		survivalChart = ChartFactory.createXYLineChart(null, // chart
				// title
				"Years", // x axis label
				"%", // y axis label
				survivalGraphDataSet, // data
				PlotOrientation.VERTICAL, false, // include legend
				true, // tooltips
				false // urls
				);

		survivalChart.setAntiAlias(true);

		survivalPlot = survivalChart.getXYPlot();

		hazardGraphDataSet = new XYSeriesCollection();

		hazardChart = ChartFactory.createXYLineChart(null, // chart
				// title
				"Years", // x axis label
				"Hazard", // y axis label
				hazardGraphDataSet, // data
				PlotOrientation.VERTICAL, false, // include legend
				true, // tooltips
				false // urls
				);
		// renderer.setDrawShapes(true);
		hazardChart.setAntiAlias(true);

		hazardPlot = hazardChart.getXYPlot();

		// Survival curves always range from 0 to 100%.
		final NumberAxis rangeAxis = (NumberAxis) survivalPlot.getRangeAxis();
		rangeAxis.setAutoRange(false);
		rangeAxis.setRange(0, 100);

		// By default we plot survival.
		chartPanel.setChart(survivalChart);

		survivalChart.setBorderVisible(true);
		survivalChart.setAntiAlias(true);

		// chartPanel.setPreferredSize(new Dimension(500,500));
		chartPanel.setVisible(true);
		this.add(chartPanel, BorderLayout.CENTER);
	}

	/**
	 * 
	 * @param dataset
	 */
	public void addDataset(PatientModel patient) {

		Vector<Vector<XYSeries>> data;
		data = patient.getSurvivalDatasets();

		if (data == null)
			return;

		// The data stuff
		Vector<XYSeries> series = data.elementAt(0);
		Vector<XYSeries> upperSeries = data.elementAt(1);
		Vector<XYSeries> lowerSeries = data.elementAt(2);

		log.debug("Number of series: " + series.size());

		for (int i = 0; i < series.size(); i++) {
			// Add the datasets.
			survivalGraphDataSet.addSeries(series.elementAt(i));
			survivalGraphDataSet.addSeries(upperSeries.elementAt(i));
			survivalGraphDataSet.addSeries(lowerSeries.elementAt(i));
			setDatasetColor(patient);
		}

		// Now setup the plot legend.
		setupLegendItems(series);

		data = patient.getHazardDatasets();
		series = data.elementAt(0);
		upperSeries = data.elementAt(1);
		lowerSeries = data.elementAt(2);

		for (int i = 0; i < series.size(); i++) {
			// Add the datasets.
			hazardGraphDataSet.addSeries(series.elementAt(i));
			hazardGraphDataSet.addSeries(upperSeries.elementAt(i));
			hazardGraphDataSet.addSeries(lowerSeries.elementAt(i));

			setDatasetColor(patient);
		}

	}

	protected void setupLegendItems(Vector<XYSeries> series) {
		LegendTitle lgTitle = survivalChart.getLegend();
		
		if(series.size() < 2){
			survivalChart.removeLegend();
			hazardChart.removeLegend();
			return;
		}
		if(lgTitle == null) {
			survivalPlot.setFixedLegendItems(null);
			survivalChart.addLegend(new LegendTitle(survivalPlot));
			hazardPlot.setFixedLegendItems(null);
			hazardChart.addLegend(new LegendTitle(hazardPlot));
		}else{
			return;
		}
		
		// We want a custom legend, not including confidence lines.
		LegendItemCollection lgCol = new LegendItemCollection();

		// Now add every third item starting with item 0... 0, 3, 6..
		LegendItemCollection lgDisplay = survivalPlot.getLegendItems();
		log.debug("Number of legendItems: " + lgDisplay.getItemCount());
		log.debug("Number of series in plot: " + survivalPlot.getSeriesCount());

		int inc = survivalPlot.getSeriesCount()/3;
		inc = lgDisplay.getItemCount()/inc;
		log.debug(inc);
		
		for (int i = 0; i < lgDisplay.getItemCount(); i=i+inc) {
			lgCol.add(lgDisplay.get(i));
			log.debug("legend index i=" + i);
		}
		
		survivalPlot.setFixedLegendItems(lgCol);
		hazardPlot.setFixedLegendItems(lgCol);

	}

	public void removeDataset(PatientModel patient) {
		Vector<Vector<XYSeries>> data;

		data = patient.getSurvivalDatasets();

		if (data == null)
			return;

		Vector<XYSeries> series = data.elementAt(0);
		Vector<XYSeries> upperSeries = data.elementAt(1);
		Vector<XYSeries> lowerSeries = data.elementAt(2);

		log.debug("Removing " + patient.getPatientName());
		for (int i = 0; i < series.size(); i++) {

			survivalGraphDataSet.removeSeries(series.elementAt(i));
			survivalGraphDataSet.removeSeries(upperSeries.elementAt(i));
			survivalGraphDataSet.removeSeries(lowerSeries.elementAt(i));
		}

		data = patient.getHazardDatasets();
		series = data.elementAt(0);
		upperSeries = data.elementAt(1);
		lowerSeries = data.elementAt(2);
		for (int i = 0; i < series.size(); i++) {

			hazardGraphDataSet.removeSeries(series.elementAt(i));
			hazardGraphDataSet.removeSeries(upperSeries.elementAt(i));
			hazardGraphDataSet.removeSeries(lowerSeries.elementAt(i));
		}
	}

	public void setDatasetColor(PatientModel patient) {
		
		Vector<Color> cols = patient.getColor();
		
		log.debug("Number of colors to set: " + cols.size());
		
		//Color color = cols.elementAt(0);

		Vector<Vector<XYSeries>> data = patient.getSurvivalDatasets();
		log.debug("Number of dataset to color: " + data.size());
		
		Vector<XYSeries> series = data.elementAt(0);
		log.debug("Number of dataseries to color: " + series.size());
		Vector<XYSeries> upperSeries = data.elementAt(1);
		Vector<XYSeries> lowerSeries = data.elementAt(2);
		
		List<XYSeries> survSeries = survivalGraphDataSet.getSeries();
		setDatasetColor(survivalPlot, survSeries, series,
				cols, false);
		setDatasetColor(survivalPlot, survSeries,
				upperSeries, cols, true);
		setDatasetColor(survivalPlot, survSeries,
				lowerSeries, cols, true);
		// survivalGraphDataSet.getSeries()
		data = patient.getHazardDatasets();
		series = data.elementAt(0);
		upperSeries = data.elementAt(1);
		lowerSeries = data.elementAt(2);

		List<XYSeries> hazSeries = hazardGraphDataSet.getSeries();
		setDatasetColor(hazardPlot, hazSeries, series, cols,
				false);
		setDatasetColor(hazardPlot, hazSeries, upperSeries,
				cols, true);
		setDatasetColor(hazardPlot, hazSeries, lowerSeries,
				cols, true);

	}

	// TODO Change the algorithm to color the curves.
	private void setDatasetColor(XYPlot plot, List<XYSeries> series, Vector<XYSeries> data,
			Vector<Color> color, boolean ci) {
		
		if (color == null || color.size() <1) {
			color = new Vector<Color>();
			color.add(Color.red);
			log.debug("Setting default color");
		}
		
		for (int i = 0; i < data.size(); i++) {
			XYSeries dataset = data.elementAt(i);
			log.debug("length of color vector:" +color.size());
			log.debug("Setting graphic color: " + color.elementAt(i).toString());
			ListIterator<XYSeries> listItr = series.listIterator();

			// customize the renderer...
			final XYItemRenderer renderer = plot.getRenderer();

			listItr = series.listIterator();
			while (listItr.hasNext()) {
				final int index = listItr.nextIndex();
				final XYSeries comp = series.get(index);
				if (dataset.equals(comp)) {
					renderer.setSeriesPaint(index, color.elementAt(i));
					if (ci) {
						renderer.setSeriesStroke(index, new BasicStroke(1.0f,
								BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1.0f,
								new float[] { 4.0f, 16.0f }, 1.0f));
					} else {
						renderer.setSeriesStroke(index, new BasicStroke());
					}
					log.debug("Set curve " + index + " to "
							+ color.elementAt(i).toString());
				}

				listItr.next();
			}
		}

	}

	public void propertyChange(PropertyChangeEvent event) {
		// What properties should we listen to?
		PatientModelSet pSet;
		PatientModel pModel;
		log.debug("Graph propertyChange: " + event.getPropertyName());
		// PatientModelSet.
		if (event.getPropertyName().equals(
				PatientModelSet.PROPERTYNAME_GRAPH_TITLE_STRING)) {
			pSet = (PatientModelSet) event.getSource();
			log.debug("GraphTitle");
			survivalChart.setTitle(pSet.getGraphTitleString());
			hazardChart.setTitle(pSet.getGraphTitleString());
		} else if (event.getPropertyName().equals(
				PatientModelSet.PROPERTYNAME_LEGENDLOCATION)) {
			pSet = (PatientModelSet) event.getSource();
			log.debug("Legend Mods");

		} else if (event.getPropertyName().equals(
				PatientModelSet.PROPERTYNAME_CURVEMODEL)) {
			pSet = (PatientModelSet) event.getSource();
			setGraphType(pSet.getCurveModel());
		}

		// If we've got a patient event.
		if (event.getPropertyName().equals(PatientModel.PROPERTYNAME_CONFIDENCE)) {
			updateData((PatientModel) event.getSource());
		}
		if (event.getPropertyName().equals(PatientModel.PROPERTYNAME_PATIENT_COLOR)) {
			pModel = (PatientModel) event.getSource();
			setDatasetColor(pModel);
		}
	}

	private void updateData(PatientModel patient) {
		// TODO Turning off confidence limits still needs work.

		// Comes off a confidence limit change. So we need to find the dataset, then
		// set the condifence limits visibility
		Vector<Vector<XYSeries>> sData = patient.getSurvivalDatasets();
		Vector<Vector<XYSeries>> hData = patient.getHazardDatasets();

		Vector<XYSeries> sSeries = sData.elementAt(0);
		Vector<XYSeries> sUpperSeries = sData.elementAt(1);
		Vector<XYSeries> sLowerSeries = sData.elementAt(2);

		// Vector<XYSeries> hSeries = hData.elementAt(0);
		Vector<XYSeries> hUpperSeries = hData.elementAt(1);
		Vector<XYSeries> hLowerSeries = hData.elementAt(2);

		int index;

		for (int i = 0; i < sSeries.size(); i++) {
			XYItemRenderer renderer = survivalPlot.getRenderer();
			index = survivalPlot.getDataset().indexOf(
					sUpperSeries.elementAt(i).getKey());
			log.debug("Setting " + index + " " + patient.isConfidenceLimits());

			renderer.setSeriesVisible(index, patient.isConfidenceLimits());
			index = survivalPlot.getDataset().indexOf(
					sLowerSeries.elementAt(i).getKey());
			log.debug("Setting " + index + " " + patient.isConfidenceLimits());
			renderer.setSeriesVisible(index, patient.isConfidenceLimits());

			renderer = hazardPlot.getRenderer();
			index = hazardPlot.getDataset().indexOf(
					hUpperSeries.elementAt(i).getKey());
			log.debug("Setting " + index + " " + patient.isConfidenceLimits());
			renderer.setSeriesVisible(index, patient.isConfidenceLimits());
			index = hazardPlot.getDataset().indexOf(
					hLowerSeries.elementAt(i).getKey());
			log.debug("Setting " + index + " " + patient.isConfidenceLimits());
			renderer.setSeriesVisible(index, patient.isConfidenceLimits());
		}
	}

	public String getGraphType() {
		return graphType;
	}

	public void setGraphType(String graphType) {
		this.graphType = graphType;

		if (graphType.equals(PROPERTYNAME_HAZARD)) {
			chartPanel.setChart(hazardChart);
		} else {
			// By default we plot survival.
			chartPanel.setChart(survivalChart);
		}
		// chartPanel.setPreferredSize(new Dimension(500,500));
		chartPanel.setVisible(true);
	}

	public void resetLegend() {
		survivalChart.removeLegend();
		hazardChart.removeLegend();
	}
}
