
package Incognito;

import datafly.DGHTree;
import datafly.DataFly;
import datafly.PrivateTable;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 *
 * @author adenugad
 */
public class deleted {
    Graph mainGraph;
    PrivateTable table;
    DataFly dataFly;
    ArrayList<String> quasiCombinationList;
     /**
         * Modify quasi combination list to include level in DGH
         * I can create graph from here, make vertex, 
         * add possible incident Edges
     * @param table
     * @param noOfAttribute
     * @throws java.io.FileNotFoundException
         */
        public void createGraphFromQuasiCombinationList(PrivateTable table, int noOfAttribute) throws FileNotFoundException{
            //Graph graph = new Graph();
            // lot of fors
            //I should add labels for DGHTrees 
            //but I know they were created in order Race, DOB, ID
            //should I assume, i know the number of quasiID
            ArrayList<DGHTree> dghTrees = dataFly.createDGHTrees(table);
            ArrayList<String> quasiIden = table.getQuasiIden().getData();
           
            String[] input = new String[quasiIden.size()];
            Incognito incognito = null;
            incognito.combination(quasiIden.toArray(input), new String[quasiIden.size()],
                     0, quasiIden.size() - 1, 0, noOfAttribute);  
            for(int i = 0; i < quasiCombinationList.size(); i++){
                Vertex vertex = new Vertex(quasiCombinationList.get(i));
                //graph.addVertex(vertex);
                String[] arr = quasiCombinationList.get(i).split(":");
                //System.out.println("arr- " + Arrays.toString(arr));
                for(int j = 0; j < arr.length; j++){
                    //System.out.println("arr[j]- " + arr[j]);
                    int treeIndex = checkListForCorrectDGH(dghTrees, arr[j]);
                    //System.out.println("tree " + treeIndex);
                    int dghHeight = dghTrees.get(treeIndex).getHeight();
                    int currHeightOfVertex = Integer.parseInt(arr[j].substring(arr[j].indexOf(" ")+1));
                    if(currHeightOfVertex + 1 <= dghHeight){
                        currHeightOfVertex = currHeightOfVertex + 1;
                    }
                    String possibleVertex = "";
                    for(int k = 0; k < arr.length; k++){
                      if(k == j){
                          possibleVertex = possibleVertex  +
                                  arr[j].substring(0, arr[j].indexOf(" ")+1 ) 
                                  + String.valueOf(currHeightOfVertex) + ":";
                      }
                      else{
                          possibleVertex = possibleVertex  + arr[k] + ":";
                      }
                    }
                    Vertex vertex2 = new Vertex (possibleVertex.substring(0, 
                            possibleVertex.lastIndexOf(":")));
                    Edge edge = new Edge(vertex, vertex2);
                    vertex.addIncidentEdges(edge);vertex2.addIncidentEdges(edge);
                    mainGraph.addVertex(vertex);mainGraph.addVertex(vertex2);
                    mainGraph.addEdge(edge);
                }
            }
          
        }
        
        public int checkListForCorrectDGH(ArrayList<DGHTree> dghTrees, String dghLabel){
           // System.out.println("dghLabel I'm looking for - " + dghLabel);
            //System.out.println("dghLabel I'm looking for2 - " + dghLabel.substring(0, dghLabel.indexOf(" ")));
            for(int i = 0; i < dghTrees.size(); i++){
                //System.out.println("dghLabel in the list - " + dghTrees.get(i).getLabel());
                //System.out.println("index-  " + dghLabel.indexOf(" "));
                
                if(dghTrees.get(i).getLabel().equalsIgnoreCase(dghLabel.substring(0, dghLabel.indexOf(" ")))){
                    return i;
                }
            }
            return -1;
        }
        
        public ArrayList<String> combineWithDGHeight(String valueToBeCombined, DGHTree dghTree){
            ArrayList<String> possibleVertices = new ArrayList<>();
            for(int i = 1; i < dghTree.getHeight(); i++){
                String newValue = valueToBeCombined + ":" + dghTree.getLabel() + " " + String.valueOf(i);
                possibleVertices.add(newValue);
            }
            return possibleVertices;
        }
        
        
        public void edgeUpVertex(Vertex v, ArrayList<DGHTree> dghTrees){
            System.out.println("v.getData - " + v.getData());
            String[] arr = v.getData().split(":");
                for(int j = 0; j < arr.length; j++){
                    int treeIndex = checkListForCorrectDGH(dghTrees, arr[j]);
                    int dghHeight = dghTrees.get(treeIndex).getHeight();
                    int currHeightOfVertex = Integer.parseInt(arr[j].substring(arr[j].indexOf(" ")+1));
                    while(currHeightOfVertex + 1 <= dghHeight){
                        currHeightOfVertex = currHeightOfVertex + 1;
                    
                    String possibleVertex = "";
                    for(int k = 0; k < arr.length; k++){
                      if(k == j){
                          possibleVertex = possibleVertex + ":" +
                                  arr[j].substring(0, arr[j].indexOf(" ")+1 ) + " " + String.valueOf(currHeightOfVertex);
                      }
                      else{
                          possibleVertex = possibleVertex + ":" + arr[k];
                      }
                    }
                    Vertex vertex2 = new Vertex (possibleVertex);
                    Edge edge = new Edge(v, vertex2);
                    v.addIncidentEdges(edge);vertex2.addIncidentEdges(edge);
                    //mainGraph.addVertex(v);
                    mainGraph.addVertex(vertex2);
                    mainGraph.addEdge(edge);
                    }
                }
        }

    
}
