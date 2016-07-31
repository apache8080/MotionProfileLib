package trajectory;

public class Segment {
  double velocity;
  double acceleration;
  double position;

  public Segment(double velocity, double acceleration, double position) {
    this.velocity = velocity;
    this.acceleration = acceleration;
    this.position = position;
  }

  public Segment(String velocity, String acceleration, String position) {
    this.velocity = Double.parseDouble(velocity);
    this.acceleration = Double.parseDouble(acceleration);
    this.position = Double.parseDouble(position);
  }

  public double getVelocity() {
    return this.velocity;
  }

  public double getAcceleration() {
    return this.acceleration;
  }

  public double getPosition() {
    return this.position;
  }

  public String toString() {
    return velocity + " " + acceleration + " " + position;
  }
}
