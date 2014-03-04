package train;

import java.util.ArrayList;

public class Model {
    protected ArrayList<Data> rawData;
    protected ArrayList<Data> estimate;
    protected ArrayList<Data> estimateVel;
    
    protected double speed;
      
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
