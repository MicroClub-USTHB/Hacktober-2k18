public class Tableau {
	// cette classe est faite pour nous aider a calculer notre statiqtiques du
	// fichier subst.dic

	char lettre;
	int nbOcc;

	// constructeur
	public Tableau() {
		this.lettre = ' ';
		this.nbOcc = 0;
	}

	public void Afficher() {
		System.out.println(this.lettre + "\t" + this.nbOcc);
	}

	public String toString() {
		return this.lettre + "\t" + this.nbOcc;
	}
}
