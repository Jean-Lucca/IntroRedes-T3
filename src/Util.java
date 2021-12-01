class Util {
	//https://stackoverflow.com/questions/29034878/how-can-i-determine-network-and-broadcast-address-from-the-ip-address-and-subnet
	public static String convertIPToBinaryIP(String ip){
		StringBuilder sb = new StringBuilder();
		ip = ip.replace(".", " ");
		String[] aux = ip.split(" ", 4);
		for( int i = 0; i < aux.length; i ++ ) {
			sb.append( addPadding( Integer.toBinaryString(Integer.parseInt(aux[i])), 8) );
		}
		return sb.toString();
	}

	public static String addPadding(String str, int padding) {
        StringBuilder sb = new StringBuilder();
        for( int toPrepend = padding - str.length(); toPrepend > 0; toPrepend--) {
            sb.append('0');
        }
        sb.append(str);
        return sb.toString();
    }

	public static boolean equalNetwork(IP one, IP another) {
		return one.getNetwork().equals(another.getNetwork());
	}
}