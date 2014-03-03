package train;

import java.util.ArrayList;

import Jama.Matrix;
import filter.KalmanFilter;

public class Computation {
    Model model;
    KalmanFilter filterX;
    private KalmanFilter filterY;
    
    public Computation(){
        model = new Model();
        filterX = new KalmanFilter(
                new Matrix(new double[][]{new double[]{1, 0}, new double[]{0, 1}}), //transition
                new Matrix(2,2), //control
                new Matrix(2,2),//process noise
                new Matrix(new double[][]{new double[]{1, 0}}),//observation matrix obs(x) = x + noise
                Matrix.identity(1,1).times(5), //observation noise 
                new Matrix(2,1),//initial estimate
                Matrix.identity(2,2).times(100)//initial estimate diviation
                );
        filterY = new KalmanFilter(
                new Matrix(new double[][]{new double[]{1, 0}, new double[]{0, 1}}), //transition
                new Matrix(2,2), //control
                new Matrix(2,2),//process noise
                new Matrix(new double[][]{new double[]{1, 0}}),//observation matrix obs(x) = x + noise
                Matrix.identity(1,1).times(5), //observation noise 
                new Matrix(2,1),//initial estimate
                Matrix.identity(2,2).times(100)//initial estimate diviation
                );
    }
   
    public void addData(Data data){
        
        filterX.predict(new Matrix(2,1));
        filterY.predict(new Matrix(2,1));        
        
        filterX.update(new Matrix(1, 1, data.x));
        filterY.update(new Matrix(1, 1, data.y));
        
        model.addEstimate(new Data(filterX.getState().get(0,0), filterY.getState().get(0,0), data.time));
        model.addEstimateVel(new Data(filterY.getState().get(1,0), filterY.getState().get(1,0), data.time));
        
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
        
        model.addRawData(data);
    }
    
    
}
