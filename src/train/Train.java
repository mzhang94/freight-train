package train;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.json.simple.parser.ParseException;

public class Train {
    
    private Computation c;
    private Model model;
    private String filename;
    
    public Train(String filename) throws FileNotFoundException, IOException, ParseException{
        model = new Model();
        c = new Computation4(model);
        this.filename = filename;
        
        Double[][] dataSet = Parser.getData(filename);
        for(int i=0; i< dataSet[0].length; i++){
            c.addData(new Data(dataSet[1][i], dataSet[2][i], dataSet[0][i], true));
        }       
        
    }
    
    /* Interface methods */
    public double[] query(double time){
         int i = binarySearch(model.rawData, time);
         
         Data state = model.estimate.get(i);
         Data vel = model.estimateVel.get(i);
         double x = state.x + vel.x*(time-state.time);
         double y = state.y + vel.y*(time-state.time);
         return new double[]{x,y};
    }

    public ArrayList<Data> playBack(){
        return Common.copyArrayList(model.estimate);
    }
    
    public double getSpeed(){
        Data velocity = model.estimateVel.get(model.estimateVel.size()-1);
        return Math.sqrt(velocity.x*velocity.x + velocity.y*velocity.y);
    }
    
    /* helper methds */
    public static int binarySearch(ArrayList<Data> d, double time){
        return binarySearch(d, time, 0, d.size()-1);
    }
    
    public static int binarySearch(ArrayList<Data> d, double time, int start, int end){
       if(d.get(start).time >time){
           return start -1;
       }
       if(d.get(end).time <= time){
           return end;
       }
       int mid = (start + end)/2;
       if(d.get(mid).time == time){
           return mid;
       }
       else if(d.get(mid).time > time){
           return binarySearch(d, time, start, mid);
       }
       else{
           return binarySearch(d, time, mid+1, end);
       }        
    }
    
    /* output methods for visualization in python */
    public void writeToFile() throws FileNotFoundException, UnsupportedEncodingException{
      //output estimate
        String[] split = filename.split("\\.");
        PrintWriter dataWriter = new PrintWriter(split[0] + "-data." + split[1], "UTF-8");           
        dataWriter.print(model.rawDataToString());        
        dataWriter.close();

        PrintWriter sDataWriter = new PrintWriter(split[0] + "-sdata." + split[1], "UTF-8");           
        sDataWriter.print(model.estimateToString());
        sDataWriter.close();
        
        PrintWriter sVelWriter = new PrintWriter(split[0] + "-sVel." + split[1], "UTF-8");
        sVelWriter.print(model.estimateVelToString());
        sVelWriter.close();
    }
    
    /* analysis methods to know how well our prediction is */
    
    /**
     * Return the variance between prediction and observation over all data
     * @return
     */
    public double getErr(){
        double var = 0;
        for(int i=0; i < model.estimate.size();i++){
            var += Math.pow(model.estimate.get(i).x - model.rawData.get(i).x, 2);
            var += Math.pow(model.estimate.get(i).y - model.rawData.get(i).y, 2);
        }
        return var/model.estimate.size();
    }
    
    /**
     * Return the variation of speed over all estimation
     * @return
     */
    public double getSpeedVar(){
        double var = 0;
        double sum = 0;
        double[] vel = new double[model.estimateVel.size()];
        for(int i=0; i < model.estimateVel.size();i++){
            vel[i] = Math.sqrt(Math.pow(model.estimateVel.get(i).x, 2) + Math.pow(model.estimateVel.get(i).y, 2));
            sum += vel[i];
        }
        double mean = sum/model.estimateVel.size();
        for(int i=0; i < model.estimateVel.size();i++){
            var += Math.pow(vel[i]-mean, 2);
        }
        return var/model.estimateVel.size();
    }
}
