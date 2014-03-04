package train;

import java.util.ArrayList;

public class Model {
    protected ArrayList<Data> rawData;
    protected ArrayList<Data> estimate;
    protected ArrayList<Data> estimateVel;
    
      
    public Model(){
        rawData = new ArrayList<Data>();          
        estimate = new ArrayList<Data>();    
        estimateVel = new ArrayList<Data>();
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
    
    public void addEstimate(Data d){
        estimate.add(d);
    }
    
    public void addRawData(Data d){
        rawData.add(d);
    }
    
    public void addEstimateVel(Data vel){
        estimateVel.add(vel);
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
