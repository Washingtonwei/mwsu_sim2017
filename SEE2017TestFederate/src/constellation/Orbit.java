package constellation;

public class Orbit {

	private final double G = 6.67E-11; // gravitational constant
	private final double M = 7.34767309E22; // mass of moon
	private final double MU = 4.9048695E12; // gravitational parameter (this is km^3 / day^2 right now)

	private double time = 0; // epoch (variable will be used for last
										// known time)
	private double meanAnomaly = 326.2322; // degrees - mean anomaly at epoch (will be used for last known mean anomaly)
	private double meanMotion = 24; // mean motion (orbits per day right now)
	private double eccentricAnomaly; // radians - eccentric anomaly
	private double perigeeDistance; // perigee distance
	private double radius; // the radius from the center of the earth

	// orbital elements
	private double semiMajorAxis; // semi-major axis
	private double eccentricity = 0.7;// 0.0001492; //eccentricity
	private double inclination = 051.9970; // inclination
	private double argOfPerigee = 33.8641; // argument of perigee
	private double RAAN = 251.0219; // R.A. of the ascending node
	private double trueAnomaly; // degrees - true anomaly

	public Orbit(double semiMajorAxis, double eccentricity, double inclination, double argOfPerigree, double RAAN, double trueAnomaly) {
		this.semiMajorAxis = semiMajorAxis;
		this.eccentricity = eccentricity;
		this.inclination = inclination;
		this.argOfPerigee = argOfPerigee;
		this.RAAN = RAAN;
		this.trueAnomaly = trueAnomaly;
	}

	public void Propagate(double dt) {
		// double dt = t - t_0;
		double nDtdiff = meanMotion * dt - (int) (meanMotion * dt);
		double M_t = meanAnomaly + 360 * (nDtdiff - (int) (meanAnomaly + 360 * (nDtdiff)) / 360);
		time += dt;
		meanAnomaly = M_t;

		// calculate the other values
		EccentricAnomaly();
		TrueAnomaly();
		SemiMajor();
		Perigee();
		Radius();
	}

	public void EccentricAnomaly() {
		double M_t = meanAnomaly * (Math.PI / 180);
		eccentricAnomaly = 2 * Math.PI;
		double E_1 = 0.0, diff = 2 * Math.PI;
		double epsilon = 1.0E-5;

		for (int i = 0; i < 30 && diff > epsilon; i++) {
			E_1 = eccentricAnomaly - (eccentricAnomaly - eccentricity * Math.sin(eccentricAnomaly) - M_t) / (1 - eccentricity * Math.cos(eccentricAnomaly));
			diff = Math.abs(E_1 - eccentricAnomaly);
			eccentricAnomaly = E_1;
		}
		E_1 = E_1 * (180 / Math.PI);
		eccentricAnomaly = E_1;
	}

	public void TrueAnomaly() {
		trueAnomaly = Math.toDegrees(Math.acos((Math.cos(Math.toRadians(eccentricAnomaly)) - eccentricity) / (1 - eccentricity * Math.cos(Math.toRadians(eccentricAnomaly)))));
	}

	public void SemiMajor() {
		semiMajorAxis = Math.pow(MU / Math.pow(2 * Math.PI * meanMotion, 2), 1.0 / 3.0);
	}

	public void Perigee() {
		perigeeDistance = semiMajorAxis * (1 - eccentricity);
	}

	public void Radius() {
		radius = (perigeeDistance * (1 + eccentricity)) / (1 + eccentricity * Math.pow(Math.cos(time), meanMotion));
	}

	public double getTrueAnomaly() {
		return trueAnomaly;
	}

	public void setTrueAnomaly(double tE_0) {
		trueAnomaly = tE_0;
	}

	public double getRadius() {
		return radius;
	}

	public void setRadius(double r) {
		this.radius = r;
	}

}