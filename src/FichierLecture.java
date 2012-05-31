
// Importation //---------------------------------------------------------------
import java.io.*;

// Classe  F i c h i e r E n t r e e //-----------------------------------------
public class FichierLecture {
	
	//--------------------------------------------------------------------Attributs
	private BufferedReader  fichier;
	private StreamTokenizer flux;
	
	//-----------------------------------------------------------------Constructeur
	// Ouvre un fichier en lecture.
	public FichierLecture(String nom) {
		try {
			fichier = new BufferedReader(new FileReader(nom));
			flux = new StreamTokenizer(fichier);
			flux.wordChars(33,65535);
			flux.parseNumbers() ;
		}
		catch(Exception exception) {
			System.err.println("Erreur: Impossible d'ouvrir '"+nom+"'.");
			//   System.exit(1);
		}
	}
	
	//-----------------------------------------------------------------------Fermer
	// Ferme le fichier.
	public void fermer() {
		try {
			flux = null;
			fichier.close();
		}

		catch(Exception exception) {}
	}

	//--------------------------------------------------------------------LireLigne
	// Lit la ligne suivante dans le fichier.
	public String lireLigne() {
		String ligne;

		try {
			do
			{ ligne = fichier.readLine(); }
			while (ligne.equals(""));

			return ligne;
		}
		catch(Exception exception) {}

		return (null);
	}      
}
// Fin //-----------------------------------------------------------------------


