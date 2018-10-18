
import java.awt.Event;
import java.awt.event.KeyEvent;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

public class Affiche2 extends Affiche1 {
	public Affiche2() {
		super("Extaction d'informations", 500, 500);
	}

	@Override
	public void f() throws SQLException {
		// se connecter a la BDD
		Connection c = (Connection) DriverManager.getConnection("jdbc:mysql://127.0.0.1/extra_info", "root", "");
		Statement s = (Statement) c.createStatement();
		// crere le menu bar
		JMenuBar menuBar = new JMenuBar();
		f.setJMenuBar(menuBar); // ratacher le a la fenetre

		// crere et ajouter les menu
		// (dictionnaire,aspiration,info,enrichier,exit)
		JMenu menu = new JMenu("Dictionnaire");
		menuBar.add(menu);
		JMenuItem newMenuItem = new JMenuItem("Aspiration");
		menu.add(newMenuItem);

		JMenuItem newMenuItem2 = new JMenuItem("Infos");

		menu.add(newMenuItem2);
		JMenuItem newMenuItem4 = new JMenuItem("Enrichir");
		menu.add(newMenuItem4);
		JMenuItem newMenuItem3 = new JMenuItem("Exit");
		menu.add(newMenuItem3);

		// les raccourcis
		newMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, Event.CTRL_MASK));
		newMenuItem2.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, Event.CTRL_MASK));
		newMenuItem4.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, Event.CTRL_MASK));
		newMenuItem3.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, Event.CTRL_MASK));

		// le menu corpus avec ses sous menu et ses raccourcis
		JMenu menu2 = new JMenu("Corpus");
		menuBar.add(menu2);
		JMenuItem MenuItem = new JMenuItem("Extraction");
		MenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, Event.CTRL_MASK));
		menu2.add(MenuItem);
		JMenuItem MenuItem2 = new JMenuItem("Vider BDD");
		MenuItem2.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, Event.CTRL_MASK));
		menu2.add(MenuItem2);
		JMenuItem MenuItem3 = new JMenuItem("Coalcul de coocurence");
		MenuItem3.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, Event.CTRL_MASK));
		menu2.add(MenuItem3);
		f.setVisible(true);

		// pour convoquer les actions listenir

		try {
			Asp4Vidal.Asp(newMenuItem);
		} catch (Exception ex) {
			Logger.getLogger(Affiche2.class.getName()).log(Level.SEVERE, null, ex);
		}
		try {
			Asp4Vidal.Info(newMenuItem2);
		} catch (Exception ex) {
			Logger.getLogger(Affiche2.class.getName()).log(Level.SEVERE, null, ex);
		}
		try {
			Asp4Vidal.Exit(this.f, newMenuItem3);
		} catch (Exception ex) {
			Logger.getLogger(Affiche2.class.getName()).log(Level.SEVERE, null, ex);
		}
		try {
			Asp4Vidal.Enrichir(newMenuItem4);
		} catch (Exception ex) {
			Logger.getLogger(Affiche2.class.getName()).log(Level.SEVERE, null, ex);
		}

		try {
			Asp4Vidal.Extraction(MenuItem, s);
		} catch (Exception ex) {
			Logger.getLogger(Affiche2.class.getName()).log(Level.SEVERE, null, ex);
		}
		try {
			Asp4Vidal.Vider(MenuItem2, s);
		} catch (Exception ex) {
			Logger.getLogger(Affiche2.class.getName()).log(Level.SEVERE, null, ex);
		}
		try {
			Asp4Vidal.calc_coo(MenuItem3);
		} catch (Exception ex) {
			Logger.getLogger(Affiche2.class.getName()).log(Level.SEVERE, null, ex);
		}
		super.f(); // pour la class mere (f.setVisible(true);)
	}

}
