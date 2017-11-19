package sma;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.gui.GuiEvent;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import jade.wrapper.StaleProxyException;
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
import sma.agents.VendeurAgent;

public class VendeurContainer extends Application {
    private VendeurAgent vendeurAgent;
    private ObservableList<String> observableList;
    private AgentContainer agentContainer;
    private VendeurContainer vendeurContainer;

    public static void main(String[] args) {
        launch(VendeurContainer.class);
    }

    public void startContainer(){
        try {

            Runtime runtime = Runtime.instance();
            Profile profile = new ProfileImpl(false);
            profile.setParameter(Profile.MAIN_HOST, "127.0.0.1");
            agentContainer = runtime.createAgentContainer(profile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        startContainer();
        vendeurContainer=this;

        primaryStage.setTitle("Vendeur 1");
        BorderPane borderPane = new BorderPane();

        HBox hBox = new HBox();
        hBox.setPadding(new Insets(10));
        hBox.setSpacing(10);

        Label labelVendeur = new Label("Nom du vendeur : ");
        TextField textFieldVendeur = new TextField();
        Button buttonVendeur = new Button("Deployer l'agent vendeur");

        hBox.getChildren().add(labelVendeur);
        hBox.getChildren().add(textFieldVendeur);
        hBox.getChildren().add(buttonVendeur);

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

        buttonVendeur.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String nomVendeur = textFieldVendeur.getText();
                try {
                    AgentController agentController = agentContainer.createNewAgent(nomVendeur,"sma.agents.VendeurAgent",new Object[]{vendeurContainer});
                    agentController.start();
                } catch (StaleProxyException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public VendeurAgent getVendeurAgent() {
        return vendeurAgent;
    }

    public void setVendeurAgent(VendeurAgent vendeurAgent) {
        this.vendeurAgent = vendeurAgent;
    }

    public ObservableList<String> getObservableList() {
        return observableList;
    }

    public void setObservableList(ObservableList<String> observableList) {
        this.observableList = observableList;
    }

    public AgentContainer getAgentContainer() {
        return agentContainer;
    }

    public void setAgentContainer(AgentContainer agentContainer) {
        this.agentContainer = agentContainer;
    }

    public void viewMessage(GuiEvent guiEvent){
        String message = guiEvent.getParameter(0).toString();
        observableList.add(message);
    }
}
