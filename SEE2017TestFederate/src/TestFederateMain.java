import java.io.File;

import java.awt.*; // Using AWT container and component classes
import java.awt.event.*; // Using AWT event classes and listener interfaces

import skf.config.Configuration;
import skf.config.ConfigurationFactory;
import federate.TestFederate;
import federate.TestFederateAmbassador;

// An AWT program inherits from the top-level container java.awt.Frame
public class TestFederateMain extends Frame implements ActionListener {
	private static final File conf = new File("conf/conf.json");
	private static Configuration configuration;
	private static TestFederate federate;

	private Label name;
	private Button reset;
	private Button join;
	private Button resign;
	private TextField status;

	// Constructor to setup GUI components and event handlers
	public TestFederateMain() {
		setLayout(new GridLayout(3, 2));

		name = new Label("MWSU Satellite Federate");
		add(name);

		reset = new Button("Reset");
		add(reset);

		join = new Button("Join");
		add(join);

		resign = new Button("Resign");
		add(resign);

		reset.addActionListener(this);
		join.addActionListener(this);
		resign.addActionListener(this);

		status = new TextField("Welcome to the MWSU Satellite Federate", 10);
		status.setEditable(false);
		add(status);

		setTitle("MWSU Satellite Federate");
		setSize(600, 300);

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				try {
					federate.diconnectFromRTI();
				} catch (Exception e) {
					e.printStackTrace();
				}
				dispose();
			}
		});

		setVisible(true);
	}

	public static void main(String[] args) throws Exception {
		ConfigurationFactory factory = new ConfigurationFactory();
		configuration = factory.importConfiguration(conf);

		TestFederateAmbassador amb = new TestFederateAmbassador();
		federate = new TestFederate(amb);

		new TestFederateMain();
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		switch (event.getActionCommand()) {
		case "Join":
			try {
				federate.configureAndStart(configuration);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case "Resign":
			try {
				federate.diconnectFromRTI();
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case "Reset":
			TestFederateAmbassador amb = new TestFederateAmbassador();
			try {
				federate = new TestFederate(amb);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
