package com.h4313.deephouse.mactuator.controller;

import com.h4313.deephouse.actuator.Actuator;
import com.h4313.deephouse.frame.Frame;
import com.h4313.deephouse.housemodel.House;
import com.h4313.deephouse.vue.MainVue;

public class Controller extends Thread
{	
	private volatile boolean alive;
	
	private static volatile Controller instance = null;
	
	private ServerListener serverListener;
	
	private SensorSender sensorSender;
	private MainVue vueActuator=null;
	
    /**
     * Constructeur de l'objet.
     */
    private Controller() {
        super();
        this.alive = true;
        vueActuator=MainVue.init(MainVue.VUE_SENSOR);
    }

    /**
     * Méthode permettant de renvoyer une instance de la classe Singleton
     * @return Retourne l'instance du singleton.
     */
    public final static Controller getInstance() {
        if (Controller.instance == null) {
           synchronized(Controller.class) {
             if (Controller.instance == null) {
            	 Controller.instance = new Controller();
             }
           }
        }
        return Controller.instance;
    }
    
    public void initServerListener(int port)
    {
    	serverListener = new ServerListener(port);
    }
    
    public void initSensorSender(String host, int port)
    {
    	sensorSender = new SensorSender(host, port);
    }
    
    @Override
	public void run()
	{
		try
		{			
			while(alive)
			{
				String message = this.serverListener.getMessage();
				if(message != null)
				{
			    	Frame frame = new Frame(message);
			    	Actuator<Object> actuator = House.getInstance().updateActuator(frame);
			    	
					vueActuator.refresh();
			    	sensorSender.submitMessage(actuator.composeFrame());	
				}
				else
				{
					Thread.sleep(2000);
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
    
    public void stopController()
    {
    	this.alive = false;
    	
    	try
    	{
	    	this.serverListener.stopListener();
	    	this.sensorSender.stopSender();
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    }
}
