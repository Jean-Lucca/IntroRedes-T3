class Simulador{

	public static Topology topology = new Topology();

	public static void main(String[] args) {
		topology.build(IO.inputFile(args[0].trim()+".txt"));
		System.out.println(args[0]+" "+args[1]+" "+args[2]+" "+args[3]);
		Node src = topology.getNode( args[2].trim().toLowerCase() );
		Node dst = topology.getNode( args[3].trim().toLowerCase() );
		if( src != null && dst != null ) {
			IO.output( "INICIO---------------------------------------------------------------\n" );
			IO.output( "Entrada: "+args[0]+" "+args[1]+" "+args[2]+" "+args[3]+"\n\n" );
			if( args[1].equals("ping") ) {
				ping(src, dst, 8);
			} else if (args[1].equals("traceroute")) {
				traceroute(src, dst, 1);
			}
			IO.output( "\n\nFIM-----------------------------------------------------------------\n" );
		}
	}

	public static void traceroute(Node srcNode, Node dstNode, int ttl) {

		if( Util.equalNetwork(srcNode.getIp(), dstNode.getIp()) ) {
			Output.echoRequest(srcNode.getNode_name(), dstNode.getNode_name(), srcNode.getIp().getIp(), dstNode.getIp().getIp(), ttl--);
			Resolve.arpResolution(srcNode, dstNode);
			ttl = 8;
			Output.echoReply(dstNode.getNode_name(), srcNode.getNode_name(), dstNode.getIp().getIp(), srcNode.getIp().getIp(), ttl--);
			return;
		}

		boolean done = false;
		while( !done ){
			done = ping(srcNode, dstNode, ttl++);
		}
	}

	public static boolean ping( Node srcNode, Node dstNode, int actualTtl ) {

		int ttl = actualTtl;
		if( Util.equalNetwork(srcNode.getIp(), dstNode.getIp()) ) {
			Resolve.arpResolution(srcNode, dstNode);
			Resolve.arpResolution(dstNode, srcNode);
			Output.echoRequest(srcNode.getNode_name(), dstNode.getNode_name(), srcNode.getIp().getIp(), dstNode.getIp().getIp(), ttl);
			Output.echoReply(dstNode.getNode_name(), srcNode.getNode_name(), dstNode.getIp().getIp(), srcNode.getIp().getIp(), ttl);
			return true;
		}

		Router router = null;
		for( Router r : topology.getRouters() ) {
			for( Pair<IP, String> p : r.getPortIpMacTable().values() ) {
				if( srcNode.getGatewayIp().getIpSufix().equals(p.getKey().getIpSufix()) ) {
					router = r;
				}
			}
		}

		IP auxIP = srcNode.getGatewayIp();
		Resolve.arpResolution( srcNode, router , auxIP );
		Output.echoRequest(srcNode.getNode_name(), router.getRouter_name(), srcNode.getIp().getIp(), dstNode.getIp().getIp(), ttl--);
		boolean done = false;
		while( !done ) {
			if( ttl == 0 ) {
				ttl = 8;
				while( !done ) {
					if( ttl == 0 ) {
						ttl=8;
						return true;
					}
					Pair<Router, IP> res = Resolve.search( router, srcNode, ttl, topology.getRouters());
					if( res != null ) {
						router = res.getKey();
						auxIP = res.getValue();
						ttl--;
					}
					done = router.isReachable(srcNode);
				}
				auxIP = router.getIp(srcNode.getIp());
				Resolve.arpResolution(router, auxIP, srcNode);
				Output.timeExceeded(router.getRouter_name(), srcNode.getNode_name(), 
				auxIP.getIp(), srcNode.getIp().getIp(), ttl--);
				return false;
			}
			Pair<Router, IP> res = Resolve.searchRequest( router, srcNode, dstNode, ttl,
			topology.getRouters() );
			if( res != null ) {
				router = res.getKey();
				auxIP = res.getValue();
				ttl--;
			}
			done = router.isReachable(dstNode);
		}

		auxIP = router.getIp(dstNode.getIp());
		Resolve.arpResolution(router, auxIP, dstNode);
		Output.echoRequest(router.getRouter_name(), dstNode.getNode_name(), srcNode.getIp().getIp(), 
		dstNode.getIp().getIp(), ttl--);
		ttl = 8;
		Output.echoReply(dstNode.getNode_name(), router.getRouter_name(), 
		dstNode.getIp().getIp(), srcNode.getIp().getIp(), ttl--);
		done = false;
		while( !done ) {
			if( ttl == 0 ) {
				ttl=8;
				//como a informacao sobre o destino esta dentro da funcao o print esta errado :)
				Output.timeExceeded(router.getRouter_name(), router.getRouter_name(), 
				dstNode.getIp().getIp(), srcNode.getIp().getIp(), ttl--);
				Output.timeExceeded(router.getRouter_name(), router.getRouter_name(), 
				dstNode.getIp().getIp(), srcNode.getIp().getIp(), ttl--);
				return false;
			}
			Pair<Router, IP> res = Resolve.searchReply( router, srcNode, 
			dstNode, ttl, topology.getRouters());
			if( res != null ) {
				router = res.getKey();
				auxIP = res.getValue();
				ttl--;
			}
			done = router.isReachable(srcNode);
		}

		auxIP = router.getIp(srcNode.getIp());
		Resolve.arpResolution(router, auxIP, srcNode);
		Output.echoReply(router.getRouter_name(), srcNode.getNode_name(), 
		dstNode.getIp().getIp(),srcNode.getIp().getIp(), ttl--);
		return true;
	}
}