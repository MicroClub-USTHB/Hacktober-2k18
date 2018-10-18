import java.awt.FlowLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.mysql.jdbc.Statement;

//cette classe represente un thread pour l'extraction ;
public class Thd_corpus implements Runnable {
	Statement s; // la connection de la BDD

	// constructeur
	Thd_corpus(Statement s) {
		this.s = s;
	}

	public void run() {
		// redefinition de la methide run()
        int vitt=5;//la vitesse serait 5 par defaut
		String speed = JOptionPane.showInputDialog("Entrez la vitesse d'aspiration (0-10 | DEFAULT 5)");// controler
							// la main pour modifier la vitesse																			// la
																										// vitesse
	    vitt = Integer.parseInt(speed);
		String ligne = "";
		String dic = "";

		// linstancuation du JFileChooser pour le corpus
		JButton open = new JButton();
		JFileChooser f = new JFileChooser();
		f.setCurrentDirectory(new java.io.File("."));
		f.setDialogTitle("Choisi un corpus");
		f.setFileSelectionMode(JFileChooser.FILES_ONLY);
		f.setFileFilter(new FileNameExtensionFilter("Fichier txt", "txt"));//le filtre pour les fichier avec extention .txt

		f.showOpenDialog(open);
		String nom_corpus = f.getSelectedFile().getName();// recuperation du nom
															// du corpus

		// linstancuation du JFileChooser pour le dict
		JFileChooser fc = new JFileChooser();
		fc.setCurrentDirectory(new java.io.File("."));
		fc.setDialogTitle("Choisi un dictionnaire");
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.setFileFilter(new FileNameExtensionFilter("Fichier dic", "dic"));
		fc.showOpenDialog(open);
		String nom_dict = fc.getSelectedFile().getName();// recuperation du nom
															// du dict

		// une petite fenetre pour afficher les noms des medicaments
		JFrame fr = new JFrame("Affichage");
		JLabel affichage = new JLabel();
		FlowLayout disposition = new FlowLayout();
		fr.setLayout(disposition);
		fr.setLocation(450, 300);
		fr.add(affichage);
		fr.setSize(350, 150);
		fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fr.setVisible(true);

		try {
			// generer le fichier qui va contenir les medicaments + le
			// traitement d'hospitalier (html)
			PrintWriter kteb = new PrintWriter("sortie.html", "UTF-16LE");
			kteb.write("\uFEFF");
			int l = 0, i = 0;
			try {
				// lire une ligne du dictionnaire
				BufferedReader entree = new BufferedReader(
						new InputStreamReader(new FileInputStream(new File(nom_dict)), "UTF-16"));
				dic = entree.readLine();

				JButton bt2 = new JButton("OK");

				do {
					l = 0;
					if (dic == null)
						break;
				if(dic.charAt(dic.length()-1)=='N' && dic.charAt(dic.length()-2)=='.')
						dic = dic.substring(0, dic.length() - 2); // pour supprimer
																// le .N

					try {
						// lire une ligne du corpus
						BufferedReader cor = new BufferedReader(
								new InputStreamReader(new FileInputStream(new File(nom_corpus)), "UTF-16"));
						ligne = cor.readLine();

						do {
							l++;
							if (ligne == null)
								break;

							// chercher s'il y a un medicament en mm temps dans
							// le corpus et dans le dictionnaire
							Pattern pattern = Pattern.compile("\\b" + dic.toLowerCase() + "\\b");
							Matcher matcher = pattern.matcher(ligne.toLowerCase());
							// boolean matchFound = matcher.find();

							while (matcher.find()) {
								i++;

								affichage.setText(dic + "\t" + l); // on affiche
																	// le
																	// medicament
																	// trouvé
								java.lang.Thread.sleep(vitt); // regler la
																// vitesse de
																// l'extraction
								String req = "INSERT INTO `subst`(`ID`, `Nom`, `NumLigne`) VALUES (" + i + ",'" + dic
										+ "'," + l + ");"; // la requette
															// d'insertion a la
															// BDD

								// remplir le fichier sortie.html
								kteb.println("<span style = \"color:red\">" + dic + "</span></br><p>" + ligne
										+ "</p><p>numero: \t" + l + "</p></br>");
								s.executeUpdate(req); // executer la requette
								// System.out.println(dic);

							}
							ligne = cor.readLine();
						} while (true);

						cor.close();
					} catch (IOException | SQLException e1) {
						JButton bt1 = new JButton("OK");
						JOptionPane.showMessageDialog(bt1, "problem SQL or IN OUT");
						e1.printStackTrace();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					dic = entree.readLine();

				} while (true);
				// System.out.println("DONE:::::::::::::::");
				entree.close();
				fr.setVisible(false);
				JButton bt3 = new JButton("OK");
				JOptionPane.showMessageDialog(bt3, "Extraction terminée .");
				fr.dispose(); // arreter le thread

			} catch (IOException e1) {

				e1.printStackTrace();
			}
			kteb.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e2) {
			JButton bt = new JButton("OK");
			JOptionPane.showMessageDialog(bt, "file not found || unsupport");
			e2.printStackTrace();
		}
	}
}
