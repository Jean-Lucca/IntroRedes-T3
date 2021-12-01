class IP {

	private String ipSufix;
	private String ip;
	private int sufix;
	private String network;

	public IP(String ipSufix) {
		this.ipSufix = ipSufix.trim();
		String[] aux = ipSufix.split("/");
		this.sufix = Integer.parseInt(aux[1]);
		this.ip = aux[0];
		this.network =  Util.convertIPToBinaryIP(ip).substring(0, sufix);
	}

	public String getIpSufix() { 
		return ipSufix; 
	}

	public String getNetwork() {
		return network;
	}

	public String getSufix() { 
		return "/" + sufix;
	}

	public String getIp() {
		return ip;
	}
}