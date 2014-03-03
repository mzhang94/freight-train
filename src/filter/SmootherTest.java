package filter;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

public class SmootherTest {

    @Test
    public void testExpoSmooth2() {
        Random rand = new Random();
        Double[] time = new Double[100];
        time[0]=0.;
        for(int i =1; i<100;i++){
            time[i] = time[i-1] + rand.nextDouble();
        }
        
        Double[] constant = new Double[100];
        for(int i=0; i< 100; i++){
            constant[i] = 10.;
        }
        
        Double[] arithmic = new Double[100];
        for(int i=0; i<100;i++){
            arithmic[i] = 0.5*time[i]+1.8;
        }
        
        Assert.assertTrue(arrayEquals(constant, Smoother.expoSmooth2(constant, time, 0.5, 0.5)[0]));
        Assert.assertTrue(arrayEquals(constant, Smoother.expoSmooth2(constant, time, 0.8,0.2)[0]));
        Assert.assertTrue(arrayEquals(constant, Smoother.expoSmooth2(constant, time, 1.,0.)[0]));
        Assert.assertTrue(arrayEquals(constant, Smoother.expoSmooth2(constant, time, 1.,1.)[0]));
        Assert.assertTrue(arrayEquals(constant, Smoother.expoSmooth2(constant, time, 0.,1.)[0]));
        Assert.assertTrue(arrayEquals(constant, Smoother.expoSmooth2(constant, time, 0.,0.)[0]));
        
        Assert.assertTrue(arrayEquals(arithmic, Smoother.expoSmooth2(arithmic, time, 0.5, 0.5)[0]));
        Assert.assertTrue(arrayEquals(arithmic, Smoother.expoSmooth2(arithmic, time, 0.8,0.2)[0]));
        Assert.assertTrue(arrayEquals(arithmic, Smoother.expoSmooth2(arithmic, time, 1.,0.)[0]));
        Assert.assertTrue(arrayEquals(arithmic, Smoother.expoSmooth2(arithmic, time, 1.,1.)[0]));
        Assert.assertTrue(arrayEquals(arithmic, Smoother.expoSmooth2(arithmic, time, 0.,1.)[0]));
        Assert.assertTrue(arrayEquals(arithmic, Smoother.expoSmooth2(arithmic, time, 0.,0.)[0]));
        
        Double[] random = new Double[100];
        for(int i=0; i<100;i++){
            random[i] = rand.nextDouble();
        }
        Assert.assertTrue(arrayEquals(random, Smoother.expoSmooth2(random, time, 1.,0.5)[0]));
    }

    public boolean arrayEquals(Double[] a, Double[] b){
        double squareDistance = 0;
        if(a.length != b.length){
            return false;
        }
        else{
            for(int i=0; i<a.length; i++){
                squareDistance += (a[i]-b[i])*(a[i]-b[i]);               
            }
            if (squareDistance/a.length > 0.5){
                return false;
            }
            else
                return true;
        }
        
    }
}
