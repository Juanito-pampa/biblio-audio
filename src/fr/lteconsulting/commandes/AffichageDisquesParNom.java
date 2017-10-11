package fr.lteconsulting.commandes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fr.lteconsulting.Commande;
import fr.lteconsulting.ContexteExecution;
import fr.lteconsulting.modele.Bibliotheque;
import fr.lteconsulting.modele.Disque;
import fr.lteconsulting.outils.ComparateurDisqueParNom;

public class AffichageDisquesParNom implements Commande
{
	@Override
	public String getNom()
	{
		return "Afficher les disques par nom";
	}

	@Override
	public void executer(ContexteExecution contexte)
	{
		List<Disque> disques = new ArrayList<Disque>( contexte.getBibliotheque().getDisques() );

		Collections.sort( disques, new ComparateurDisqueParNom() );

		for( Disque disque : disques )
			disque.afficher( false );
	}
}
