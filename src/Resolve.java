import java.util.LinkedList;

public class Resolve {
	
    public static void arpResolution(Node src, Node dst) {
		if( src.get(dst.getIp()) == null) {
			src.put(dst.getIp(), dst.getMac());
			Output.arpRequest( src.getNode_name(), src.getIp().getIp(), 
			dst.getIp().getIp() );
			dst.put(src.getIp(), src.getMac());
			Output.arpReply(dst.getMac(), dst.getNode_name(), 
			dst.getIp().getIp(), src.getNode_name());
		}
	}
	public static void arpResolution(Router src, IP srcIp, Node dst) {
		if( src.get(dst.getIp()) == null ) {
			src.put(dst.getIp(), dst.getMac());
			Output.arpRequest( src.getRouter_name(), srcIp.getIp(), 
			dst.getIp().getIp() );
			dst.put(srcIp, src.getMac(srcIp));
			Output.arpReply(dst.getMac(), dst.getNode_name(), 
			dst.getIp().getIp(), src.getRouter_name());
		}
	}
	public static void arpResolution(Router src, Router dst, IP srcIp, IP dstIp) {
		if( src.get(dstIp) == null ) {
			String srcMac = dst.getMac(dstIp);
			src.put(dstIp, srcMac);
			Output.arpRequest(src.getRouter_name(), 
			srcIp.getIp() , dstIp.getIp() );
			dst.put(srcIp, src.getMac(srcIp));
			Output.arpReply(srcMac, dst.getRouter_name(), 
			dstIp.getIp(), src.getRouter_name());
		}
	}
	public static void arpResolution(Node src, Router dst, IP dstIp) {
		if( src.get(dstIp) == null ) {
			src.put(dstIp, dst.getMac(dstIp));
			Output.arpRequest( src.getNode_name(), src.getIp().getIp(), 
			src.getGatewayIp().getIp() );
			dst.put(src.getIp(), src.getMac());
			Output.arpReply(dst.getMac(dstIp), dst.getRouter_name(),
			src.getGatewayIp().getIp(), src.getNode_name());
		}
	}

	public static Pair<Router, IP> search(Router router, 
	Node srcNode, int ttl , LinkedList<Router> routerList) {
		IP nextHop = router.getHop(srcNode);
		for( Router r : routerList ) {
			for( Pair<IP, String> p : r.getPortIpMacTable().values() ) {
				if(p.getKey().getIpSufix().equals(nextHop.getIpSufix())) {
					IP routerIP = router.getIp(nextHop);
					Resolve.arpResolution(router, r, routerIP, p.getKey());
					Output.timeExceeded(router.getRouter_name(), r.getRouter_name(), 
					router.getIp(routerIP).getIp(), srcNode.getIp().getIp(), ttl);
					return new Pair<Router, IP>(r, routerIP);
				}
			}
		}
		return null;
	}

	public static Pair<Router, IP> searchRequest( Router router, 
	Node srcNode, Node dstNode, int ttl, LinkedList<Router> routerList) {
		for( Router r: routerList ) {
			for( Pair<IP, String> p : r.getPortIpMacTable().values()) {
				if( p.getKey().getIpSufix().equals(router.getHop(dstNode).getIpSufix())) {
					IP routerIP = router.getIp(p.getKey());
					Resolve.arpResolution(router, r, routerIP, p.getKey());
					
					Output.echoRequest(router.getRouter_name(), 
					r.getRouter_name(), srcNode.getIp().getIp(), 
					dstNode.getIp().getIp(), ttl);
					return new Pair<Router, IP>(r, routerIP);
				}
			}
		}
		return null;
	}

	public static Pair<Router, IP> searchReply( Router router, 
	Node srcNode, Node dstNode, int ttl, LinkedList<Router> routerList ) {
		for( Router r : routerList ){
			for( Pair<IP, String> p : r.getPortIpMacTable().values() ) {
				if( p.getKey().getIpSufix().equals(router.getHop(srcNode).getIpSufix() )){
					IP routerIP = router.getIp(p.getKey());
					Resolve.arpResolution( router, r, routerIP, p.getKey() );
					Output.echoReply(router.getRouter_name(), r.getRouter_name(), 
					dstNode.getIp().getIp(), srcNode.getIp().getIp(), ttl);
					return new Pair<Router, IP>(r, routerIP);
				}
			}
		}
		return null;
	}
}
