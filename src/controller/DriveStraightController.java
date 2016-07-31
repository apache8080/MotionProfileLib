package controller;

import java.util.TimerTask;
import trajectory.TrajectoryFollower;
import trajectory.Segment;

public class DriveStraightController extends TimerTask {

  TrajectoryFollower rightSide;
  TrajectoryFollower leftSide;
  double leftCurrPosition = 0.0;
  double rightCurrPosition = 0.0;

  public DriveStraightController(
      Segment[] leftTrajectory,
      Segment[] rightTrajectory,
      double leftMaxV,
      double rightMaxV,
      double controlLoop) {
    
    double leftkV = 1 / leftMaxV;
    double rightkV = 1 / rightMaxV;
    double dt = 1 / controlLoop;
    //Will later set these values as constants in a constants file
    this.leftSide = new TrajectoryFollower(leftTrajectory, leftkV, 0.0, 0.0, 0.0, 0.0, dt);
    this.rightSide = new TrajectoryFollower(rightTrajectory, rightkV, 0.0, 0.0, 0.0, 0.0, dt);
  }

  public void setEncoderValue(double leftEncoderVal, double rightEncoderVal) {
    //Later on we need to multiply the encoder value by a ticks per feet/inch value to get the actual position
    this.leftCurrPosition = leftEncoderVal;
    this.rightCurrPosition = rightEncoderVal;
  }

  public void run() {
    if (!rightSide.isTrajDone() && !leftSide.isTrajDone()) {
      System.out.println(
          "Right Side Motor Val: "
              + this.rightSide.calcMotorOutput(this.rightCurrPosition)
              + "----- Left Side Motor Val: "
              + this.leftSide.calcMotorOutput(this.leftCurrPosition)
              + "\n");
    } else {
      System.exit(0);
    }
  }
}
