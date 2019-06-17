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
        error = finalXValue - currentXValue;
        double outputValue = kp*error + kd * ((error - previousError)/0.5);
        previousError = error;
        return outputValue;
    }
}
