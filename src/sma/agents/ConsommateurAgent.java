package sma.agents;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.wrapper.ControllerException;
import sma.ConsommateurContainer;


public class ConsommateurAgent extends GuiAgent{

    private ConsommateurContainer gui;

    @Override
    protected void setup(){
        gui= (ConsommateurContainer) getArguments()[0];
        gui.setConsommateurAgent(this);
        System.out.println("Initialisation de l'agent "+this.getAID().getName());

        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                MessageTemplate messageTemplate = MessageTemplate.or(MessageTemplate.MatchPerformative(ACLMessage.REQUEST), MessageTemplate.MatchPerformative(ACLMessage.REFUSE));
                ACLMessage message = receive(messageTemplate);
                if(message!= null){
                    System.out.println("Reception d'un message"+message.getContent());
                    GuiEvent guiEvent = new GuiEvent(this, 1);
                    guiEvent.addParameter(message.getContent());
                    gui.viewMessage(guiEvent);
                }
            }
        });
    }

    @Override
    protected void takeDown(){
        System.out.println("Destruction de l'agent");
    }

    @Override
    protected void beforeMove() {
        try {
            System.out.println("Avant migration ... du container "+this.getContainerController().getContainerName());
        } catch (ControllerException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void afterMove() {
        try {
            System.out.println("Apres migration ..."+this.getContainerController().getContainerName());
        } catch (ControllerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onGuiEvent(GuiEvent guiEvent) {
        ACLMessage aclMessage = new ACLMessage(ACLMessage.REQUEST);
        String livre = guiEvent.getParameter(0).toString();
        aclMessage.setContent(livre);
        aclMessage.addReceiver(new AID("acheteur", AID.ISLOCALNAME));
        send(aclMessage);
    }
}
