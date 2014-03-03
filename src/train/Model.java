package train;

import java.util.ArrayList;

public class Model {
    ArrayList<Data> rawData;
    ArrayList<Data> estimate;
    ArrayList<Data> estimateVel;
    
    private double speed;
   
    
    public Model(){
        rawData = new ArrayList<Data>();          
        estimate = new ArrayList<Data>();    
        estimateVel = new ArrayList<Data>();
    }
    
    public double query(double time){
        return time;       
    }
    
    public ArrayList<Data> playBack(){
        return copyArrayList(estimate);
    }
    
    public double getSpeed(){
        return speed;
    }
    
    public void addRawData(Data d){
        rawData.add(d.clone());
    }
    
    public void addEstimate(Data d){
        estimate.add(d.clone());
    }
    
    public void addEstimateVel(Data vel){
        estimateVel.add(vel);
    }
    public void setSpeed(double s){
        speed = s;
    }
     
    public String estimateToString(){
        StringBuilder s = new StringBuilder();
        for(Data d: estimate){
            s.append(d);
            s.append("\n");
        }
        return s.toString();
    }
    
    public String rawDataToString(){
        StringBuilder s = new StringBuilder();
        for(Data d: rawData){
            s.append(d);
            s.append("\n");
        }
        return s.toString();
    }
    
    private ArrayList<Data> copyArrayList(ArrayList<Data> a){
        ArrayList<Data> b = new ArrayList<Data>();
        for(Data data : a){
            b.add(data.clone());
        }
        return b;
    }
            
            
}
