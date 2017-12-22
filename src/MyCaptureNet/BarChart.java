package MyCaptureNet;

import java.awt.BorderLayout;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 * 生成柱状图的类
 * @author 于修彦
 *
 */
public class BarChart {
	//柱状图对象
	public static JFreeChart barChart = ChartFactory.createBarChart("数据包统计结果", "数据包类型", "数量", createDataset(),
			PlotOrientation.HORIZONTAL, true, true, false);
	
	/**
	 * 数据集
	 * @return dataset
	 */
	private static CategoryDataset createDataset() {
		final String tcp = "TCP";
		final String udp = "UDP";
		final String arp = "ARP";
		final String icmp = "ICMP";
		final String widespread = "广播包";
		final String number = "包数量";
		final DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		dataset.addValue(MyPacketMatch.numberOfTcp, tcp, number);
		dataset.addValue(MyPacketMatch.numberOfUdp, udp, number);
		dataset.addValue(MyPacketMatch.numberOfArp, arp, number);
		dataset.addValue(MyPacketMatch.numberOfIcmp, icmp, number);
		dataset.addValue(MyPacketMatch.numberOfWideSpread, widespread, number);
		

		return dataset;
	}

	/**
	 * 显示图表
	 */
	public void showChart() {
		ChartPanel myChart = new ChartPanel(barChart);
		MainWin.jp_tuxingArea.setLayout(new java.awt.BorderLayout()); //border布局
		MainWin.jp_tuxingArea.add(myChart,BorderLayout.CENTER);
		MainWin.jp_tuxingArea.validate();  //设置为生效
	}
}
