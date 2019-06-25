public class PIDController {
    private double kp;
    private double kd;
    private double finalXValue=0;
    private double error;
    private double previousError=0;
    public PIDController(double kp, double kd)
    {
        this.kp=kp;
        this.kd=kd;
    }
    public double executePID(double currentXValue)
    {
        double outputValue=0;
        error = finalXValue - currentXValue;
        //where 0.1 is the timestep
        outputValue = kp * error + kd * ((error - previousError) / 0.1);
        previousError = error;
        return outputValue;
    }
    public void setFinalXValue(double newFinalXValue)
    {
        this.finalXValue=newFinalXValue;
    }
}
