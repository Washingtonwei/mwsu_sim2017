import java.io.File;
import java.util.Scanner;

import skf.config.Configuration;
import skf.config.ConfigurationFactory;
import federate.TestFederate;
import federate.TestFederateAmbassador;

public class TestFederateMain {


	private static final File conf = new File("conf/conf.json");

	private static Scanner sc = null;

	public static void main(String[] args) throws Exception {

		ConfigurationFactory factory = new ConfigurationFactory();
		Configuration configuration = factory.importConfiguration(conf);

		TestFederateAmbassador amb = new TestFederateAmbassador();
		TestFederate federate = new TestFederate(amb);

		federate.configureAndStart(configuration);

		sc = new Scanner(System.in);
		String currValue = null;
		while(true){
			System.out.println("enter command: ");
			currValue = sc.next();
			System.out.println("command: " + currValue);
			
			if(currValue.equals("a")){
				federate.sendGoToShutdown();
				break;
			}

			if(currValue.equals("q")){
				System.out.println("shutting down");
				federate.diconnectFromRTI();
				break;
			}
		}

	}//main

}
