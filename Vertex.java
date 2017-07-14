
package Incognito;

import java.util.ArrayList;

/**
 *
 * @author adenugad
 */
public class Vertex {
    //PrivateTable data;
    String data; // This represents QI + " " + level
                // it should hold more attributes as it
    ArrayList<Edge> incidentEdges = new ArrayList<>();
    int generationLevel;// if i generalize a vertex to get you, i add one to that 
                        //vertex's generation Level to get yours   
    String label; // unique to every vertex
    /**
     * Specifies if the table associated with this vertex has been ascertained
     * to be k-anonymous
     */
    boolean marked = false;
    
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
     
     public void setGenerationLevel(int i){
         generationLevel = i;
     }
     
     public boolean isMarked(){
         return marked;
     }
     
     public void setMark(boolean val){
         marked = val;
     }
     
     public void setLabel(String label){
         this.label = label;
     }
     
     /**
      * checks if this vertex has no adjacent vertices
      * @return boolean
      */
     public boolean isRoot(){
        return incidentEdges.isEmpty();
     }
     
     public boolean equal(Vertex vertex){
         return data.hashCode() == vertex.data.hashCode();
         //not only this though
         //incident edges should be the same
     }
     
     public Vertex copy(){
         Vertex vertex = new Vertex(data);
         vertex.generationLevel = generationLevel;
         for(int i = 0; i < incidentEdges.size(); i++){
             vertex.incidentEdges.add(incidentEdges.get(i));
         }
         return vertex;
     }
     
     public Vertex getAdjacentVertex(Edge edge){
         return edge.getAdjacentVertex(this);
     }
     
     public int getVertexHeight(){
         String[] arr = data.split(":");
         int height = 0;
         for(int i = 0; i < arr.length; i++){
            height = height + Integer.parseInt(arr[i].substring(arr[i].indexOf(" ") + 1));
         }
         return height;
     }
     
     //Insert direct generalizations of node into queue, keeping queue ordered by height
     public ArrayList<Vertex> getDirectGeneralizations(ArrayList<Vertex> generalizations){
         //ArrayList<Vertex> generalizations = new ArrayList<>();
         for(int i = 0; i < incidentEdges.size(); i++){
             //either of this should work
             Vertex v = incidentEdges.get(i).getTo();
             if(generalizations.contains(v) == false){
             //generalizations.add(incidentEdges.get(i).getAdjacentVertex(this));
             generalizations.add(incidentEdges.get(i).getTo());
             }
         }
         
         return generalizations;
     }
     
    @Override
     public String toString(){
         return this.getData();
     }
     /*public boolean equal(Vertex vertex){
        boolean result = true; 
        if(incidentEdges.size() != vertex.incidentEdges.size()){
            
        }
        if(data.getTableRows().size() == vertex.data.getTableRows().size()){ 
        for(int i = 0; i < data.getTableRows().size(); i++){
                result = data.getTableRows().get(i).
                        checkEquality(vertex.getTable().getTableRows().get(i));
                if(result == false){
                    return result;
                }        
            }
        
    return result;    
     }*/
}

