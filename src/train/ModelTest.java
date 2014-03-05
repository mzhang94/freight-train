package train;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import org.json.simple.parser.ParseException;
import org.junit.Test;

public class ModelTest {

    @Test 
    public void testSearch() throws FileNotFoundException, IOException, ParseException{
        Model model= new Model();
        Random rand = new Random();
        double[] timeList = new double[100];
        double time = 0;
        for(int i=0; i<100; i++){
            model.addRawData(new Data(0,0,time, true));
            model.addEstimate(new Data(0,0,time, true));
            model.addEstimateVel(new Data(0,0,time, true));
            timeList[i] = time;
            time += rand.nextDouble();            
        }
        
        //test on searching elements in the  list
        for(int i=0; i< model.getDataNum(); i++){
            assertEquals(model.binarySearch(timeList[i]), i);
        }
        
        //test on searching elements not in the list
        for(int i=0 ; i< model.getDataNum() -1 ; i++){
            assertEquals(model.binarySearch((timeList[i] + timeList[i+1])/2), i);
        }
        
        //test on searching elements smaller or larger than everything
        assertEquals(model.binarySearch(timeList[0]-1), -1);
        assertEquals(model.binarySearch(timeList[99]+1), 99);
    }

}
