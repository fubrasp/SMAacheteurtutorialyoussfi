package sma;

import jade.core.AgentContainer;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.util.ExtendedProperties;
import jade.util.leap.Properties;
import jade.wrapper.ControllerException;


public class MainContainer {
    public static void main(String[] args) {
        Runtime runtime = Runtime.instance();
        Properties properties = new ExtendedProperties();
        properties.setProperty(Profile.GUI,"true");
        Profile profile = new ProfileImpl(properties);
        jade.wrapper.AgentContainer mainContainer = runtime.createMainContainer(profile);
        try {
            mainContainer.start();
        } catch (ControllerException e) {
            e.printStackTrace();
        }
    }
}
