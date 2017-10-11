package fr.lteconsulting;

import fr.lteconsulting.modele.Bibliotheque;

public interface Commande
{
	String getNom();

	void executer( ContexteExecution contexte );
}
