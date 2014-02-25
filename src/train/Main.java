package train;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import org.json.simple.parser.ParseException;

public class Main {
    public static void main(String[] args){
        try {
            Double[][] data = Parser.getData("freight-train/reports.txt");
            Double[][] velocity = Parser.getVelocity(data);
            Double[][] sData = Parser.smoothedData(data, 0.5,0.5);
            
            PrintWriter dataWriter = new PrintWriter("report-data.txt", "UTF-8");           
            for(int i=0;i<data[0].length; i++){
                dataWriter.println(String.format("%f %f %f", data[0][i], data[1][i], data[2][i]));
            }
            dataWriter.close();
            
            PrintWriter sDataWriter = new PrintWriter("report-sData.txt", "UTF-8");
            for(int i=0;i<velocity[0].length; i++){
                sDataWriter.println(String.format("%f %f %f", sData[0][i], sData[1][i], sData[2][i]));
            }
            sDataWriter.close();
            
           
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
}
