package train;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.parser.ParseException;
import org.junit.Test;

public class TrainTest {
    @Test 
    public void testSearch() throws FileNotFoundException, IOException, ParseException{
        Train train = new Train("data/hard-reports-1.txt");
        ArrayList<Data> playBack = train.playBack();
        
        //test on searching elements in the  list
        for(int i=0; i< playBack.size(); i++){
            assertEquals(Train.binarySearch(playBack, playBack.get(i).time), i);
        }
        
        //test on searching elements not in the list
        for(int i=0 ; i< playBack.size()-1; i++){
            assertEquals(Train.binarySearch(playBack, (playBack.get(i).time + playBack.get(i+1).time)/2), i);
        }
        
        //test on searching elements smaller or larger than everything
        assertEquals(Train.binarySearch(playBack, playBack.get(0).time -1), -1);
        assertEquals(Train.binarySearch(playBack, playBack.get(playBack.size()-1).time + 1), playBack.size()-1);
    }
    
    @Test
    public void testPlayBackAndQuery() throws FileNotFoundException, IOException, ParseException {
        Train train = new Train("data/hard-reports-1.txt");
        ArrayList<Data> playBack = train.playBack();
        
        //test for query with time existed in data
        for(int i= 0; i < playBack.size(); i++){
            Data playBackEstimate = playBack.get(i);
            double[] estimate = train.query(playBackEstimate.time);
            assertEquals(estimate[0], playBackEstimate.x, 0.001);
            assertEquals(estimate[1], playBackEstimate.y, 0.001);
        }
    }
    
    @Test
    //not automated tests. print out result to see if it is reasonable.
    public void testSpeedAndErrs() throws FileNotFoundException, IOException, ParseException{
        Train train = new Train("data/hard-reports-1.txt");
        ArrayList<Data> playBack = train.playBack();
        
        System.out.println(train.getSpeed());
        System.out.println(train.getErr());
        System.out.println(train.getSpeedVar());
    }

}
