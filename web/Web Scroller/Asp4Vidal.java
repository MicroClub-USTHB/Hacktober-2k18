
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import com.mysql.jdbc.Statement;

class Asp4Vidal {
	static void Asp(JMenuItem newMenuItem) throws Exception {

		newMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				char c;
				char ex;
				String ch ="";
				String sh;
				try {
					 sh = JOptionPane.showInputDialog("Entrez l'URL");
				} catch (Exception e_) {
					sh = "http://127.0.0.1/home/vidal/vidal-Sommaires-Substances-A.htm";
				}
				try {
					c = JOptionPane.showInputDialog("entrez le debut d'interval ( DEFAULT : A )").toUpperCase()
							.charAt(0);
				} catch (Exception e_) {
					c = 'A';
				}
				try {
					ex = JOptionPane.showInputDialog("entrez le fin d'interval ( DEFAULT : Z )").toUpperCase()
							.charAt(0);
				} catch (Exception e_) {
					ex = 'Z';
				}
				Pattern p = Pattern.compile("(.*)\\.htm");

				Matcher matcher = p.matcher(sh);
				boolean matchFound = matcher.find();

				if (matchFound) {
					ch = matcher.group(1);
				}

				Thread t = new Thread(new Thd(ch, c, ex));
				t.start();
			}
		});
	}

	static void Info(JMenuItem newMenuItem) throws Exception {

		newMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ArrayList<Tableau> lst = new ArrayList<>();
				Tableau tab = new Tableau();

				int nbMot = 0;
				String ligne;
				char i = 'a';
				try {

					PrintWriter result = new PrintWriter("info.txt", "UTF-16LE");
					result.write("\uFEFF");

					try {
						BufferedReader entree = new BufferedReader(
								new InputStreamReader(new FileInputStream(new File("subst.dic")), "UTF-16"));
						ligne = entree.readLine();

						if (ligne != null)
							if (ligne.charAt(0) == 'é') {
								String t = 'e' + ligne.substring(1, ligne.length());
								i = 'e';
								ligne = t;
							} else
								i = ligne.charAt(0);

						do {
							while (i != ligne.charAt(0))
								i++;
							while (ligne != null && i == ligne.charAt(0)) {

								nbMot++;
								tab.nbOcc++;
								tab.lettre = i;
								ligne = entree.readLine();

								if (ligne != null && ligne.charAt(0) == 'é') {
									String t = 'e' + ligne.substring(1, ligne.length());
									ligne = t;
								}

							}
							lst.add(tab);
							tab = new Tableau();
							if (ligne == null)
								break;
						} while (true);
						entree.close();
						System.out.println("TOTAL ==>" + nbMot);

						for (Tableau t : lst) {
							t.Afficher();
							result.println(t);
						}
						result.println("TOTAL ==>" + nbMot);
						entree.close();
					} catch (FileNotFoundException ex) {
						Logger.getLogger(Asp4Vidal.class.getName()).log(Level.SEVERE, null, ex);

					}
					result.close();

				} catch (IOException ex) {
					Logger.getLogger(Asp4Vidal.class.getName()).log(Level.SEVERE, null, ex);
				}

			}
		});

	}
	// 6----------------------

	static public void Exit(JFrame fe, JMenuItem it) {

		it.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				fe.setVisible(false);
				fe.dispose();
			}
		});
	}

	static public void Extraction(JMenuItem it, Statement st) {

		it.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Thread t1 = new Thread(new Thd_corpus(st));
				t1.start();
			}
		});
	}

	static public void Vider(JMenuItem it, Statement st) {

		it.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String req = "TRUNCATE TABLE `subst`;";
				try {
					st.executeUpdate(req);
					JButton bt = new JButton("OK");
					JOptionPane.showMessageDialog(bt, "Table Videe");
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
	}

	static public void Enrichir(JMenuItem it) {

		it.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				TreeSet<String> Ts = new TreeSet<String>();
				String c = "e1";
				try {
					BufferedReader dict = new BufferedReader(
							new InputStreamReader(new FileInputStream(new File("subst.dic")), "UTF-16"));

					String ligne = dict.readLine();
					do {
						if (ligne == null)
							break;
						ligne = ligne.substring(0, ligne.length() - 2);
						if (ligne.charAt(0) == 'é') {
							String t = "e" + ligne.substring(1, ligne.length()) + "Z";
							ligne = t;
						}
						Ts.add(ligne);
						ligne = dict.readLine();

					} while (true);
					dict.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				try {
					BufferedReader enrich = new BufferedReader(
							new InputStreamReader(new FileInputStream(new File("corpus.txt")), "UTF-16"));
					String t;
					String ligne_cor = enrich.readLine();
					int i = 0;
					do {
						if (ligne_cor == null)
							break;

						Pattern p = Pattern.compile("\\b([A-Z]\\w+)\\b \\d+ mg");

						Matcher matcher = p.matcher(ligne_cor);
						boolean matchFound = matcher.find();

						if (matchFound) {
							if (matcher.group(1).charAt(0) == 'é') {
								t = "e" + matcher.group(1).substring(1, matcher.group(1).length()) + "Z";
								Ts.add(t.toLowerCase());
							} else {
								Ts.add(matcher.group(1).toLowerCase());
							}

							i++;
						}
						ligne_cor = enrich.readLine();

					} while (true);

					enrich.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				try {
					PrintWriter kteb = new PrintWriter("subst.dic", "UTF-16LE");
					kteb.write("\uFEFF");
					for (String ts : Ts) {
						if ((ts.charAt(0) == 'e') && ts.charAt(ts.length() - 1) == 'Z') {
							String s = 'é' + ts.substring(1, ts.length() - 1);
							kteb.println(s);
						}

						else
							kteb.println(ts);
					}
					kteb.close();
					JButton bt3 = new JButton("OK");
					JOptionPane.showMessageDialog(bt3,"Enrichissement Termine");
				} catch (FileNotFoundException | UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
	}

	static public void calc_coo(JMenuItem it) {

		it.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				String a = JOptionPane.showInputDialog(null, "Nombre de tokens : ( entre 2 et 5 ) ", "",
						JOptionPane.QUESTION_MESSAGE);
				int nb_token = Integer.parseInt(a);
				a = JOptionPane.showInputDialog(null, "Fréquence : ", "", JOptionPane.QUESTION_MESSAGE);
				int freq = Integer.parseInt(a);
				List<String> array = new ArrayList<String>();
				String[] b;
				try {

					BufferedReader lire_corpus = new BufferedReader(
							new InputStreamReader(new FileInputStream(new File("corpus.txt")), "UTF-16"));

					PrintWriter ecrire = new PrintWriter("coccurrence.txt");
					String s = "";
					s = lire_corpus.readLine();
					Pattern p = Pattern.compile("[a-zA-Z0-9]");
					Matcher m;
					while (s != null) {
						b = null;
						b = s.split("[ ,./\t;:!?'-]");
						int i = 0;

						while (i < b.length) {
							m = p.matcher(b[i]);
							if (m.find()) {
								b[i] = b[i].toLowerCase();
								array.add(b[i]);
							}
							i++;
						}
						s = lire_corpus.readLine();
					}
					int i, j, k, cpt;
					i = 0;
					Pattern p1 = Pattern.compile("[0-9]");
					Matcher m1, m2;
					while (i < array.size() - nb_token) {
						m1 = p1.matcher(array.get(i));
						m2 = p1.matcher(array.get(i + nb_token));
						if (m1.find() || m2.find())
							i++;
						else {

							cpt = 0;
							j = 0;
							while ((j < array.size() - nb_token - 1) && (cpt < freq)) {
								k = 0;

								while ((k < nb_token) && (array.get(j + k).compareTo(array.get(i + k)) == 0))
									k++;

								if (k == nb_token)
									cpt++;
								j++;
							}

							if (cpt == freq) {
								for (k = 0; k < nb_token; k++) {
									ecrire.write(array.get(i + k));
									ecrire.write(" ");
								}
								ecrire.write("\r\n");
							}

							i++;
						}

					}
					ecrire.close();
					lire_corpus.close();
				} catch (Exception e1) {
					e1.printStackTrace();
				}

			}
		});
	}

}
