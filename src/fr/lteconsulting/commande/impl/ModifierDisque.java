package fr.lteconsulting.commande.impl;

import java.util.List;

import fr.lteconsulting.Menu;
import fr.lteconsulting.commande.Commande;
import fr.lteconsulting.commande.ContexteExecution;
import fr.lteconsulting.modele.Chanson;
import fr.lteconsulting.modele.Disque;
import fr.lteconsulting.outils.Saisie;
import fr.lteconsulting.ui.OutilsSaisie;

public class ModifierDisque implements Commande
{
	@Override
	public String getNom()
	{
		return "Modifier un disque";
	}

	@Override
	public void executer( ContexteExecution contexte )
	{
		Disque disque = selectionnerDisque( "modification", contexte );
		if( disque == null )
		{
			System.out.println( "Aucun disque sélectionné, on abandonne..." );
			return;
		}

		disque.afficher( false );

		Menu menu = new Menu( "Modification du disque '" + disque.getNom() + "'" );
		menu.ajouterCommande( new ModifierTitreOuCodeBarreDisque( disque ) );
		menu.ajouterCommande( new AjouterChansonDisque( disque ) );
		// TODO menu.ajouterCommande( new SupprimerChansonDisque( disque ) );
		// TODO menu.ajouterCommande( new ModifierChansonDisque( disque ) );

		Commande commande = menu.saisirCommmande();
		commande.executer( contexte );
	}

	private Disque selectionnerDisque( String sousTitre, ContexteExecution contexte )
	{
		// Choix du type de recherche
		ChoixTypeRecherche choixTypeRecherche = new ChoixTypeRecherche();
		Menu menu = new Menu( "Sélection d'un disque pour " + sousTitre );
		menu.ajouterCommande( new ChoixTypeRechercheCommande( "Rechercher un disque par son nom", TypeRecherche.PAR_NOM, choixTypeRecherche ) );
		menu.ajouterCommande( new ChoixTypeRechercheCommande( "Rechercher un disque par son code barre", TypeRecherche.PAR_CODE_BARRE, choixTypeRecherche ) );
		Commande choixRecherche = menu.saisirCommmande();
		choixRecherche.executer( contexte );

		// Les commandes de choix sont censées alimenter l'objet 'choixTypeRecherche'
		TypeRecherche typeRecherche = choixTypeRecherche.getTypeRecherche();

		return selectionnerDisque( typeRecherche, contexte );
	}

	private Disque selectionnerDisque( TypeRecherche typeRecherche, ContexteExecution contexte )
	{
		switch( typeRecherche )
		{
			case PAR_NOM:
				return selectionnerDisqueParNom( contexte );

			case PAR_CODE_BARRE:
				return selectionnerDisqueParCodeBarre( contexte );

			default:
				return null;
		}
	}

	private Disque selectionnerDisqueParNom( ContexteExecution contexte )
	{
		String critere = Saisie.saisie( "Quel mot-clé à trouver dans le nom ?" );
		List<Disque> disques = contexte.getBibliotheque().rechercherDisqueParNom( critere );

		if( disques != null && disques.size() == 1 )
		{
			// pas besoin de faire choisir s'il n'y a qu'un seul choix !
			return disques.get( 0 );
		}

		ChoixDisque choixDisque = new ChoixDisque();
		Menu menu = new Menu( "Sélection d'un disque" );
		for( Disque disque : disques )
			menu.ajouterCommande( new ChoixDisqueCommande( disque, choixDisque ) );
		Commande commandeDeSelection = menu.saisirCommmande();
		commandeDeSelection.executer( contexte );

		return choixDisque.getDisque();
	}

	private Disque selectionnerDisqueParCodeBarre( ContexteExecution contexte )
	{
		String codeBarre = Saisie.saisie( "Quel code barre (extact) à trouver ?" );
		return contexte.getBibliotheque().rechercherDisqueParCodeBarre( codeBarre );
	}
}

class AjouterChansonDisque implements Commande
{
	private Disque disque;

	public AjouterChansonDisque( Disque disque )
	{
		this.disque = disque;
	}

	@Override
	public String getNom()
	{
		return "Ajouter une chanson";
	}

	@Override
	public void executer( ContexteExecution contexte )
	{
		// TODO : on devrait aussi pouvoir spécifier la position d'insertion

		Chanson chanson = OutilsSaisie.saisirChanson();
		if( chanson != null )
			disque.addChanson( chanson );
	}
}

class ModifierTitreOuCodeBarreDisque implements Commande
{
	private Disque disque;

	public ModifierTitreOuCodeBarreDisque( Disque disque )
	{
		this.disque = disque;
	}

	@Override
	public String getNom()
	{
		return "Modifier son code barre ou son titre";
	}

	@Override
	public void executer( ContexteExecution contexte )
	{
		String nouveauNom = Saisie.saisie( "Entrez le nouveau titre (ENTREE pour laisser '" + disque.getNom() + "')" );
		String nouveauCodeBarre = Saisie.saisie( "Entrez le nouveau code barre (ENTREE pour laisser '" + disque.getCodeBarre() + "')" );

		if( nouveauNom != null )
			disque.setNom( nouveauNom );

		if( nouveauCodeBarre != null )
			disque.setCodeBarre( nouveauCodeBarre );

		System.out.println( "Modification effectuée !" );
	}
}

class ChoixDisque
{
	private Disque disque;

	public Disque getDisque()
	{
		return disque;
	}

	public void setDisque( Disque disque )
	{
		this.disque = disque;
	}
}

class ChoixDisqueCommande implements Commande
{
	private ChoixDisque choix;

	private Disque disqueASelectionner;

	public ChoixDisqueCommande( Disque disqueASelectionner, ChoixDisque choix )
	{
		this.disqueASelectionner = disqueASelectionner;
		this.choix = choix;
	}

	@Override
	public String getNom()
	{
		return disqueASelectionner.getNom();
	}

	@Override
	public void executer( ContexteExecution contexte )
	{
		choix.setDisque( disqueASelectionner );
	}
}

class ChoixTypeRechercheCommande implements Commande
{
	private String nom;
	private TypeRecherche typeRecherche;

	private ChoixTypeRecherche choixTypeRecherche;

	public ChoixTypeRechercheCommande( String nom, TypeRecherche typeRecherche, ChoixTypeRecherche choixTypeRecherche )
	{
		this.nom = nom;
		this.typeRecherche = typeRecherche;
		this.choixTypeRecherche = choixTypeRecherche;
	}

	@Override
	public String getNom()
	{
		return nom;
	}

	@Override
	public void executer( ContexteExecution contexte )
	{
		choixTypeRecherche.setTypeRecherche( typeRecherche );
	}
}

class ChoixTypeRecherche
{
	private TypeRecherche typeRecherche = TypeRecherche.PAR_NOM;

	public void setTypeRecherche( TypeRecherche typeRecherche )
	{
		this.typeRecherche = typeRecherche;
	}

	public TypeRecherche getTypeRecherche()
	{
		return typeRecherche;
	}
}

enum TypeRecherche
{
	PAR_NOM,
	PAR_CODE_BARRE;
}
