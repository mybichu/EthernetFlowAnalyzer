package MyCaptureNet;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.jnetpcap.Pcap;
import org.jnetpcap.PcapIf;

/**
 * 抓包的工具类，偏于底层的抓包，接入jnetpcap的接口 负责网卡列表的获取、包的捕获、抓包程序的停止
 * 
 * @author 于修彦
 *
 */
public class MyCapUtil extends Thread {
	private static boolean flag = true;//网卡设备使用标志位
	public static int number = 2;
	private static StringBuilder errbuf = new StringBuilder();// 存储错误信息

	/**
	 * 用于获取设备的网卡适配器 部分代码参考jnetpcap官网案例1
	 * 
	 * @return Arrayist（网卡设备列表）
	 */
	public static ArrayList<PcapIf> CaptureNet() {
		//System.out.println("MyCapUtil.CaptureNet() is start");
		MyCapUtil.flag = false;  //是否抓包的标志位，true代表正在抓包，false 代表停止抓包

		ArrayList<PcapIf> alldevs = new ArrayList<PcapIf>();// 存储搜索到的网卡设备
        /* 这个方法构造了可以用pcap_open_live()打开的所有网络设备
        * 这个列表中的元素都是 pcap_if_t，
        * name 一个指向设备名字的指针；
        * adderess 是一个接口的地址列表的第一个元素的指针；
        * flags 一个PCAP_IF_LOOPBACK标记接口是否是loopback的
        * 失败返回-1，成功返回0
        */ 
		int r = Pcap.findAllDevs(alldevs, errbuf);// 查找设备上的网卡适配器
		// 出现异常，如果r为1或者列表为空
		if (r == Pcap.NOT_OK || alldevs.isEmpty()) {
			// 弹出错误信息提示框
			JOptionPane.showMessageDialog(null, errbuf.toString(), "获取网卡设备错误", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		//System.out.println("MyCapUtil.CaptureNet() is end");
		return alldevs;
	}
	
	/**
	 * 选取网卡设备并且捕捉数据包
	 * 部分代码参考jnetpcap官网案例1
	 * @param alldevs 设备列表
	 */
	public static void CapturePacket(ArrayList<PcapIf> alldevs){
		
		MyCapUtil.flag = true;
		PcapIf device = alldevs.get(number);
		
		//打开选中的网卡设备
		int snaplen = Pcap.DEFAULT_SNAPLEN;//截取默认长度为65535
		int flags = Pcap.MODE_PROMISCUOUS;//截取模式为混杂模式
		int timeout = 10 * 1000;//超时设置为10seconds		
		
        // openlive方法：这个方法打开一个和指定网络设备有关的，活跃的捕获器 

        // 参数：snaplen指定的是可以捕获的最大的byte数，
        // 如果 snaplen的值 比 我们捕获的包的大小要小的话，
        // 那么只有snaplen大小的数据会被捕获并以packet data的形式提供。
        // IP协议用16位来表示IP的数据包长度，所有最大长度是65535的长度
        // 这个长度对于大多数的网络是足够捕获全部的数据包的

        // 参数：flags promisc指定了接口是promisc模式的，也就是混杂模式，
        // 混杂模式是网卡几种工作模式之一，比较于直接模式：
        // 直接模式只接收mac地址是自己的帧，
        // 但是混杂模式是让网卡接收所有的，流过网卡的帧，达到了网络信息监视捕捉的目的

        // 参数：timeout 这个参数使得捕获报后等待一定的时间，来捕获更多的数据包，
        // 然后一次操作读多个包，不过不是所有的平台都支持，不支持的会自动忽略这个参数

        // 参数：errbuf pcap_open_live()失败返回NULL的错误信息，或者成功时候的警告信息   
		Pcap pcap = Pcap.openLive(device.getName(), snaplen,flags, timeout, errbuf);
		if(pcap == null){
			JOptionPane.showMessageDialog(null, errbuf.toString(),"捕捉数据包错误",JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		//获取数据包后交给handler处理,创建一个packet handler 处理器来从libpcap loop中接收数据包
		MyPacketMatch packMatch = MyPacketMatch.getInstance();
		MyPcapHandler<Object> myhandler = new MyPcapHandler<Object>();
		
		//如果在抓包
		while(MyCapUtil.flag){
			//将handler进入loop中并告诉它抓取1个包
			pcap.loop(1, myhandler,"/njnetpcap");
		}		
		//关闭pcap
		pcap.close();
	}
	
	/**
	 * 停止抓包
	 */
	public static void stopCapturePacket(){
		MyCapUtil.flag = false;
	}
	
	/**
	 * 清空记录
	 */
	public static void clearPacket(){
		MyPacketMatch.numberOfPacket=0;		
		MyPacketMatch.numberOfArp=0;
		MyPacketMatch.numberOfTcp=0;
		MyPacketMatch.numberOfUdp=0;
		MyPacketMatch.numberOfIcmp=0;
		MyPacketMatch.numberOfWideSpread=0;
		MyPacketMatch.hm.clear();
		MainWin.lItems.clear();	
		Listener.jta_totalWord.setText("");
	}
	
	public void run(){
		MyCapUtil.CapturePacket(MyCapUtil.CaptureNet());
	}
}
