package sma;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.gui.GuiEvent;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sma.agents.AcheteurAgent;
import sma.agents.AcheteurAgent;

public class AcheteurContainer extends Application {
    private AcheteurAgent acheteurAgent;
    private ObservableList<String> observableList;
    public static void main(String[] args) {
        launch(AcheteurContainer.class);
    }

    public void startContainer(){
        try {

            Runtime runtime = Runtime.instance();
            Profile profile = new ProfileImpl(false);
            profile.setParameter(Profile.MAIN_HOST, "127.0.0.1");
            AgentContainer agentContainer = runtime.createAgentContainer(profile);
            AgentController agentController = agentContainer.createNewAgent("acheteur", "sma.agents.AcheteurAgent", new Object[]{this});
            agentController.start();
        } catch (ControllerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        startContainer();

        primaryStage.setTitle("Acheteur");
        BorderPane borderPane = new BorderPane();

        VBox vBox = new VBox();
        GridPane gridPane = new GridPane();

        observableList = FXCollections.observableArrayList();
        ListView<String> listViewMessages = new ListView<String>(observableList);


        gridPane.add(listViewMessages, 0, 0);

        vBox.getChildren().add(gridPane);
        vBox.setPadding(new Insets(10));
        vBox.setSpacing(10);

        borderPane.setCenter(vBox);

        Scene scene = new Scene(borderPane, 400, 500);
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public AcheteurAgent getAcheteurAgent() {
        return acheteurAgent;
    }

    public void setAcheteurAgent(AcheteurAgent acheteurAgent) {
        this.acheteurAgent = acheteurAgent;
    }

    public void viewMessage(GuiEvent guiEvent){
        String message = guiEvent.getParameter(0).toString();
        observableList.add(message);
    }
}
