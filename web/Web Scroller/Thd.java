
import java.awt.FlowLayout;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

// c'est uine classe d'un thread pour l'asspiration
public class Thd implements Runnable {

	String ch;
	char c;
	char ex;

	Thd(String ch, char c, char ex) {
		this.ch = ch;
		this.c = c;
		this.ex = ex;

	}

	// redefinition de la methode run
	public void run() {
		// JOptionPane.showMessageDialog(null, "Bonjour");
int vit;
try{
		String speed = JOptionPane.showInputDialog("Entrez la vitesse d'aspiration ( 0 - 10 | DEFAULT 5)");
		 vit = Integer.parseInt(speed); // le rendre entier
}
catch(Exception e){vit=5;}
		JFrame fram = new JFrame("Aspiration");
		JProgressBar barre = new JProgressBar();
		JProgressBar barre2 = new JProgressBar();
		JLabel UrlLabel = new JLabel();
		JLabel UrlLabe2 = new JLabel();
		barre.setStringPainted(true);

		barre2.setStringPainted(true);
		FlowLayout disposition = new FlowLayout();
		fram.setLayout(disposition);
		fram.setLocation(450, 300);
		fram.add(UrlLabel);
		fram.add(barre);
		fram.add(barre2);
		fram.add(UrlLabe2);
		fram.setSize(350, 150);
		barre.setLocation(200, 100);
		barre2.setLocation(50, 50);
		fram.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fram.setVisible(true);

		int t = 0;
		int d = 100 / (ex - c + 1) + 1;
		int g = 100 / (ex - c + 1) + 1;

		System.out.println(d);

		// -----------------------------------------------------------------
		try {
			PrintWriter kteb = new PrintWriter("subst.dic", "UTF-16LE");
			kteb.write("\uFEFF");
			try {
				PrintWriter ecrire = new PrintWriter("fichier.txt", "UTF-16LE");
				ecrire.write("\uFEFF");
				// PrintWriter ecrire = new PrintWriter(new
				// FileWriter("fichier.txt",true))) {
				// -----------------coller-------------------------

				while (c <= ex) {

					String url_vidal = ch.substring(0, ch.length() - 1) + c + ".htm";

					for (int i = 0; i <= 100; i++) {

						try {
							java.lang.Thread.sleep(vit);
						} catch (InterruptedException ex1) {
							Logger.getLogger(Asp4Vidal.class.getName()).log(Level.SEVERE, null, ex1);
						}
						barre.setValue(i);
						UrlLabel.setText("URL==" + c);

					}

					URL url = new URL(url_vidal);

					ecrire.println(url);

					System.out.println("url a aspirer ==" + url);

					try {
						BufferedReader lire = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
						while (true) {
							String lin = lire.readLine();
							String temp = "";
							if (lin == null)
								break;

							ecrire.println(lin);
							Pattern pattern = Pattern.compile("<a.*.htm\">");
							Matcher matcher = pattern.matcher(lin);

							boolean matchFound = matcher.find();

							if (matchFound) {

								// System.out.println(line);

								temp = matcher.replaceAll(" ");
								temp = temp.replace("</a>", " ");
								temp = temp.trim();
								if (temp.startsWith("<li>")) {
								} else {
									System.out.println(temp);
									kteb.println(temp + ".N");

								}
							}

						}

						for (int i = t; i <= d; i++)

						{
							try {
								java.lang.Thread.sleep(vit);
							} catch (InterruptedException ex1) {
								Logger.getLogger(Asp4Vidal.class.getName()).log(Level.SEVERE, null, ex1);
							}
							barre2.setValue(d);
							// UrlLabe2.setText("URL=="+(int)d+"% ");
						}

						t = d;//des variables pour gerer la barre de progression
						d += g;
						c++;

					} catch (IOException ex1) {
						JButton bt = new JButton("OK");
						JOptionPane.showMessageDialog(bt, "URL \n" + url_vidal + " \n not found");
						c++;
					}

				}

				ecrire.close();

			} catch (IOException ex1) {
				Logger.getLogger(Asp4Vidal.class.getName()).log(Level.SEVERE, null, ex1);
			}
			kteb.close();
		} catch (IOException ex2) {
			Logger.getLogger(Thd.class.getName()).log(Level.SEVERE, null, ex2);
		}

		// -----------------------------------------

		// ----------------------------------------

		barre2.setValue(100);
		JButton bt = new JButton("OK");
		JOptionPane.showMessageDialog(bt, "Aspiration terminee .");
		fram.setVisible(false);
		fram.dispose();

	}

}
