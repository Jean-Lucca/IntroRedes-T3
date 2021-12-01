public class Output {
    public static void arpRequest(String src_name, String src_IP, String dst_IP) {
		String msg = "Note over "+ src_name +": ARP Request<br/>Who has "+ dst_IP +"? Tell "+ src_IP;
        System.out.println(msg);
		IO.output(msg+"\n");
	}

	public static void arpReply(String src_MAC, String src_name, String src_IP, String dst_name) {
		String msg = src_name +" ->> "+ dst_name +": ARP Reply<br/>"+src_IP+" is at "+src_MAC;
        System.out.println(msg);
		IO.output(msg+"\n");
	}

	public static void echoRequest(String src_name, String dst_name, String src_IP, String dst_IP, int ttl) {
		String msg = src_name +" ->> "+ dst_name+ " : ICMP Echo Request<br/>src="+src_IP+" dst="+dst_IP+" ttl="+ttl;
        System.out.println(msg);
		IO.output(msg+"\n");
	}

	public static void echoReply(String src_name, String dst_name, String src_IP, String dst_IP, int ttl) {
		String msg = src_name +" ->> "+ dst_name+" : ICMP Echo Reply<br/>src="+src_IP+" dst="+dst_IP+" ttl="+ttl;
        System.out.println(msg);
		IO.output(msg+"\n");
	}

	public static void timeExceeded(String src_name, String dst_name, String src_IP, String dst_IP, int ttl) {
		String msg = src_name +" ->> "+ dst_name+" : ICMP Time Exceeded<br/>src="+src_IP+" dst="+dst_IP+" ttl="+ttl;
        System.out.println(msg);
		IO.output(msg+"\n");
	}
}
