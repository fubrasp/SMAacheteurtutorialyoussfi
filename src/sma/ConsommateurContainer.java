package sma;

import jade.core.*;
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
import sma.agents.ConsommateurAgent;

public class ConsommateurContainer extends Application {
    private ConsommateurAgent consommateurAgent;
    private ObservableList<String> observableList;
    public static void main(String[] args) {
        launch(ConsommateurContainer.class);
    }

    public void startContainer(){
        try {

            Runtime runtime = Runtime.instance();
            Profile profile = new ProfileImpl(false);
            profile.setParameter(Profile.MAIN_HOST, "127.0.0.1");
            AgentContainer agentContainer = runtime.createAgentContainer(profile);
            AgentController agentController = agentContainer.createNewAgent("consommateur", "sma.agents.ConsommateurAgent", new Object[]{this});
            agentController.start();
        } catch (ControllerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        startContainer();

        primaryStage.setTitle("Consommateur");
        BorderPane borderPane = new BorderPane();

        HBox hBox = new HBox();
        hBox.setPadding(new Insets(10));
        hBox.setSpacing(10);

        Label labelLivre = new Label("Livre : ");
        TextField textFieldLivre = new TextField();
        Button buttonAcheter = new Button("Acheter");

        hBox.getChildren().add(labelLivre);
        hBox.getChildren().add(textFieldLivre);
        hBox.getChildren().add(buttonAcheter);

        VBox vBox = new VBox();
        GridPane gridPane = new GridPane();

        observableList = FXCollections.observableArrayList();
        ListView<String> listViewMessages = new ListView<String>(observableList);


        gridPane.add(listViewMessages, 0, 0);

        vBox.getChildren().add(gridPane);
        vBox.setPadding(new Insets(10));
        vBox.setSpacing(10);

        borderPane.setTop(hBox);
        borderPane.setCenter(vBox);

        Scene scene = new Scene(borderPane, 400, 500);
        primaryStage.setScene(scene);
        primaryStage.show();

        buttonAcheter.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String livre = textFieldLivre.getText();
                GuiEvent guiEvent = new GuiEvent(this, 1);
                guiEvent.addParameter(livre);
                consommateurAgent.onGuiEvent(guiEvent);
                //observableList.add(livre);
            }
        });

    }

    public ConsommateurAgent getConsommateurAgent() {
        return consommateurAgent;
    }

    public void setConsommateurAgent(ConsommateurAgent consommateurAgent) {
        this.consommateurAgent = consommateurAgent;
    }

    public void viewMessage(GuiEvent guiEvent){
        String message = guiEvent.getParameter(0).toString();
        observableList.add(message);
    }
}
