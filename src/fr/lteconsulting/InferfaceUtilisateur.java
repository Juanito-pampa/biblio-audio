package fr.lteconsulting;

import fr.lteconsulting.commandes.AffichageDisquesParCodeBarre;
import fr.lteconsulting.commandes.AffichageDisquesParNom;
import fr.lteconsulting.commandes.AjouterDisque;
import fr.lteconsulting.commandes.ChargerFichier;
import fr.lteconsulting.commandes.GenerationDisques;
import fr.lteconsulting.commandes.Quitter;
import fr.lteconsulting.commandes.RechercheParCodeBarre;
import fr.lteconsulting.commandes.RechercheParNom;
import fr.lteconsulting.commandes.SauvegardeFichier;
import fr.lteconsulting.modele.Bibliotheque;

public class InferfaceUtilisateur
{
	private ContexteExecution contexte;
	private Menu menu = new Menu();

	public InferfaceUtilisateur( ContexteExecution contexte )
	{
		this.contexte = contexte;

		menu.ajouterCommande( new AjouterDisque() );
		menu.ajouterCommande( new RechercheParNom() );
		menu.ajouterCommande( new RechercheParCodeBarre() );
		menu.ajouterCommande( new GenerationDisques() );
		menu.ajouterCommande( new AffichageDisquesParNom() );
		menu.ajouterCommande( new AffichageDisquesParCodeBarre() );
		menu.ajouterCommande( new SauvegardeFichier() );
		menu.ajouterCommande( new ChargerFichier() );
		menu.ajouterCommande( new Quitter() );
	}

	public void execute()
	{
		while( true )
		{
			Commande commandeAExecuter = menu.saisirCommmande();

			commandeAExecuter.executer( contexte );
		}
	}
}
