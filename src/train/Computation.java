package train;

import java.util.ArrayList;

import Jama.Matrix;
import filter.KalmanFilter;

public class Computation {
    private Model model;
    private KalmanFilter filterX;
    private KalmanFilter filterY;
    private int falseCount = 0; //record the number of invalid data that has been received after start
    private int count = 0;      //number of data received after start
    
    private ArrayList<Data> rawDataCache = new ArrayList<Data>();
    private ArrayList<Data> estimateCache = new ArrayList<Data>();
    private ArrayList<Data> velEstimateCache = new ArrayList<Data>();
    private Matrix resetStateX;
    private Matrix resetErrX;
    private Matrix resetStateY;
    private Matrix resetErrY;
    
    public Computation(Model model){
        this.model = model;
        filterX = new KalmanFilter(
                new Matrix(new double[][]{new double[]{1, 0}, new double[]{0, 1}}), //transition
                new Matrix(2,2), //control
                new Matrix(2,2),//process noise
                new Matrix(new double[][]{new double[]{1, 0}}),//observation matrix obs(x) = x + noise
                Matrix.identity(1,1).times(400), //observation noise 
                new Matrix(2,1),//initial estimate
                Matrix.identity(2,2).times(10000000)//initial estimate diviation
                );
        filterY = new KalmanFilter(
                new Matrix(new double[][]{new double[]{1, 0}, new double[]{0, 1}}), //transition
                new Matrix(2,2), //control
                new Matrix(2,2),//process noise
                new Matrix(new double[][]{new double[]{1, 0}}),//observation matrix obs(x) = x + noise
                Matrix.identity(1,1).times(400), //observation noise 
                new Matrix(2,1),//initial estimate
                Matrix.identity(2,2).times(10000000)//initial estimate diviation
                );
    }
   
    /**
     * Feed new data to kalman filter. Will filter out invalid data according to current prediction. 
     * Invalid data will still be stored in filter and in data in case that later 
     * we find velocity have been changed and want to reset model.
     * @param data
     */
    public void addData(Data data){
                
        boolean valid = addDataNoUpdateRawData(data);
        rawDataCache.add(new Data(data.x, data.y, data.time, valid));
        
        System.out.println(count + " " + falseCount);
        //check if too many errors
        if(falseCount > count * 0.2 && count > 20){
            
            //reset estimation coviriance for speed.
            filterX.reset(resetStateX, new Matrix(new double[][]{
                    new double[]{resetErrX.get(0,0),0},
                    new double[]{0, 100},
            }));
                       
            filterY.reset(resetStateY, new Matrix(new double[][]{
                    new double[]{resetErrY.get(0,0),0},
                    new double[]{0, 100},
            }));
                       
            estimateCache = new ArrayList<Data>();
            velEstimateCache = new ArrayList<Data>();
            count = 0;
            falseCount = 0;
            
            for(Data d: rawDataCache){
                addDataNoUpdateRawData(d);
            }
        }
        
        //count reaches 100, push estimation 
        if(count >= 100){
            model.addRawData(rawDataCache);
            model.addEstimate(estimateCache);
            model.addVelEstimate(velEstimateCache);
            
            rawDataCache = new ArrayList<Data>();
            estimateCache = new ArrayList<Data>();
            velEstimateCache = new ArrayList<Data>();
            
            count = 0;
            falseCount = 0;
            resetStateX = filterX.getState();
            resetStateY = filterY.getState();
            resetErrX = filterX.getErr();
            resetErrY = filterY.getErr();
        }
    }
    
    /**doing the same thing as addData without adding raw data into local raw data cache
     * also no reset here since there is low chance that we reset twice in 100 data samples
     * also no push here since we will not reach the push threshold during recalculation
     * used for recaculation after reset
     * @param data
     * @return true is the data is valid, otherwise false
     */
    
    private boolean addDataNoUpdateRawData(Data data){      
       
        //filter out illegal data which deviates far from prediction
        count++;
        
        Matrix stateX = filterX.getState();
        Matrix stateY = filterY.getState();
        Matrix coResX = filterX.getCoRes();
        Matrix coResY = filterY.getCoRes();
               
        if(count >= 2 && (Math.pow(stateX.get(0,0)-data.x,2) > coResX.get(0,0) || 
                        Math.pow(stateY.get(0,0)-data.y,2) > coResY.get(0,0))){           
            estimateCache.add(new Data(filterX.getState().get(0,0), filterY.getState().get(0,0), data.time, false));
            velEstimateCache.add(new Data(filterX.getState().get(1,0), filterY.getState().get(1,0), data.time, false));
            falseCount++;
            return false;
        }
        
        //update
        filterX.update(new Matrix(1, 1, data.x));
        filterY.update(new Matrix(1, 1, data.y));
        
        //update transitional model
        if(count >= 2){
            filterX.setTran(new Matrix(new double[][]{
                    new double[]{1, data.time - rawDataCache.get(count-2).time}, //x' = x + dt * v
                    new double[]{0, 1}//v' = v
            }));
            filterY.setTran(new Matrix(new double[][]{
                    new double[]{1, data.time - rawDataCache.get(count-2).time}, //x' = x + dt * v
                    new double[]{0, 1}//v' = v
            }));
        }
             
        //update local estimation cache
        estimateCache.add(new Data(filterX.getState().get(0,0), filterY.getState().get(0,0), data.time, true));
        velEstimateCache.add(new Data(filterX.getState().get(1,0), filterY.getState().get(1,0), data.time, true));
        
        //predict
        filterX.predict(new Matrix(2,1));
        filterY.predict(new Matrix(2,1));        
               
        return true;
    }
    
}
