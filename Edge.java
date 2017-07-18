
package Incognito;

/**
 *
 * @author adenugad
 */
public class Edge {//undirected, weightless
    Vertex to;
    Vertex from;
    
    /**
     * Constructor
     * @param from
     * @param to
     */
    public Edge(Vertex from, Vertex to){//doesn't check for  self loop
        this.from = from;
        this.to = to;
        //from.addIncidentEdges(this);
        //to.addIncidentEdges(this);
    }
    
    public Vertex getAdjacentVertex(Vertex current){
        if(to.equals(current))
            return from;
        if(from.equals(current))
            return to;
    return null;            
    }
    
    public boolean equal(Edge e){
        return to.equals(e.to) && from.equals(e.from);
    }
    
    public Edge copy(){
        return new Edge(from.copy(), to.copy());
    }
    
    public Vertex getTo(){
        return to;
    }
    
    public Vertex getFrom(){
        return from;
    }
    
    @Override
    public String toString(){
        return from.toString() + " -> " + to.toString();
    }
}
