
package Incognito;

import java.util.ArrayList;

/**
 *
 * @author adenugad
 */
/** Inspiration from
 * http://www.vogella.com/tutorials/JavaAlgorithmsMergesort/article.html
 * @author adenugad
 */
public class MergeSort {
    static ArrayList<Vertex>   vertexes  = new ArrayList<>();
    static ArrayList<Vertex> tempVertexes = new ArrayList<>();
    
    public static ArrayList<Vertex> sort(ArrayList<Vertex> vS){
        if(vS.isEmpty()){
            return vS;
        }
        //System.out.println( "list size: "+vS.size());
        vertexes = vS;
        System.out.println( "list size: "+vertexes.size());
        mergesort(0, vertexes.size()-1);
        return vertexes;
    }
    
    public static void mergesort(int low, int high){
        if(low < high){
            int middle = (low + high)/2;
            
            mergesort(low, middle);
            mergesort(middle + 1, high);
            merge(low, middle, high);
            
        }
    }
    
    public static void merge(int low, int middle, int high){
        
        for(int i = 0; i < vertexes.size(); i++){
            tempVertexes.add(i, vertexes.get(i).copy());
        }
        
        int i = low;
        int j = middle + 1;
        int k = low;
        
        while(i <= middle && j <= high){
            if(tempVertexes.get(i).getVertexHeight() <= tempVertexes.get(j).getVertexHeight()){
                vertexes.set(k, tempVertexes.get(i));
                i++;
            }
            else{
                vertexes.set(k, tempVertexes.get(j));
                j++;
            }
            k++;
        }
        
        while(i <= middle){
            vertexes.set(k, tempVertexes.get(i));
            i++;
            k++;
        }
        
    }
    
}
