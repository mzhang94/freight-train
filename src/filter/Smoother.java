package filter;

public class Smoother {
    
    public static Double[][] expoSmooth2(Double[] data, Double[] time, double a, double b){
        Double[] valueEstimate = new Double[data.length];
        Double[] trendEstimate = new Double[data.length];
        trendEstimate[0] = 0.;
        trendEstimate[1] = trendEstimate[0];
        valueEstimate[0] = data[0];
        valueEstimate[1] = data[1];
        
        for (int i=1;i<10; i++){
            trendEstimate[i] = b*(data[i] - data[0])/(time[i]-time[0]) + (1-b)*trendEstimate[0]; 
            valueEstimate[i] = a*data[i] + (1-a)*(data[i-1]); 
        }
        for(int i=10; i< data.length; i++){          
            valueEstimate[i] = a*data[i] + (1-a)*(data[i-1] + trendEstimate[i-1] * (time[i]-time[i-1]));            
            trendEstimate[i] = b*(valueEstimate[i] - valueEstimate[i-10])/(time[i]-time[i-10]) + (1-b)*trendEstimate[i-10];                
            if(trendEstimate[i] > 10 || trendEstimate[i] < -10){
                trendEstimate[i] = trendEstimate[i-1];
            }
        }
        
        Double[][] result = new Double[2][data.length];
        result[0] = valueEstimate;
        result[1] = trendEstimate;
        return result;
    }
    
    
    public static Double[][] smooth(Double[][] data, double a, double b){
        Double[][] smoothedValue = new Double[3][data[0].length];
                
        smoothedValue[0][0] = data[0][0];
        smoothedValue[1][0] = data[1][0];
        smoothedValue[2][0] = data[2][0];
        smoothedValue[0][1] = data[0][1];
        smoothedValue[1][1] = data[1][1];
        smoothedValue[2][1] = data[2][1];
        
        double xTrendEstimate = data[1][1] - data[1][0];
        double yTrendEstimate = data[1][1] - data[1][0];
        
        for(int i=2; i< data[0].length; i++){
            smoothedValue[0][i] = data[0][i];
            smoothedValue[1][i] = a*data[1][i] + (1-a)*(data[1][i-1] + xTrendEstimate);
            smoothedValue[2][i] = a*data[2][i] + (1-a)*(data[2][i-1] + yTrendEstimate);
            xTrendEstimate = b*(smoothedValue[1][i] - smoothedValue[1][i-1]) + (1-b)*xTrendEstimate;
            yTrendEstimate = b*(smoothedValue[2][i] - smoothedValue[2][i-1]) + (1-b)*yTrendEstimate;
        }
        
        return smoothedValue;
    }
    
}
