
package Incognito;

import java.util.ArrayList;

/**
 *
 * @author adenugad
 */
public class Graph {

    private final ArrayList<Vertex> vertices = new ArrayList<>();
    private final ArrayList<Edge> edges = new ArrayList<>();
    
    public Graph(){
        
    }
    
     public ArrayList<Vertex> getVertices() {
        return vertices;
    }

    public ArrayList<Edge> getEdges() {
        return edges;
    }
    
    public Graph copy(){
        Graph graph = new Graph();
        for(int i = 0; i < vertices.size(); i++ ){
            graph.vertices.add(vertices.get(i));
        }
        for(int i = 0; i < edges.size(); i++ ){
            graph.edges.add(edges.get(i));
        }
        
        return graph;
    }
    
    public int numEdges(){
        return edges.size();
    }
    
    public int numVertices(){
        return vertices.size();
    }
    
    public void addEdge(Vertex x, Vertex y){
        Edge edge = new Edge(x, y);
        edges.add(edge);
        x.addIncidentEdges(edge);
        //y.addIncidentEdges(edge);
    }
    
    public void addEdge(Edge edge){
        if(edges.contains(edge) == false)
        {
        edges.add(edge);
        }
    }
    
    public void addVertex(Vertex v){
        if(vertices.contains(v) == false)
        {
        vertices.add(v);
        }
    }
    
    public void removeEdge(Edge e){
        if(edges.contains(e)){
            edges.remove(e);
        }
        e.from.getIncidentEdges().remove(e);
        //e.to.getIncidentEdges().remove(e);   
    }
    
    public void removeVertex(Vertex v){
        vertices.remove(v);
        for(int i = 0; i < v.getNumOfIncidentEdges(); i++){
            removeEdge(v.getIncidentEdges().get(i));
        }
    }
    
    public Vertex getVertex(int index){
        return vertices.get(index);
    }
    
    public void printOut(){
        //System.out.println(vertices.size());
        for(int i = 0; i < vertices.size(); i++){
            for(int j = 0; j < vertices.get(i).getNumOfIncidentEdges(); j++){
                System.out.println(vertices.get(i).getData() + " -> " 
                        + vertices.get(i).getIncidentEdges().get(j).getAdjacentVertex(vertices.get(i)).getData());
            }
            System.out.println();
        }
    }
    
    /**
     * Does graph have vertex ?
     * @param v - vertex
     * @return vertex if it's in graph
     */
    public Vertex hasVertex(Vertex v){
        for(int i = 0; i < vertices.size(); i++){
            if(vertices.get(i).equals(v)){
                return vertices.get(i);
            }
        }
        return v;
    }
    
    /**
     * Does graph have vertex ?
     * @param v - vertex
     * @return 
     */
    public boolean hasVertex2(Vertex v){
        for(int i = 0; i < vertices.size(); i++){
            if(vertices.get(i).equals(v)){
                return true;
            }
        }
        return false;
    }
    
    /**
     * all nodes(vertices) in graph with no edge directed to them
     * @return list of these vertices
     */
    public ArrayList<Vertex> getRoots(){
        ArrayList<Vertex> queue = new ArrayList<>();
        for(int j = 0; j < vertices.size(); j++){
            boolean issaRoot = false;
            for(int i = 0; i < edges.size(); i++){
                if(vertices.get(j).equals(edges.get(i).to)){
                    issaRoot = true;
                }
            }
            if(issaRoot == false){
                queue.add(vertices.get(j));
            }
        
        }
    return queue;
    }
    
   
}   
 
