package satellite;

public class Orbit {

	private final double G = 6.67E-11; // gravitational constant
	private final double M = 5.972E24; // mass of earth
	private final double MU = 2.97554E15; // gravitational parameter (this is
											// km^3 / day^2 right now)

	// orbital elements
	private double t_0 = 7350.24607837; // epoch (variable will be used for last
										// known time)
	private double i = 051.9970; // inclination
	private double a_w0 = 251.0219; // R.A. of the ascending node
	private double e = 0.25;// 0.0001492; //eccentricity
	private double w_0 = 33.8641; // argument of perigee
	private double M_0 = 326.2322; // degrees - mean anomaly at epoch (will be
									// used for last known mean anomaly)
	private double n = 12.62256095; // mean motion (orbits per day right now)

	private double E_0; // radians - eccentric anomaly
	private double TE_0; // degrees - true anomaly
	private double a; // semi-major axis
	private double P; // perigee distance
	private double r; // the radius from the center of the earth

	public Orbit() {
	}

	public void Propogate(double dt) {
		// double dt = t - t_0;
		double nDtdiff = n * dt - (int) (n * dt);
		double M_t = M_0 + 360 * (nDtdiff - (int) (M_0 + 360 * (nDtdiff)) / 360);
		t_0 += dt;
		M_0 = M_t;

		// calculate the other values
		EccentricAnomaly();
		TrueAnomaly();
		SemiMajor();
		Perigee();
		Radius();
	}

	public void EccentricAnomaly() {
		double M_t = M_0 * (Math.PI / 180);
		E_0 = 2 * Math.PI;
		double E_1 = 0.0, diff = 2 * Math.PI;
		double epsilon = 1.0E-5;

		for (int i = 0; i < 30 && diff > epsilon; i++) {
			E_1 = E_0 - (E_0 - e * Math.sin(E_0) - M_t) / (1 - e * Math.cos(E_0));
			diff = Math.abs(E_1 - E_0);
			E_0 = E_1;
		}
		E_1 = E_1 * (180 / Math.PI);
		E_0 = E_1;
	}

	public void TrueAnomaly() {
		TE_0 = Math.toDegrees(Math.acos((Math.cos(Math.toRadians(E_0)) - e) / (1 - e * Math.cos(Math.toRadians(E_0)))));
	}

	public void SemiMajor() {
		a = Math.pow(MU / Math.pow(2 * Math.PI * n, 2), 1.0 / 3.0);
	}

	public void Perigee() {
		P = a * (1 - e);
	}

	public void Radius() {
		r = (P * (1 + e)) / (1 + e * Math.pow(Math.cos(t_0), n));
	}

	public double getTrueAnomaly() {
		return TE_0;
	}

	public void setTrueAnomaly(double tE_0) {
		TE_0 = tE_0;
	}

	public double getRadius() {
		return r;
	}

	public void setRadius(double r) {
		this.r = r;
	}

}