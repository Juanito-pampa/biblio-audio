package fr.lteconsulting.commandes;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import fr.lteconsulting.Commande;
import fr.lteconsulting.modele.Bibliotheque;
import fr.lteconsulting.modele.Chanson;
import fr.lteconsulting.modele.Disque;
import fr.lteconsulting.outils.Saisie;

/**
 * Format du fichier de sauvegarde :
 * 
 * Pour la bibliotheque
 * MAGIC_SIGNATURE
 * NOMBRE DE DISQUE
 * [DISQUES]
 * 
 * Pour chaque disque :
 * TITRE
 * CODE BARRE
 * NOMBRE DE CHANSONS DANS LE DISQUE
 * [CHANSONS]
 * 
 * Pour chaque chanson :
 * TITRE
 * DUREE
 */
public class SauvegardeFichier implements Commande
{
	public static final String NOM_FICHIER = "bibliotheque.data";
	public static final String MAGIC_SIGNATURE = "BIB1";

	private Bibliotheque bibliotheque;

	public SauvegardeFichier( Bibliotheque bibliotheque )
	{
		this.bibliotheque = bibliotheque;
	}

	@Override
	public String getNom()
	{
		return "Sauvegarder la bibliothèque dans un fichier";
	}

	public void executer()
	{
		try
		{
			Collection<Disque> disques = bibliotheque.getDisques();

			System.out.println( "Enregistrement des " + disques.size() + " disques en cours..." );

			OutputStream outputStream;
			if( "oui".equals( Saisie.saisie( "Voulez-vous compresser ? (oui/non)" ) ) )
				outputStream = zipOutputStream();
			else
				outputStream = flatOutputStream();

			PrintWriter writer = new PrintWriter( outputStream );

			writer.println( MAGIC_SIGNATURE );
			writer.println( disques.size() );

			for( Disque disque : disques )
			{
				List<Chanson> chansons = disque.getChansons();

				writer.println( disque.getNom() );
				writer.println( disque.getCodeBarre() );
				writer.println( chansons.size() );

				for( Chanson chanson : chansons )
				{
					writer.println( chanson.getNom() );
					writer.println( chanson.getDureeEnSecondes() );
				}
			}

			writer.close();

			System.out.println( "Enregistrement effectué dans le fichier " + NOM_FICHIER );
		}
		catch( FileNotFoundException e )
		{
			e.printStackTrace();
		}
		catch( UnsupportedEncodingException e )
		{
			e.printStackTrace();
		}
		catch( IOException e )
		{
			e.printStackTrace();
		}
	}

	private OutputStream zipOutputStream() throws IOException
	{
		FileOutputStream fos = new FileOutputStream( NOM_FICHIER + ".zip" );
		ZipOutputStream zos = new ZipOutputStream( fos, Charset.forName( "UTF8" ) );
		ZipEntry ze = new ZipEntry( NOM_FICHIER );
		zos.putNextEntry( ze );
		return zos;
	}

	private OutputStream flatOutputStream() throws IOException
	{
		return new FileOutputStream( NOM_FICHIER );
	}
}
