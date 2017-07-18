
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
    ArrayList <Vertex> directGen = new ArrayList<>();
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
     
     
    @Override
     public boolean equals(Object vertex){
         Vertex v = (Vertex) vertex;
         return data.hashCode() == v.data.hashCode();
         //not only this though
         //incident edges should be the same
     }
     
     public Vertex copy(){
         Vertex vertex = new Vertex(data);
         vertex.generationLevel = generationLevel;
         for(int i = 0; i < incidentEdges.size(); i++){
             vertex.incidentEdges.add(new Edge (vertex, incidentEdges.get(i).getTo()));
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
         System.out.println("Nodeee - " + this.getData());
         System.out.println("Direct Gen size: " + incidentEdges.size());
         for(int i = 0; i < incidentEdges.size(); i++){
             Vertex v = incidentEdges.get(i).getTo();
             System.out.print(" Direct Gen " + i +  " - " + v.getData() );
             //either of this should work
             
             if(generalizations.contains(v) == false){
             //generalizations.add(incidentEdges.get(i).getAdjacentVertex(this));
             generalizations.add(incidentEdges.get(i).getTo());
             }
             //Terrible way to do it but hope it works-contains doesn't fit here so i'll check every vertex in generalizations
             
             
         }
         /*System.out.println("here");
         //Seems to be adding to queue only when it's empty
          System.out.println("Nodeee: " + this.getData());
          System.out.println("Direct Gen size: " + directGen.size());
         for(int i = 0; i < directGen.size(); i++){
             System.out.print(" Direct Gen " + i +  ": " + directGen.get(i).getData() );
             if(generalizations.contains(directGen.get(i)) == false){
             //generalizations.add(incidentEdges.get(i).getAdjacentVertex(this));
             generalizations.add(directGen.get(i));
             }
         }*/
         System.out.println();
         return generalizations;
     }
     
    @Override
     public String toString(){
         return this.getData().trim();
     }
     
     public void addDirectGeneralizations(Vertex v){
         directGen.add(v);
     }
     
     public ArrayList<Vertex>  getDirectGeneralizations(){
         return directGen;
     }
     
     public int getNumOfDirectGen(){
         return directGen.size();
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

