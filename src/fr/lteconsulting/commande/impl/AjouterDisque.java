package fr.lteconsulting.commande.impl;

import java.util.UUID;

import fr.lteconsulting.commande.Commande;
import fr.lteconsulting.commande.ContexteExecution;
import fr.lteconsulting.modele.Bibliotheque;
import fr.lteconsulting.modele.Chanson;
import fr.lteconsulting.modele.Disque;
import fr.lteconsulting.outils.Saisie;

public class AjouterDisque implements Commande
{
	@Override
	public String getNom()
	{
		return "Ajouter un disque";
	}

	@Override
	public void executer(ContexteExecution contexte)
	{
		String nom = Saisie.saisie( "Nom du disque" );
		String codeBarre = Saisie.saisie( "Code barre (laisser vide pour génération aléatoire)" );
		if( codeBarre.isEmpty() )
			codeBarre = UUID.randomUUID().toString();

		Disque disque = new Disque( codeBarre, nom );

		while( true )
		{
			String titre = Saisie.saisie( "Nom de la chanson (laisser vide pour terminer)" );
			if( titre.isEmpty() )
				break;

			int duree = Saisie.saisieInt( "Durée de la chanson" );

			Chanson chanson = new Chanson( titre, duree );
			disque.addChanson( chanson );
		}

		contexte.getBibliotheque().ajouterDisque( disque );
	}
}
