package train;

import java.util.ArrayList;

public class Model {
    private ArrayList<Data> rawData;
    private ArrayList<Data> estimate;
    private ArrayList<Data> estimateVel;
    
      
    public Model(){
        rawData = new ArrayList<Data>();          
        estimate = new ArrayList<Data>();    
        estimateVel = new ArrayList<Data>();
    }
       
    /* modify */
    public void addRawData(ArrayList<Data> dataList){
        for(Data d: dataList){
            rawData.add(d);
        }
    }
    
    public void addEstimate(ArrayList<Data> dataList){
        for(Data d: dataList){
            estimate.add(d);
        }
    }
    
    public void addVelEstimate(ArrayList<Data> dataList){
        for(Data d: dataList){
            estimateVel.add(d);
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
    
    /* read */
    public int getDataNum(){
        return rawData.size();
    }
    
    public Data getRawData(int i){
        return rawData.get(i);
    }
    
    public Data getEstimate(int i){
        return estimate.get(i);
    }
    
    public Data getVelEstimate(int i){
        return estimateVel.get(i);
    }
    
    public ArrayList<Data> getRawData(){
        return Common.copyArrayList(rawData);
    }
    
    public ArrayList<Data> getEstimate(){
        return Common.copyArrayList(estimate);
    }
    
    public ArrayList<Data> getVelEstimate(){
        return Common.copyArrayList(estimateVel);
    }
    
    /* search */
    public int binarySearch(double time){
        return binarySearch(time, 0, rawData.size()-1);
    }
    
    public int binarySearch(double time, int start, int end){
       if(rawData.get(start).time >time){
           return start -1;
       }
       if(rawData.get(end).time <= time){
           return end;
       }
       int mid = (start + end)/2;
       if(rawData.get(mid).time == time){
           return mid;
       }
       else if(rawData.get(mid).time > time){
           return binarySearch(time, start, mid);
       }
       else{
           return binarySearch(time, mid+1, end);
       }        
    }
    
    /* toString */
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
          
    public String estimateToString(){
        StringBuilder s = new StringBuilder();
        for(Data d: estimate){
            s.append(d);
            s.append("\n");
        }
        return s.toString();
    }
    
}
