package sma.agents;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.wrapper.ControllerException;
import sma.AcheteurContainer;


public class AcheteurAgent extends GuiAgent{

    private AcheteurContainer gui;
    private AID[] listVendeurs;

    @Override
    protected void setup(){
        gui= (AcheteurContainer) getArguments()[0];
        gui.setAcheteurAgent(this);
        System.out.println("Initialisation de l'agent "+this.getAID().getName());

        ParallelBehaviour parallelBehaviour = new ParallelBehaviour();
        addBehaviour(parallelBehaviour);
        parallelBehaviour.addSubBehaviour(new TickerBehaviour(this, 6000) {
            @Override
            protected void onTick() {
                DFAgentDescription description = new DFAgentDescription();
                ServiceDescription serviceDescription = new ServiceDescription();
                serviceDescription.setType("Vente");
                description.addServices(serviceDescription);
                try {
                    DFAgentDescription[] agentDescriptions= DFService.search(myAgent, description);
                    listVendeurs=new AID[agentDescriptions.length];
                    for (int i=0;i<agentDescriptions.length;i++){
                        listVendeurs[i]=agentDescriptions[i].getName();
                    }
                } catch (FIPAException e) {
                    e.printStackTrace();
                }
                //serviceDescription.setName("Vente-Livre");
            }
        });

        parallelBehaviour.addSubBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                MessageTemplate messageTemplate = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
                ACLMessage message = receive(messageTemplate);
                if(message!= null){
                    System.out.println("Reception d'un message"+message.getContent());
                    GuiEvent guiEvent = new GuiEvent(this, 1);
                    String nomLivre = message.getContent();
                    guiEvent.addParameter(nomLivre);
                    gui.viewMessage(guiEvent);

                    ACLMessage aclMessage=new ACLMessage(ACLMessage.CFP);
                    aclMessage.setContent(nomLivre);
                    for (AID aid:listVendeurs){
                        aclMessage.addReceiver(aid);
                    }
                    send(aclMessage);
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
        //ACLMessage aclMessage = new ACLMessage(ACLMessage.REQUEST);
        //String livre = guiEvent.getParameter(0).toString();
        //aclMessage.setContent(livre);
        //aclMessage.addReceiver(new AID("acheteur", AID.ISLOCALNAME));
        //send(aclMessage);
    }
}
