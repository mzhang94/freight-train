package train;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Parser {
    public static JSONArray parse(String filename) throws FileNotFoundException, IOException, org.json.simple.parser.ParseException{
        JSONParser parser = new JSONParser();
        Object obj;           
        obj = parser.parse(new FileReader(filename));
        JSONObject jsonObject = (JSONObject) obj;                           
        JSONArray reports = (JSONArray) jsonObject.get("reports");
        return reports;
    }   
    
//    public static Double[] getTime(JSONArray reports){
//        Double[] time = new Double[reports.size()];
//        for(int i=0; i<reports.size(); i++){
//            JSONObject obj = (JSONObject) reports.get(i);
//            time[i] = Double.parseDouble((String)obj.get("timestamp"));
//        }
//        return time;
//    }
//    
//    public static Double[] getX(JSONArray reports){
//        Double[] x = new Double[reports.size()];
//        for(int i=0; i<reports.size(); i++){
//            JSONObject obj = (JSONObject) reports.get(i);
//            x[i] = Double.parseDouble((String)obj.get("x"));
//        }
//        return x;
//    }
//   
//    public static Double[] getY(JSONArray reports){
//        Double[] y = new Double[reports.size()];
//        for(int i=0; i<reports.size(); i++){
//            JSONObject obj = (JSONObject) reports.get(i);
//            y[i] = Double.parseDouble((String)obj.get("y"));
//        }
//        return y;
//    }
      
    public static Double[][] getData(JSONArray reports){
        JSONObject[] jsonArray = new JSONObject[reports.size()];
        for(int i=0; i<reports.size(); i++){
            jsonArray[i] = (JSONObject) reports.get(i);            
        }
        Arrays.sort(jsonArray, new TimeComparator());
        Double[][] data = new Double[3][reports.size()];
        //sort data by time
        for(int i=0; i<reports.size(); i++){
            JSONObject obj = jsonArray[i];
            data[0][i] = (Double) obj.get("timestamp");
            data[1][i] = (Double) obj.get("x");
            data[2][i] = (Double) obj.get("y");
        }              
        return data;
    }      

    public static Double[][] getData(String filename) throws FileNotFoundException, IOException, org.json.simple.parser.ParseException{
        JSONArray reports = parse(filename);
        return getData(reports);
    }
    public static Double[][] getVelocity(Double[][]data){
        Double[][] velocity = new Double[3][data[0].length-1];
        for(int i=0; i< data[0].length-1; i++){
            velocity[0][i] = data[0][i];
            velocity[1][i] = (data[1][i+1] - data[1][i])/(data[0][i+1] - data[0][i]);
            velocity[2][i] = (data[2][i+1] - data[2][i])/(data[0][i+1] - data[0][i]);
        }
        return velocity;
    }
    
   
}

class TimeComparator implements Comparator<JSONObject>{
    public TimeComparator(){
        
    }
    @Override
    public int compare(JSONObject arg0, JSONObject arg1) {
        double time0 = (double) arg0.get("timestamp");
        double time1 = (double) arg1.get("timestamp");
        if(time0==time1){
            return 0;
        }
        else{
            return time0>time1?1:-1;
        }
    }
    
}
