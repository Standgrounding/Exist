package lt.standgrounding.exist;
//kampinis greitis

public class AngularVelocity{
	int time;
	double deltaPitch, deltaYaw;
	int timestamp;
	//gavimo proceduros
    public  int getTime(){
		return time;
    }
    public double getPitch(){
    	return deltaPitch;
    }
    public double getYaw(){
    	return deltaYaw;
    }
    //pokycio gavimo proceduros
    public double getPitchChange(){
    	return deltaPitch/time;
    }
    public double getYawChange(){
    	return deltaYaw/time;
    }
    public double getSum(){
		return Math.sqrt(deltaPitch*deltaPitch+deltaYaw*deltaYaw)/time;	
    }
    //pokycio gavimas turint 2 kampus
    public double getDelta(double D1, double D2){
    	//matematinis modulis tarp kampu atimties
    	if(D2 > D1){
		return D2-D1;
    	} else {
    	return D1-D2;
    	}
    }
    //nustatymo proceduros
    public void setDeltaPitch(double P1, double P2){
    	deltaPitch = getDelta(P1, P2);
    }
    public void setDeltaYaw(double Y1, double Y2){
    	deltaYaw = getDelta(Y1, Y2);
    }
    public void recordingTimeframe(int T){
    	 time = T;
    }
    public void stamp(){
    	timestamp = (int) System.currentTimeMillis();
    }
    public int destamp(){
    	return timestamp;
    }
    @Override
    public String toString(){
		return "Judate "+getSum()*time+" laipsniø per milisekundæ greièiu";
    }
}
