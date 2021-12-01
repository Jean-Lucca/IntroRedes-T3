import java.util.HashMap;

class Node{

	private String node_name;
	private String mac;
	private IP ip;
	private IP gatewayIp;
	private HashMap<IP, String> arpTable;

	public Node(String node_name, String mac, IP ip, IP gatewayIp){
		this.ip = ip;
		this.mac = mac;
		this.arpTable = new HashMap<>();
		this.node_name = node_name;
		this.gatewayIp = gatewayIp;
	}

	public IP getIp() {
		return ip;
	}

	public String getMac() {
		return mac;
	}

	public String getNode_name() { 
		return this.node_name; 
	}

	public HashMap<IP, String> getArpTable() {
		return arpTable;
	}

	public void put(IP ip, String mac) {
		arpTable.put(ip, mac);
	}

	public String get(IP ip) {
		return arpTable.get(ip);
	}

	public IP getGatewayIp(){ 
		return gatewayIp; 
	}
}