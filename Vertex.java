
package Incognito;

import java.util.ArrayList;

/**
 *
 * @author Dunni
 */
public class Vertex {
    private final String data; // This represents QI + " " + level, it should hold more attributes as it
    private ArrayList<Edge> incidentEdges = new ArrayList<>(); 
    /**
     * Specifies if the table associated with this vertex has been ascertained
     * to be k-anonymous
     */
    private boolean marked = false;
    
    public Vertex(String data2){
        data = data2;
    }
    
    public void addIncidentEdges(Edge edge){
        if(incidentEdges.contains(edge) == false){
            incidentEdges.add(edge);
        }
    }
    
     public void removeIncidentEdges(Edge edge){
        if(incidentEdges.contains(edge) == true){
            incidentEdges.remove(edge);
        }
    }
     
     public String getData(){
         return data;
     }
     
     public int getNumOfIncidentEdges(){
         return incidentEdges.size();
     }
     
     public ArrayList<Edge> getIncidentEdges(){
         return incidentEdges;
     }
     
     public boolean isMarked(){
         return marked;
     }
     
     public void setMark(boolean val){
         marked = val;
     }
     
     /**
      * checks if this vertex has no adjacent vertices
      * @return boolean
      */
     public boolean isRoot(){
        return incidentEdges.isEmpty();
     }
     
     
    @Override
     public boolean equals(Object vertex){
         Vertex v = (Vertex) vertex;
         return data.hashCode() == v.data.hashCode();
         //not only this though
         //incident edges should be the same
     }
     
     public Vertex copy(){
         Vertex vertex = new Vertex(data);
         for(int i = 0; i < incidentEdges.size(); i++){
             vertex.incidentEdges.add(new Edge (vertex, incidentEdges.get(i).getTo()));
         }
         return vertex;
     }
     
     public Vertex getAdjacentVertex(Edge edge){
         return edge.getAdjacentVertex(this);
     }
     
     /**
      * Addition of heights of every attribute represented in this vertex
      * @return integer value
      */
     public int getVertexHeight(){
         String[] arr = data.split(":");
         int height = 0;
         for(int i = 0; i < arr.length; i++){
            height = height + Integer.parseInt(arr[i].substring(arr[i].indexOf(" ") + 1));
         }
         return height;
     }
     
     /**
      * Insert direct generalizations of node into queue, keeping queue ordered by height
      * @param generalizations - queue
      * @return list of vertices that this vertex directs towards
      */
     public ArrayList<Vertex> getDirectGeneralizations(ArrayList<Vertex> generalizations){
         for(int i = 0; i < incidentEdges.size(); i++){
             Vertex v = incidentEdges.get(i).getTo();
                    if(generalizations.contains(v) == false)
                    {
                    generalizations.add(incidentEdges.get(i).getTo());
                    } 
         }
         return generalizations;
     }
     
    @Override
     public String toString(){
         return this.getData().trim();
     }
}

