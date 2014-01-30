package com.h4313.deephouse.mactuator.main;

import java.util.Scanner;

import com.h4313.deephouse.mactuator.controller.Controller;
import com.h4313.deephouse.util.DeepHouseCalendar;

public class Main
{
	public static void main(String[] args)
	{
		// Initialisation de l'horloge de simulation
		DeepHouseCalendar.getInstance().init();
		
		// Initialisation du reseau
		Controller.getInstance().initServerListener(Integer.valueOf(args[0]).intValue());
		Controller.getInstance().initSensorSender(args[1], Integer.valueOf(args[2]).intValue());
		Controller.getInstance().start();
		
		
		// En attente de l'arret de la machine
		String str = "";
		Scanner scExit;
		do {
			scExit = new Scanner(System.in);
			System.out.println("/// Tapez 'EXIT' pour arreter la machine ///");
			str = scExit.nextLine();
		} while (!str.toLowerCase().contains((CharSequence) "exit"));
		scExit.close();
	
		Controller.getInstance().stopController();
		
		
		System.out.println("Arret du serveur");

		System.exit(0);
	}
}