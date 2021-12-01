import java.util.LinkedList;

public class Topology {
    private static LinkedList<Node> nodes;
	private static LinkedList<Router> routers;

    public Topology() {
        this.nodes = new LinkedList<Node>();
        this.routers = new LinkedList<Router>();
    }

    public void build( String topology ) {
		String[] auxTopology = topology.toLowerCase().split("\n"); 
        int index = 1;

        while(!auxTopology[index].equals("#router")) {
            String[] aux = auxTopology[index].split(",");
            IP tmp = new IP(aux[2]);
            nodes.add(new Node(aux[0], aux[1], tmp, new IP(aux[3] + tmp.getSufix())) );
            index++;
        }
        index++;
        while(!auxTopology[index].equals("#routertable")) {
            String[] aux = auxTopology[index].split(",");
            Router tmp = new Router(aux[0], Integer.valueOf(aux[1]));
            for( int i = 2; i < (Integer.valueOf(aux[1])*2)+2; i+=2 ) {
                tmp.addIpMac(new IP(aux[i+1]), aux[i], (i/2)-1);
            }
            routers.add(tmp);
            index++;
        }
        index++;	
        while(index < auxTopology.length) {
            String[] aux = auxTopology[index].split(",", 4);
            for( Router r : routers ) {
                if( r.getRouter_name().equals(aux[0].trim()) ) {
                    r.add(new IP(aux[1].trim()),
					new IP(aux[2].trim() + r.getIPFromPort(Integer.valueOf(aux[3].trim())).getSufix()));
                }
            }
            index++;
        }
    }

    public Node getNode(String node) {
		for( Node n : nodes ) {
			if( n.getNode_name().equals(node) ) {
				return n;
			}
		}
        System.err.println("---------- Node Not Found ----------");
        return null;
	}
	
    public LinkedList<Node> getNodes() {
        return nodes;
    }

    public LinkedList<Router> getRouters() {
        return routers;
    }

    @Override
    public String toString() {
        return nodes+"\n"+routers;
    }
}
