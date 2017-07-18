
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
 * @author Dunni 
 */
    public class Incognito {
    
        private ArrayList<String>  quasiCombinationList;// = new ArrayList<>() ;
        private Graph mainGraph;
        private final ArrayList<String> listOfkAnon = new ArrayList<>();
        private LinkedHashMap<String, Graph> rAttributeGen;
        private final DataFly dataFly = new DataFly();
        private PrivateTable table;
        
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
        
        incognito.mainIncognitoAlgorithm(kAnon);
        incognito.displayGeneralizationCombinations();
        System.out.println();
        incognito.showFinalGeneralizedTables();
     }  

        public ArrayList<String> getQuasiCombinationList() {
            return quasiCombinationList;
        }

        public ArrayList<String> getListOfkAnon() {
            return listOfkAnon;
        }

        public LinkedHashMap<String, Graph> getrAttributeGen() {
            return rAttributeGen;
        }
        
        /**
         * The main Incognito Algorithm, goes through every possible 
         * generalization and stores those that are k-anonymous
         * It uses the generalization property
         * @param kAnon - k in k-Anonymous
         * @throws FileNotFoundException 
         */
        public void mainIncognitoAlgorithm(int kAnon) throws FileNotFoundException {
        getQuasiCombinations(table, 1);//should go till n // change to 1
        createGraphsForRattributes();
        ArrayList<Vertex> queue ;
        
        for(int i = 1; i <= table.getQuasiIden().getData().size(); i++){

            for(int x = 0; x < quasiCombinationList.size(); x++){
                Graph tempGraph = rAttributeGen.get(quasiCombinationList.get(x));//change to get x
                //tempGraph.printOut();
                queue = sortByHeight(tempGraph.getRoots());
                
                while(queue.isEmpty() == false){
                    Vertex node = queue.remove(0);
                    boolean issaKnon ;//= false; //checks if it's k-Anonymous
                    
                    if(node.isMarked() == false){
                        issaKnon = generalizeWithLevel(node.getData(), kAnon);
                        System.out.println("node: " + node.getData());
                        System.out.println("Am I k-Anon ? " + issaKnon);
                        System.out.println();
                        if(issaKnon == true){
                            markAllDirectGeneralizations(node);
                        }
                        else{
                            queue = node.getDirectGeneralizations(queue);// add generalizations of current node to queue
                            queue = sortByHeight(queue);//i should be adding to the queue not replacing values
                        }
                    }   
                }
            }
            
            getQuasiCombinations(table, i + 1);
            createGraphsForRattributes();
        }
    }  
        
        /**
         * Displays attribute generalizations that makes table k-anonymous
         */
        public void displayGeneralizationCombinations(){
            System.out.println("Combinations that are k-Anon:" );//+ Arrays.toString(incognito.listOfkAnon.toArray()));
            for(int m = 0; m < listOfkAnon.size(); m++){
                System.out.println(listOfkAnon.get(m));
            }
        }
        
        /**
         * Displays Final Table Generalizations that are k-Anonymous
         * @throws FileNotFoundException 
         */
        public void showFinalGeneralizedTables() throws FileNotFoundException{
            System.out.println("Generalized Tables that are k-Anon:" );
            for(int i = 0; i < listOfkAnon.size(); i++){
                generalizeWithLevel(listOfkAnon.get(i));
            }
        }
        
        /**
         * Evaluates the possible ways the quasi identifiers of a 
         * table can be combined
         * @param table
         * @throws FileNotFoundException 
         */
        public void getQuasiCombinations(PrivateTable table) throws FileNotFoundException{
            
           ArrayList<String> quasiIden = table.getQuasiIden().getData();
           for(int i = 1; i <= quasiIden.size();i++){
             String[] input = new String[quasiIden.size()];
             combination(quasiIden.toArray(input), new String[quasiIden.size()],
                     0, quasiIden.size() - 1, 0, i);  
           }
        }
        
        /**
         * Evaluates the ways r elements can be selected from the quasi 
         * identifiers of a table
         * @param table
         * @param r
         * @throws FileNotFoundException 
         */
        public void getQuasiCombinations(PrivateTable table, int r) throws FileNotFoundException{
           quasiCombinationList = new ArrayList<>() ; 
           ArrayList<String> quasiIden = table.getQuasiIden().getData();
           
             String[] input = new String[quasiIden.size()];
             combination(quasiIden.toArray(input), new String[quasiIden.size()],
                     0, quasiIden.size() - 1, 0, r);  
          
        }
        
        /**
         *@param input[]  ---> Input Array
         *@param temp[] ---> Temporary array to store current combination
         *@param start & 
         *@param end ---> Staring and Ending indexes in input[]
         *@param index  ---> Current index in data[]
         *@param r ---> Size of a combination to be printed
         * As inspired by
         * http://www.geeksforgeeks.org/print-all-possible-combinations-of-r-elements-in-a-given-array-of-size-n/
         * Adds the height of an attribute to its label
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
        /**
         * Compares quasi to all the labels of the table quasi identifiers
         * @param quasi
         * @return String of height if found
         * @throws FileNotFoundException 
         */
        public String getDGHeight(String quasi) throws FileNotFoundException{
            ArrayList<DGHTree> dghTrees = dataFly.createDGHTrees(table);
            for(int i = 0; i < dghTrees.size(); i++){
          
                if(dghTrees.get(i).getLabel().equalsIgnoreCase(quasi)){
                    return String.valueOf(dghTrees.get(i).getHeight() - 1);
                }
            }
            return "";
        }
        
        /**
         * Creates the possible parents of a vertex. i.e if the parents are 
         * generalized, the result is v
         * @param v - vertex
         */
        public void descendFromTopVertex(Vertex v){
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
                //System.out.println("here - " + vertex2.getData());
                mainGraph.addEdge(edge);
                //mainGraph.addVertex(v);
                mainGraph.addVertex(vertex2);
               }  
            }
            
            //mainGraph.printOut();
            
        }
        
        /**
         * Checks to see if a vertex is last possible parent
         * This last possible parent is when all the attributes are NOT
         * generalized
         * @param v - vertex
         * @return 
         */
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
        
        /**
         * Creates a Graph for each possible combination of the quasi Identifiers
         * Continually creates parents for generated vertices until we get to the
         * first parent - all the attributes are NOT
         * generalized
         */
        public void createGraphsForRattributes(){
            rAttributeGen = new LinkedHashMap<>();
            for(int j = 0; j < quasiCombinationList.size(); j++){
            //System.out.println(quasiCombinationList.get(j));
        
       
        Vertex topRoot = new Vertex(quasiCombinationList.get(j));
        mainGraph = new Graph();
        mainGraph.addVertex(topRoot);
        int origLength = mainGraph.getVertices().size();
        descendFromTopVertex(mainGraph.getVertex(0));

        while(checkIfAtBottom(mainGraph.getVertex(origLength)) == false){
        int newLength = mainGraph.getVertices().size();
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
        
        /**
         * Sort a list of vertices by the current heights of the attributes in
         * a specific vertex
         * @param queue
         * @return 
         */
        public ArrayList<Vertex> sortByHeight(ArrayList<Vertex> queue){
        return MergeSort.sort(queue);
        }
        
         /**
          * Generalizes original table by quasi ID and level of quasiS
          * then gets freq set of generated table
          * @param s - quasi ID + level of Generalization
          * @param kAnon - k
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
            //newTable.printFormat();
         return dataFly.checkTable(kAnon, newTable);
         }
         
         /**
          * Generalizes original table by quasi ID and level of quasiS
          * then prints it
          * @param s - quasi ID + level of Generalization
          * @throws java.io.FileNotFoundException
          */
         public void generalizeWithLevel(String s) throws FileNotFoundException{
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
         
         }
         
         /**
          * If a node is k-Anon, mark the node and the nodes it's incident on
          * (uses recursion)
          * @param v - node
          */
         public void markAllDirectGeneralizations(Vertex v){
             addListOfGeneralizations(v);
         for(int i =0; i < v.getNumOfIncidentEdges(); i++){
             markAllDirectGeneralizations(v.getIncidentEdges().get(i).getTo());
         }  
        } 
        
         /**
          * Set the mark on a node to true -means 
          * Then add it to a list of vertices that are not k-Anonymous
          * @param node 
          */
        public void addListOfGeneralizations(Vertex node){
          node.setMark(true);
          if(listOfkAnon.contains(node.getData()) == false){
              listOfkAnon.add(node.getData());
          }  
       }
         
    
}       


