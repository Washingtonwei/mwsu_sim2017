package satellite;

import java.awt.*; // Using AWT container and component classes
import java.awt.event.*; // Using AWT event classes and listener interfaces

// An AWT program inherits from the top-level container java.awt.Frame
public class GUI extends Frame implements ActionListener {

	static final long TIME_TICK = 1000000; // microseconds.
	static final boolean HLA_CONSTRAIN_TIME = true; // set to false for debug

	static Federate federate;

	private Label name;
	private Button join;
	private Button resign;
	private Button gameloop;
	private TextField status;

	// Constructor to setup GUI components and event handlers
	public GUI() {
		setLayout(new BorderLayout());

		name = new Label("MWSU Satellite Federate"); // construct the Label component
		add(name, BorderLayout.PAGE_START);

		status = new TextField("Welcome to the MWSU Satellite Federate", 10);
		status.setEditable(false);
		add(status, BorderLayout.PAGE_END);

		join = new Button("Join");
		add(join, BorderLayout.LINE_START);
		resign = new Button("Resign");
		add(resign, BorderLayout.LINE_END);
		gameloop = new Button("Gameloop");
		add(gameloop, BorderLayout.CENTER);

		join.addActionListener(this);
		resign.addActionListener(this);
		gameloop.addActionListener(this);

		setTitle("MWSU Satellite Federate");
		setSize(600, 600);
		
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				dispose();
			}
		});

		setVisible(true);
	}

	public static void main(String[] args) {
		federate = new Federate("10.8.0.193", "8989", TIME_TICK, HLA_CONSTRAIN_TIME);
		GUI gui = new GUI();
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		switch(event.getActionCommand()) {
		case "Join":
			if (!federate.isConnected()) {
				federate.Join();
				if (federate.isConnected()) {
					status.setText("Joined successfully");
				} else {
					status.setText("Error: failed to join");
				}
			} else {
				status.setText("Error: already connected to a federation");
			}
			break;
		case "Resign":
			if (federate.isConnected()) {
				federate.Resign();
				if (federate.isConnected()) {
					status.setText("Resigned successfully");
				} else {
					status.setText("Error: failed to resign");
				}
			} else {
				status.setText("Error: not connected to a federation");
			}
			break;
		case "Gameloop":
			if (federate.isConnected()) {
				federate.Gameloop();
			} else {
				status.setText("Error: not connected to a federation");
			}
			break;
		}
	}
}