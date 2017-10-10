package fr.lteconsulting.commandes;

import fr.lteconsulting.Commande;
import fr.lteconsulting.outils.Saisie;

public class Quitter implements Commande
{
	@Override
	public String getNom()
	{
		return "Quitter l'application";
	}

	@Override
	public void executer()
	{
		String reponse = Saisie.saisie( "Voulez-vous vraiment partir ? (oui/non)" );
		if( "oui".equals( reponse ) )
		{
			System.out.println( "Ciao amigo" );
			System.exit( 0 );
		}
	}
}
