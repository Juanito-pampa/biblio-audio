package fr.lteconsulting;

import fr.lteconsulting.modele.Bibliotheque;

public class Application
{
	public static void main( String[] args )
	{
		Bibliotheque b = new Bibliotheque();		
		InferfaceUtilisateur ui = new InferfaceUtilisateur( b );

		ui.execute();
	}
}
