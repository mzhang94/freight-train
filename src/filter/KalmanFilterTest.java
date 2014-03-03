package filter;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;

import Jama.Matrix;

public class KalmanFilterTest {

    @Test
    // simple gaussian noise model
    // one-dimensional state: x' = x
    // observation:  o = x + GaussianNoise with std 1
    public void simpleTest() {       
        KalmanFilter filter = new KalmanFilter(
                Matrix.identity(1,1),
                new Matrix(1,1),
                new Matrix(1,1),
                Matrix.identity(1,1), 
                Matrix.identity(1,1),
                new Matrix(1,1),
                Matrix.identity(1,1).times(10));
        Random rand = new Random();
        double virance = 0;
        for(int i=0;i<1000;i++){
            Matrix state = filter.predict(new Matrix(1,1));
            filter.update(Matrix.identity(1,1).times(1+rand.nextDouble()));
            virance += Math.pow(state.get(0,0)-1.5,2);
        }
        assertTrue(virance/1000<0.01);
    }
    
    //add transitional model
    //state (x,v) x'=x+v, v'=v
    //observation o = x + GaussianNoise with std 1
    @Test
    public void simpleTranTest(){
        KalmanFilter filter = new KalmanFilter(
                new Matrix(new double[][]{new double[]{1,1}, new double[]{0,1}}),
                new Matrix(2,2),
                new Matrix(2,2),
                Matrix.identity(1,2), 
                Matrix.identity(1,1),
                new Matrix(2,1),
                Matrix.identity(2,2).times(10));
        
        Random rand = new Random();
        double virance = 0;
        for(int i=0;i<100;i++){
            Matrix state = filter.predict(new Matrix(2,1));
            filter.update(Matrix.identity(1,1).times(i+rand.nextDouble()));
            virance += Math.pow(state.get(0,0)-i-0.5,2);
        }        
        assertTrue(virance<100*0.05);
    }
}
