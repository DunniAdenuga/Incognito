
/* Incognito k-Anonymisation algorithm as defined by LeFevre et al*/

package Incognito;

import datafly.DGHTree;
import datafly.DataFly;
import datafly.PrivateTable;
import java.io.FileNotFoundException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Properties;
import java.util.Scanner;

/**
 *
 * @author adenugad
 */
    public class Incognito {
    
        ArrayList<String>  quasiCombinationList;// = new ArrayList<>() ;
        Graph mainGraph;
        ArrayList<String> listOfkAnon = new ArrayList<>();
        LinkedHashMap<String, Graph> rAttributeGen;
        DataFly dataFly = new DataFly();
        PrivateTable table;
        
        public static void main(String[] args) throws FileNotFoundException, SQLException, ClassNotFoundException  {
        Incognito incognito = new Incognito();
        
        /****/
        Class.forName("org.postgresql.Driver");
        String url = "jdbc:postgresql://audgendb.c9az8e0qjbgo.us-east-1.rds.amazonaws.com:5432/data";
        Properties props = new Properties();
        props.setProperty("user", "adenugad");//UPDATE
        props.setProperty("password", "Stanbic@349");//UPDATE
        incognito.dataFly.setConn(DriverManager.getConnection(url, props)); //uncomment when connecting to DB
        incognito.table = incognito.dataFly.setup();
        
        //ask user for k
        System.out.print("What's k ? ");
        Scanner keyboard = new Scanner(System.in);
        int kAnon = keyboard.nextInt();
        
        incognito.getQuasiCombinations(incognito.table, 1);//should go till n // change to 1
        incognito.createGraphsForRattributes();
        //System.out.println(Arrays.toString(incognito.quasiCombinationList.toArray()));
        ArrayList<Vertex> queue ;
        //for(int i = 1; i <= 2; i++){
        for(int i = 1; i <= incognito.table.getQuasiIden().getData().size(); i++){
            
        /*incognito.getQuasiCombinations(incognito.table, i + 1);
        incognito.createGraphsForRattributes();*/
        for(int x = 0; x < incognito.quasiCombinationList.size(); x++){
        //there should be a while here too, for all graphs formed in rAttribute
        //Graph tempGraph = incognito.rAttributeGen.get(incognito.quasiCombinationList.get(i-1));
        Graph tempGraph = incognito.rAttributeGen.get(incognito.quasiCombinationList.get(x));//change to get x
        //System.out.println("size " + incognito.rAttributeGen.size());
        tempGraph.printOut();
        queue = incognito.sortByHeight(tempGraph.getRoots());
        //System.out.println(Arrays.toString(queue.toArray()));
        while(queue.isEmpty() == false){
            Vertex node = queue.remove(0);
            //System.out.println(node.getData());
            boolean issaKnon ;//= false; //checks if it's k-Anonymous
            if(node.isMarked() == false){
                issaKnon = incognito.generalizeWithLevel(node.getData(), kAnon);
                System.out.println("Am I k-Anon ? " + issaKnon);
                if(issaKnon == true){
                    System.out.println("node: " + node.getData());
                    System.out.println("Direct-Generalizations:\n" + Arrays.toString(node.getIncidentEdges().toArray()));
                    incognito.markAllDirectGeneralizations(node);
                }
                else{
                    
                    System.out.println("node: " + node.getData());
                    queue = node.getDirectGeneralizations(queue);// add generalizations of current node to queue
                    System.out.println("Direct-Generalizations:\n" + Arrays.toString(queue.toArray()));
                    queue = incognito.sortByHeight(queue);//i should be adding to the queue not replacing values
                    System.out.println("Direct-Generalizations (Arranged):\n" + Arrays.toString(queue.toArray()));
                    //tempGraph.removeVertex(node); i don't know 
                }
            }
            
          }
        // from each graph add to listOfKAnon Strings that are kAnon 
            //incognito.addListOfGeneralizations(tempGraph);
       }
        incognito.getQuasiCombinations(incognito.table, i + 1);
        incognito.createGraphsForRattributes();
        }
       System.out.println("Combinations that are k-Anon:" );//+ Arrays.toString(incognito.listOfkAnon.toArray()));
       for(int m = 0; m < incognito.listOfkAnon.size(); m++){
           System.out.println(incognito.listOfkAnon.get(m));
       }
     }
        
        public void getQuasiCombinations(PrivateTable table) throws FileNotFoundException{
            
           ArrayList<String> quasiIden = table.getQuasiIden().getData();
           for(int i = 1; i <= quasiIden.size();i++){
             String[] input = new String[quasiIden.size()];
             combination(quasiIden.toArray(input), new String[quasiIden.size()],
                     0, quasiIden.size() - 1, 0, i);  
           }
        }
        
        public void getQuasiCombinations(PrivateTable table, int r) throws FileNotFoundException{
           quasiCombinationList = new ArrayList<>() ; 
           ArrayList<String> quasiIden = table.getQuasiIden().getData();
           
             String[] input = new String[quasiIden.size()];
             combination(quasiIden.toArray(input), new String[quasiIden.size()],
                     0, quasiIden.size() - 1, 0, r);  
          
        }
        
        /**
         * @param input[]  ---> Input Array
        @param temp[] ---> Temporary array to store current combination
        @param start & 
        *@param end ---> Staring and Ending indexes in input[]
        @param index  ---> Current index in data[]
        @param r ---> Size of a combination to be printed
        * As inspired by
        * http://www.geeksforgeeks.org/print-all-possible-combinations-of-
        * r-elements-in-a-given-array-of-size-n/
     * @throws java.io.FileNotFoundException
        */
        public void combination(String[] input, String[] temp, int start, 
                int end,int index, int r) throws FileNotFoundException{
            //Current combination is ready to be added to list
            if(index == r){
                String combine = temp[0] + " "+ getDGHeight(temp[0]);
                for(int j = 1; j < r; j++){
                    combine = combine + ":" + temp[j] + " "+ getDGHeight(temp[j]);
                }
                quasiCombinationList.add(combine);
            }
        // replace index with all possible elements. The condition
        // "end-i+1 >= r-index" makes sure that including one element
        // at index will make a combination with remaining elements
        // at remaining positions
        for (int i=start; i<=end && end-i+1 >= r-index; i++)
        {
            temp[index] = input[i];
            combination(input, temp, i+1, end, index+1, r);
        }
        }
        
        public String getDGHeight(String quasi) throws FileNotFoundException{
            ArrayList<DGHTree> dghTrees = dataFly.createDGHTrees(table);
            for(int i = 0; i < dghTrees.size(); i++){
          
                if(dghTrees.get(i).getLabel().equalsIgnoreCase(quasi)){
                    return String.valueOf(dghTrees.get(i).getHeight());
                }
            }
            return "";
        }
        
        public void descendFromTopVertex(Vertex v){
            /*if(checkIfAtBottom(v) == true){
                //should stop recursion, i hope
                return;
            }  */ 
            //System.out.println("v.getD - " + v.getData());
            String [] arr = v.getData().split(":");
            //System.out.print("arr - ");  System.out.println(Arrays.toString(arr));
            LinkedHashMap<String, Integer> quasiIDTopHeights = new LinkedHashMap<>();
            for(int i = 0; i < arr.length; i++){
                quasiIDTopHeights.put(arr[i].substring(0, arr[i].indexOf(" ")),
                        Integer.parseInt(arr[i].substring(arr[i].indexOf(" ") + 1)));
                //System.out.print("quasiIDTop2 - ");  System.out.println(quasiIDTopHeights);
            }
            //System.out.print("quasiIDTop - ");  System.out.println(quasiIDTopHeights);
            String[] quasiID = new String[quasiIDTopHeights.size()]; 
            quasiID = quasiIDTopHeights.keySet().toArray(quasiID);
            //System.out.print("quasiID - ");  System.out.println(Arrays.toString(quasiID));
            
            for(int i = 0; i < quasiID.length; i++){//what I'm going for here ? I get it
                int oldValue = quasiIDTopHeights.get(quasiID[i]);
                int newValue = quasiIDTopHeights.get(quasiID[i]) - 1;
                if(newValue >= 0){
                quasiIDTopHeights.replace(quasiID[i], quasiIDTopHeights.get(quasiID[i]), quasiIDTopHeights.get(quasiID[i]) - 1) ; 
                
                String possibleVertex = "";
                for(int j = 0; j < quasiIDTopHeights.size(); j++){
                    possibleVertex = possibleVertex + quasiID[j].trim() + " " +  
                            String.valueOf(quasiIDTopHeights.get(quasiID[j])) + ":";
                }
                quasiIDTopHeights.replace(quasiID[i], oldValue);
                //System.out.println("here2 - " + possibleVertex);
                Vertex vertex2 = new Vertex (possibleVertex.substring(0, 
                            possibleVertex.lastIndexOf(":")));
                vertex2 = mainGraph.hasVertex(vertex2);
                Edge edge = new Edge(vertex2, v);
                //v.addIncidentEdges(edge);
                //System.out.println(v.getData());
                vertex2.addIncidentEdges(edge);// adding direct generalization
                vertex2.addDirectGeneralizations(v);//this isn't working
                //System.out.println("here - " + vertex2.getData());
                mainGraph.addEdge(edge);
                //mainGraph.addVertex(v);
                mainGraph.addVertex(vertex2);
               }  
            }
            /*for(int x = 0; x < v.getNumOfIncidentEdges(); x++){
                descendFromTopVertex(v.getAdjacentVertex(v.getIncidentEdges().get(x)));
            }*/
            
            //mainGraph.printOut();
            
        }
        
        public boolean checkIfAtBottom(Vertex v){
           
            String [] arr = v.getData().split(":");
            for(int i = 0; i < arr.length; i++){
                int index = Integer.parseInt(arr[i].split(" ")[1]);
                if(index != 0){
                    return false;
                }
            }
            return true;
        }
        
        public void createGraphsForRattributes(){
            rAttributeGen = new LinkedHashMap<>();
            for(int j = 0; j < quasiCombinationList.size(); j++){
            //System.out.println(quasiCombinationList.get(j));
        
       
        Vertex topRoot = new Vertex(quasiCombinationList.get(j));
        mainGraph = new Graph();
        mainGraph.addVertex(topRoot);
        int origLength = mainGraph.vertices.size();
        descendFromTopVertex(mainGraph.getVertex(0));

        while(checkIfAtBottom(mainGraph.getVertex(origLength)) == false){
        int newLength = mainGraph.vertices.size();
        if(newLength > origLength){
            for(int i = origLength; i < newLength; i++){
                descendFromTopVertex(mainGraph.getVertex(i));
            }
        }
        origLength = newLength;
        }
        
        rAttributeGen.put(quasiCombinationList.get(j), mainGraph.copy());
        //rAttributeGen.get(quasiCombinationList.get(j)).printOut();
            }
        }
       
         public ArrayList<Vertex> sortByHeight(ArrayList<Vertex> queue){
        return MergeSort.sort(queue);
        }
         /**
          * Generalizes original table by quasi ID and level of quasiS
          * then gets freq set of generated table
          * @param s
          * @param kAnon
          * @return true if generated table is kAnon
          * @throws java.io.FileNotFoundException
          */
         public boolean generalizeWithLevel(String s, int kAnon) throws FileNotFoundException{
             PrivateTable newTable = table.copy();
             //System.out.println("s " + s);
             ArrayList<DGHTree> dghTrees = dataFly.createDGHTrees(newTable);
             //System.out.println("dghT " + dghTrees.size());
             String[] arr = s.split(":");
             int index = -1;//of correct dghTree
             //System.out.println("arr " + Arrays.toString(arr));
             for(int i = 0; i < arr.length; i++){
                for(int j = 0; j < dghTrees.size(); j++){
                    if(dghTrees.get(j).getLabel().equals(
                            arr[i].substring(0, arr[i].indexOf(" ")))){
                        //System.out.println("here! ");
                        index = j;
                    }
                }    
                    //generalize using this tree dghTree j at level rest of arr[i]
                int x = 0;
                int generalizationLevel = Integer.parseInt(arr[i].substring(arr[i].indexOf(" ")+ 1));
                //System.out.println("genL " + generalizationLevel);
                while(x < generalizationLevel){
                //System.out.println("index- " + index);
                newTable = dataFly.generateTableWithDGHTable(newTable, dghTrees.get(index), index);
                x++;
                }
            
            }
            //maybe later, i will have it return the new table
            newTable.printFormat();
         return dataFly.checkTable(kAnon, newTable);
         }
         
        /* public void markAllDirectGeneralizations(Vertex v){
             v.setMark(true);
         for(int i =0; i < v.getNumOfDirectGen(); i++){
             markAllDirectGeneralizations(v.getDirectGeneralizations().get(i));
         }  
        }*/
         
         public void markAllDirectGeneralizations(Vertex v){
             addListOfGeneralizations(v);
         for(int i =0; i < v.getNumOfIncidentEdges(); i++){
             markAllDirectGeneralizations(v.getIncidentEdges().get(i).getTo());
         }  
        } 
         
         public void addListOfGeneralizations(Graph graph){
             for(int i = 0; i < graph.vertices.size(); i++){
                 if(graph.vertices.get(i).isMarked()){
                     listOfkAnon.add(graph.vertices.get(i).getData());
                 }
             }
         }
         
          public void addListOfGeneralizations(Vertex node){
            node.setMark(true);
            if(listOfkAnon.contains(node.getData()) == false){
                listOfkAnon.add(node.getData());
            }  
         }
         
    
}       


