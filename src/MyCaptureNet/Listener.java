package MyCaptureNet;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

import javax.swing.JList;
import javax.swing.JTextArea;

/**
 * 监听器，监听主窗体的菜单项，下拉列表等
 * 
 * @author 于修彦
 *
 */
public class Listener implements ActionListener {
	private String cmd;
	private static String message;
	public static JTextArea jta_totalWord;
	private JTextArea jta_detailInfo;
	private JList list;
	private DecimalFormat df = new DecimalFormat("0.0000");

	public void setJta_totalWord(JTextArea jta_totalWord) {
		this.jta_totalWord = jta_totalWord;
	}

	public JTextArea getJta_totalWord() {
		return jta_totalWord;
	}

	public void setList(JList list) {
		this.list = list;
	}

	public JList getList() {
		return list;
	}

	public void setJta_detailInfo(JTextArea jta_detailInfo) {
		this.jta_detailInfo = jta_detailInfo;
	}

	public JTextArea getJta_detailInfo() {
		return jta_detailInfo;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		cmd = e.getActionCommand();
		if ("mi_startCap".equals(cmd)) { // 开始抓包
			// 开启抓包线程
			(new MyCapUtil()).start();
		} else if ("mi_endCap".equals(cmd)) {// 停止抓包
			MyCapUtil.stopCapturePacket();
			jta_totalWord.setText("");
			message = "Tcp:\t" + MyPacketMatch.numberOfTcp + "包\t" + df.format(MyPacketMatch.totalOfTcp) + "KB\n"
					+ "Udp:\t" + MyPacketMatch.numberOfUdp + "包\t" + df.format(MyPacketMatch.totalOfUdp) + "KB\n"
					+ "Icmp:\t" + MyPacketMatch.numberOfIcmp + "包\t" + df.format(MyPacketMatch.totalOfIcmp) + "KB\n"
					+ "Arp:\t" + MyPacketMatch.numberOfArp + "包\t" + df.format(MyPacketMatch.totalOfArp) + "KB\n"
					+ "广播包:\t" + MyPacketMatch.numberOfWideSpread + "包\t" + df.format(MyPacketMatch.totalOfSpread)
					+ "KB\n" + "总流量:\t" + MyPacketMatch.numberOfPacket + "包\t" + df.format(MyPacketMatch.totalOfIp)
					+ "MB";
			jta_totalWord.append(message);
		} else if ("mi_clean".equals(cmd)) { // 清空记录
			MyCapUtil.clearPacket();
		} else if ("mi_about".equals(cmd)) { // 关于软件
			AboutWin aw = new AboutWin();
			aw.showAboutWin();
		} else if ("mi_save".equals(cmd)) { // 保存结果到文件
			SaveFile sf = new SaveFile();
			sf.saveFile(MainWin.mainFrame, jta_totalWord.getText());
		} else if ("mi_tuxing".equals(cmd)) { // 图形显示
			BarChart bc = new BarChart();
			bc.showChart();
		}
	}

}
