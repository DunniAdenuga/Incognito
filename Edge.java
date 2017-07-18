
package Incognito;

/**
 *
 * @author Dunni
 */
public class Edge {
    //kinda directed, weightless
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
    }
    
    public Vertex getAdjacentVertex(Vertex current){
        if(to.equals(current))
            return from;
        if(from.equals(current))
            return to;
    return null;            
    }
    
    @Override
    public boolean equals(Object edge){
        if(edge.getClass() != this.getClass()){
            return false;
        }
        Edge e = (Edge) edge;
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
