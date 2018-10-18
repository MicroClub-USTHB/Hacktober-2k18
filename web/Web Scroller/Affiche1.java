
import java.sql.SQLException;

import javax.swing.JFrame;

//c'est la classe de laquelle on va heriter la classe qui va lancer la fenetre de notre application
public class Affiche1 {
	final JFrame f;

	public Affiche1(String nomF, int h, int l) {
		f = new JFrame("Extraction d'information");
		f.setSize(h, l);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // pour arreter
															// l'execution du
															// notre application
															// .
		f.setLocation(450, 120);

	}

	public void f() throws SQLException {
		f.setVisible(true);
	}
}
