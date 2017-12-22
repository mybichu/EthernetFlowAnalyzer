package MyCaptureNet;
import javax.swing.JOptionPane;
/**
 * “关于软件”界面的实现类
 * @author 于修彦
 *
 */
public class AboutWin {
	String str = "软件名：基于winpcap的以太网流量分析器\n" + "版本：v1.0.0\n" + "作者：mybichu";

	// 显示
	public void showAboutWin() {
		// 弹窗
		JOptionPane.showMessageDialog( MainWin.mainFrame, str,"关于软件",JOptionPane.INFORMATION_MESSAGE);
	}
	
}
