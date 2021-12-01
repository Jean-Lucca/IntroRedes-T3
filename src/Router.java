import java.util.*;

class Router {
	private String router_name;
	private int num_ports;
	private HashMap<IP, String> arpTable;
	private LinkedList<Pair<IP, IP>> routerTable;
	private HashMap<Integer, Pair<IP, String>> portIpMacTable;

	public Router(String router_name, int ports) {
		this.router_name = router_name;
		this.num_ports = ports;
		this.arpTable = new HashMap<>();
		this.routerTable = new LinkedList<Pair<IP, IP>>();
		this.portIpMacTable = new HashMap<>();
	}

	public String getRouter_name() {
		return this.router_name;
	}

	public int getNumPort() {
		return this.num_ports;
	}

	public IP getIPFromPort(Integer port) {
		return this.portIpMacTable.get(port).getKey();
	}

	public HashMap<Integer, Pair<IP, String>> getPortIpMacTable(){ 
		return this.portIpMacTable; 
	}

	public void add(IP ip, IP nextHop) {
		this.routerTable.add(new Pair<IP, IP>(ip, nextHop));
	}

	public void addIpMac(IP ip, String mac, Integer port) {
		this.portIpMacTable.put(port, new Pair<IP, String>(ip, mac));
	}

	public IP getIp(IP ip) {
		Pair<IP, String> ipMac = null;
		for ( Pair<IP, String> p : this.portIpMacTable.values()) {
			if( Util.equalNetwork(p.getKey(), ip)){
				ipMac = p;
				break;
			}
		}
		return ipMac.getKey();
	}

	public String getMac(IP ip) {
		Pair<IP, String> ipMac = null;
		for ( Pair<IP, String> p : this.portIpMacTable.values()) {
			if( Util.equalNetwork(p.getKey(), ip)){
				ipMac = p;
				break;
			}
		}
		return ipMac.getValue();
	}

	public void put(IP ip, String mac) {
		this.arpTable.put(ip, mac);
	}

	public String get(IP ip) {
		return this.arpTable.get(ip);
	}

	public boolean isReachable(Node node) {
		for( Pair<IP, IP> pair : routerTable ) {
			if( Util.equalNetwork(pair.getKey(), node.getIp()) 
			&& pair.getValue().getIp().equals("0.0.0.0") ){
				return true;
			}
		}
		return false;
	}

	public IP getHop(Node node) {
		for( Pair<IP, IP> pair : routerTable ) {
			if( Util.equalNetwork(pair.getKey(), node.getIp()) ) {
				return pair.getValue();
			}
		}
		for( Pair<IP, IP> pair : routerTable ) {
			if( pair.getKey().getIp().equals("0.0.0.0") ) {
				return pair.getValue();
			}
		}
		return null;
	}
}