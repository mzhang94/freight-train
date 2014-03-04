package train;

import java.util.ArrayList;

import Jama.Matrix;
import filter.KalmanFilter;

public class Computation {
    Model model;
    KalmanFilter filterX;
    private KalmanFilter filterY;
    int falseCount = 0; //record the number of invalid data that has been received. If percentage is too high, should reset model
    int count = 0;      //number of data received since last reset
    
    public Computation(){
        model = new Model();
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
        Matrix stateX = filterX.getState();
        Matrix stateY = filterY.getState();
        Matrix errX = filterX.getErr();
        Matrix errY = filterY.getErr();
        
        //filter out illegal data which deviates far from prediction
        if(Math.pow(stateX.get(0,0)-data.x,2) > 9*errX.get(0,0) || Math.pow(stateY.get(0,0)-data.y,2) > 9*errY.get(0,0)){           
            model.addRawData(new Data(data.x, data.y, data.time, false));
            model.addEstimate(new Data(filterX.getState().get(0,0), filterY.getState().get(0,0), data.time, false));
            model.addEstimateVel(new Data(filterX.getState().get(1,0), filterY.getState().get(1,0), data.time, false));
            return;
        }
        
        //update
        filterX.update(new Matrix(1, 1, data.x));
        filterY.update(new Matrix(1, 1, data.y));
        
        //update transitional model
        if(model.rawData.size() >= 1){
            filterX.setTran(new Matrix(new double[][]{
                    new double[]{1, data.time - model.rawData.get(model.rawData.size()-1).time}, //x' = x + dt * v
                    new double[]{0, 1}//v' = v
            }));
            filterY.setTran(new Matrix(new double[][]{
                    new double[]{1, data.time - model.rawData.get(model.rawData.size()-1).time}, //x' = x + dt * v
                    new double[]{0, 1}//v' = v
            }));
        }
        
        
        //store estimate information
        model.addEstimate(new Data(filterX.getState().get(0,0), filterY.getState().get(0,0), data.time, true));
        model.addEstimateVel(new Data(filterX.getState().get(1,0), filterY.getState().get(1,0), data.time, true));
          
        //predict
        filterX.predict(new Matrix(2,1));
        filterY.predict(new Matrix(2,1));        
        
        model.addRawData(data);
    }
    
    
}
