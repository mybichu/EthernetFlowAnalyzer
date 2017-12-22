package MyCaptureNet;

import java.util.HashMap;

import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.protocol.network.Arp;
import org.jnetpcap.protocol.network.Icmp;
import org.jnetpcap.protocol.network.Ip4;
import org.jnetpcap.protocol.tcpip.Tcp;
import org.jnetpcap.protocol.tcpip.Udp;

/**
 * 数据包处理类,设计为单例模式
 * 
 * @author 于修彦
 *
 */
public class MyPacketMatch {

	public static HashMap<Integer, String> hm = new HashMap<Integer, String>();// 存储捕捉到的数据包
	public static int numberOfPacket = 0;// 数据包数量
	private static MyPacketMatch mpm; // 单例化

	private Icmp icmp = new Icmp();// 处理icmp数据包
	private Tcp tcp = new Tcp();// 处理tcp数据包
	private Udp udp = new Udp();// 处理udp数据包
	private Arp arp = new Arp();// 处理arp包
	private Ip4 ip4 = new Ip4();// 处理IPv4的信息，主要辅助获取广播信息

	public static double totalOfIcmp = 0; // 统计icmp数据流量
	public static double totalOfTcp = 0; // 统计tcp数据流量
	public static double totalOfUdp = 0; // 统计udp数据流量
	public static double totalOfArp = 0; // 统计arp数据流量
	public static double totalOfIp = 0; // 统计ip数据流量
	public static double totalOfSpread = 0; // 统计广播数据流量

	public static int numberOfWideSpread = 0;// 统计广播包数量
	public static int numberOfTcp = 0;// 统计tcp包数量
	public static int numberOfUdp = 0;// 统计udp包数量
	public static int numberOfIcmp = 0;// 统计icmp包数量
	public static int numberOfArp = 0;// 统计arp包数量

	/**
	 * 单例化 获取MyPacketMatch的实例
	 * 
	 * @return MyPacketMatch的实例
	 */
	public static MyPacketMatch getInstance() {
		if (mpm == null) {
			mpm = new MyPacketMatch();
		}
		return mpm;
	}

	/**
	 * 为数据包分类(tcp包,udp包,arp包,icmp包,广播包)
	 * 
	 * @param packet
	 *            一个pcap的数据报
	 */
	public void handlePacket(PcapPacket packet) {
		MyPacketMatch.totalOfIp += packet.getTotalSize() / (1024.0 * 1024.0);// 单位：MB

		if (packet.hasHeader(icmp)) {
			handleIcmp(packet);
		}
		if (packet.hasHeader(arp)) {
			handleArp(packet);
		}
		if (packet.hasHeader(tcp)) {
			handleTcp(packet);
		}
		if (packet.hasHeader(udp)) {
			handleUdp(packet);
		}
		if (packet.hasHeader(ip4)) {
			handleIp4(packet);
		}

	}

	/**
	 * 广播包的处理方法 受限广播 它不被路由发送，但会被送到相同物理网络段上的所有主机
	 * IP地址的网络字段和主机字段全为1就是地址255.255.255.255
	 * 
	 * @param packet
	 */
	private void handleIp4(PcapPacket packet) {
		packet.getHeader(ip4);
		if (MyPacketMatch.int2Ip(ip4.destinationToInt()).equals("255.255.255.255")) {
			// System.out.println("收到了一个广播包");
			MainWin.lItems.add(numberOfPacket, "广播数据包");

			hm.put(numberOfPacket, "广播数据包\n" + ip4.toString());

			numberOfWideSpread++;

			totalOfSpread += (ip4.getLength() / 1024.0); // 单位：KB

			numberOfPacket++;
		}
	}

	/**
	 * tcp包的处理方法
	 * 
	 * @param packet
	 */
	private void handleTcp(PcapPacket packet) {
		packet.getHeader(tcp);
		// System.out.println(tcp.toString());
		hm.put(numberOfPacket, packet.toString());

		MainWin.lItems.add(numberOfPacket, "TCP Packet");

		numberOfTcp++;

		totalOfTcp += tcp.getLength() / 1024.0; // 单位为KB

		numberOfPacket++;

	}

	/**
	 * udp包的处理方法
	 * 
	 * @param packet
	 */
	private void handleUdp(PcapPacket packet) {
		packet.getHeader(udp);
		// System.out.println("udp来源端口"+udp.toString());
		hm.put(numberOfPacket, packet.toString());

		MainWin.lItems.add(numberOfPacket, "UDP Packet");

		numberOfUdp++;

		totalOfUdp += udp.getLength() / 1024.0; // 单位转为KB

		numberOfPacket++;
	}

	/**
	 * arp包的处理方法
	 * 
	 * @param packet
	 */
	private void handleArp(PcapPacket packet) {
		packet.getHeader(arp);
		// System.out.println("arp:"+arp.toString());
		hm.put(numberOfPacket, arp.toString());

		MainWin.lItems.add(numberOfPacket, "ARP Packet");

		numberOfArp++;

		totalOfArp += arp.getLength() / 1024.0;

		numberOfPacket++;
	}

	/**
	 * icmp包的处理方法
	 * 
	 * @param icmp
	 */
	private void handleIcmp(PcapPacket packet) {
		packet.getHeader(icmp);
		// System.out.println("icmp:"+icmp.toString());
		hm.put(numberOfPacket, icmp.toString());

		MainWin.lItems.add(numberOfPacket, "ICMP Packet");

		numberOfIcmp++;

		totalOfIcmp += icmp.getLength() / 1024.0;

		numberOfPacket++;
	}

	/**
	 * 将int类型转换为IP地址
	 * 
	 * @param ipInt
	 * @return IP-String
	 */
	public static String int2Ip(int ipInt) {
		return new StringBuilder().append(((ipInt >> 24) & 0xff)).append('.').append((ipInt >> 16) & 0xff).append('.')
				.append((ipInt >> 8) & 0xff).append('.').append((ipInt & 0xff)).toString();
	}

}
