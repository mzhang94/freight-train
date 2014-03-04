package filter;

import java.util.ArrayList;

import train.Common;
import Jama.Matrix;

public class KalmanFilter {
    private Matrix control;
    private Matrix tran;
    private Matrix tranT;
    
    private Matrix procNoise;
    private Matrix obsMat;
    private Matrix obsMatT;
    private Matrix obsNoise;
    
    private Matrix errCo;   
    private Matrix coRes;
    private Matrix kalmanGain;
    private Matrix state;
    private Matrix mRes;
    private Matrix input;
    private int d; //dimention of state
    
    private ArrayList<Matrix> prioriState = new ArrayList<Matrix>();
    private ArrayList<Matrix> prioriErr = new ArrayList<Matrix>();
    
    private ArrayList<Matrix> postState = new ArrayList<Matrix>();
    private ArrayList<Matrix> postErr = new ArrayList<Matrix>();
    
    /**
     * Initialize
     * @param tran
     * @param control
     * @param procNoise
     * @param obsMat
     * @param obsNoise
     * @param initState
     * @param initErrCoviriance
     */
    public KalmanFilter(Matrix tran, Matrix control, Matrix procNoise, Matrix obsMat, Matrix obsNoise,
            Matrix initState, Matrix initErrCoviriance){
        this.tran = tran.copy();
        tranT = tran.transpose();
        
        this.control = control.copy();
        this.procNoise = procNoise.copy();
        this.obsMat = obsMat.copy();
        obsMatT = obsMat.transpose();
        this.obsNoise = obsNoise.copy();
        
        //initialize the other fields
        this.d = tran.getRowDimension();
        this.state = initState;
        this.errCo = initErrCoviriance;
        this.input = new Matrix(d, 1);
        
        prioriState.add(initState);
        prioriErr.add(initErrCoviriance);
    }
    
    /**
     * Predict next state given input based on previous observation. Should be called after update
     * @param input
     * @return predict state
     */
    public Matrix predict(Matrix input){
        this.input = input;       
        state = tran.times(state).plus(control.times(input));
        errCo = tran.times(errCo).times(tranT).plus(procNoise); 
        return state.copy();
    }
    
    /**
     * Update internal state given new observation obs
     * @param obs
     */
    public void update(Matrix obs){       
        mRes = obs.minus(obsMat.times(state));
        coRes = obsMat.times(errCo).times(obsMatT).plus(obsNoise);
        kalmanGain = errCo.times(obsMatT).times(coRes.inverse());
        state = state.plus(kalmanGain.times(mRes));
        errCo = Matrix.identity(d, d).minus(kalmanGain.times(obsMat)).times(errCo);
        
        postState.add(state);
        postErr.add(errCo);
    }
    
    /**
     * Reset state, priorState, priorErr, postErr, postState to the point before inputing the ith observation.
     * That is to say, discard anything after the ith observation including ith observation
     * Will not reset othre fields because they can be calculated from state and errCo
     * @param i observation after which all inputs should be discarded
     */
    public void reset(int i){
        Common.removeAllAfter(postState, i);
        Common.removeAllAfter(postErr, i);
        
        Common.removeAllAfter(prioriState, i+1);
        Common.removeAllAfter(prioriErr, i+1);
        
        state = postState.get(postState.size()-1);
        errCo = postState.get(postErr.size()-1);       
    }
    
    //getters and setters
    public Matrix getState(){
        return state.copy();
    }
    
    /**
     * Get the covariance matrix of current state estimation
     * @return
     */
    public Matrix getErr(){
        return errCo.copy();
    }
    
    public ArrayList<Matrix> getPrioriState(){
        return Common.copyArrayMatrix(prioriState);
    }
    
    public ArrayList<Matrix> getPriori(){
        return Common.copyArrayMatrix(prioriState);
    }
    
    public void setTran(Matrix tran){
        this.tran = tran.copy();
        tranT = tran.transpose();
    }
    
    public void setControl(Matrix control){
        this.control = control.copy();
    }
    
    public void setNoise(Matrix procNoise, Matrix obsNoise){
        this.procNoise = procNoise.copy();       
        this.obsNoise = obsNoise.copy();
    }
    
    
}
    