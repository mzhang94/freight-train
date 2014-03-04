package train;

import java.util.ArrayList;

import Jama.Matrix;

//some tool functions
public class Common {
    
    public static ArrayList<Matrix> copyArrayMatrix(ArrayList<Matrix> a){
        ArrayList<Matrix> b = new ArrayList<Matrix>();
        for(Matrix m: a){
            b.add(m.copy());
        }
        return b;
    }
    
    //helper function to remove all elements after index, including index
    public static void removeAllAfter(ArrayList<Matrix> array, int index){
        for(int i = array.size() - 1; i >= index; i--){
            array.remove(index);
        }
    }
    public static ArrayList<Data> copyArrayList(ArrayList<Data> a){
        ArrayList<Data> b = new ArrayList<Data>();
        for(Data data : a){
            b.add(data.clone());
        }
        return b;
    }
            
    
}
