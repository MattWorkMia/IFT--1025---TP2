package dirogue.example;

import java.io.IOException;

import dirogue.example.code_squelette.Exterieur;
import dirogue.example.code_squelette.Piece;
import dirogue.example.code_squelette.PieceNotFoundException;
import dirogue.example.code_squelette.RencontreType;
import dirogue.example.code_squelette.Server;

/**
 * Classe représentant le serveur pour l'application DIROgue.
 * Ce serveur écoute les commandes provenant du client, telles que la création
 * de pièces, l'ajout de corridors, la fin de la création du labyrinthe, la
 * sauvegarde du rapport d'aventure, etc.
 */
public class DIROgueServer {
	static boolean exterieurAjoute = false;
	static MonLabyrinthe2 l = new MonLabyrinthe2();
	static MonAventure m = null;

	public static void main(String[] args) {

		try {
			Server s = new Server(1370);

			s.addEventHandler((cmd, cmdArgs) -> {
				if (cmd.equals("piece")) {
					if (cmdArgs.length == 2) {
						int id = Integer.parseInt(cmdArgs[0]);
						RencontreType type = RencontreType.valueOf(cmdArgs[1].toUpperCase());
						if (!exterieurAjoute) {
							l.ajouteEntree(Exterieur.getExterieur(), new Piece(id, type));
							exterieurAjoute = true;
						} else {
							l.ajoutePiece(new Piece(id, type));
						}
					}
				}
			});

			s.addEventHandler((cmd, cmdArgs) -> {
				if (cmd.equals("CORRIDORS")) {
					System.out.println("CORRIDORS commande recue...");
				}
			});

			s.addEventHandler((cmd, cmdArgs) -> {
				if (cmd.equals("corridor")) {
					if (cmdArgs.length == 2) {
						int id1 = Integer.parseInt(cmdArgs[0]);
						int id2 = Integer.parseInt(cmdArgs[0]);
						try {
							l.ajouteCorridor(id1, id2);
							System.out.println("corridor " + id1 + " " + id2 + "ajouté.");
						} catch (PieceNotFoundException e) {
							System.out.println("Erreur lors de l'ajout du corridor"  + id1 + " " + id2 + " " + e.getMessage());
						}
					}
				}
			});

			s.addEventHandler((cmd, cmdArgs) -> {
				if (cmd.equals("FIN")) {
					System.out.println("FIN commande recue...");
					m = new MonAventure(l);
				}
			});

			s.addEventHandler((cmd, cmdArgs) -> {
				if (cmd.equals("save")) {
					if (cmdArgs.length == 1) {
						String save_path = cmdArgs[0];
						try {
							m.sauvegarderRapport(save_path);
						}
        				catch (IOException e) {
            				System.out.println("Erreur lors du save de " + save_path + " " + e.getMessage());
        				}
					}
				}
			});

			s.listen();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
