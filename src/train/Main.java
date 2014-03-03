package train;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import org.json.simple.parser.ParseException;

import filter.Smoother;

public class Main {
    public static void main(String[] args){
        try {
            Double[][] data = Parser.getData("freight-train/hard-reports-2.txt");
//            Double[][] xEstimate = Smoother.expoSmooth2(data[1], data[0], 0.1,0.5);
//            Double[][] yEstimate = Smoother.expoSmooth2(data[2], data[0], 0.1,0.5);
           
//            PrintWriter dataWriter = new PrintWriter("hard-report-2-data.txt", "UTF-8");           
//            for(int i=0;i<data[0].length; i++){
//                dataWriter.println(String.format("%f %f %f", data[0][i], data[1][i], data[2][i]));
//            }         
//            dataWriter.close();
//            
//            PrintWriter sDataWriter = new PrintWriter("hard-report-2-sdata.txt", "UTF-8");           
//            for(int i=0;i<xEstimate[0].length; i++){
//                sDataWriter.println(String.format("%f %f %f", data[0][i], xEstimate[0][i], yEstimate[0][i]));
//            }
//            sDataWriter.close();
//            
//            PrintWriter sVelWriter = new PrintWriter("hard-report-2-sVel.txt", "UTF-8");
//            for(int i=0;i<xEstimate[0].length; i++){
//                sVelWriter.println(String.format("%f %f %f", data[0][i], xEstimate[1][i], yEstimate[1][i]));
//            }
//            sVelWriter.close();
            
            Computation c = new Computation();
            //add data
            for(int i=0; i< data[0].length; i++){
                c.addData(new Data(data[1][i], data[2][i], data[0][i]));
            }
            //output estimate
            PrintWriter dataWriter = new PrintWriter("hard-report-2-data.txt", "UTF-8");           
            dataWriter.print(c.model.rawDataToString());        
            dataWriter.close();

            PrintWriter sDataWriter = new PrintWriter("hard-report-2-sdata.txt", "UTF-8");           
            sDataWriter.print(c.model.estimateToString());
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
