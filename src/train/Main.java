package train;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import org.json.simple.parser.ParseException;

import filter.Smoother;

public class Main {
    public static void main(String[] args){
        try {            
            Train train = new Train("data/reports.txt");
            train.writeToFile();
            
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
