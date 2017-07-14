
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
        if(to.equal(current))
            return from;
        if(from.equal(current))
            return to;
    return null;            
    }
    
    public boolean equal(Edge e){
        return to.equal(e.to) && from.equal(e.from);
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
}
