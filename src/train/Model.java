package train;

import java.util.ArrayList;

public class Model {
    private ArrayList<Data> rawData;
    private ArrayList<Data> estimate;
    private ArrayList<Data> estimateVel;
    
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
        return Common.copyArrayList(estimate);
    }
    
    public double getSpeed(){
        return speed;
    }
    
    public void addRawData(ArrayList<Data> dataList){
        for(Data d: dataList){
            rawData.add(d.clone());
        }
    }
    
    public void addEstimate(ArrayList<Data> dataList){
        for(Data d: dataList){
            estimate.add(d.clone());
        }
    }
    
    public void addVelEstimate(ArrayList<Data> dataList){
        for(Data d: dataList){
            estimateVel.add(d.clone());
        }
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
    
    public String estimateVelToString(){
        StringBuilder s = new StringBuilder();
        for(Data d: estimateVel){
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
               
}
