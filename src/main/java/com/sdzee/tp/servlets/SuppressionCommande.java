package com.sdzee.tp.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.sdzee.tp.dao.CommandeDao;
import com.sdzee.tp.dao.DAOException;
import com.sdzee.tp.entities.Commande;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(urlPatterns = { "/suppressionCommande" })
public class SuppressionCommande extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public static final String PARAM_ID_COMMANDE = "idCommande";
	public static final String SESSION_COMMANDES = "commandes";

	public static final String VUE = "/listeCommandes";

	@EJB
	private CommandeDao commandeDao;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/* R�cup�ration du param�tre */
		String idCommande = getValeurParametre(request, PARAM_ID_COMMANDE);
		Long id = Long.parseLong(idCommande);

		/* R�cup�ration de la Map des commandes enregistr�es en session */
		HttpSession session = request.getSession();
		@SuppressWarnings("unchecked")
		Map<Long, Commande> commandes = (HashMap<Long, Commande>) session.getAttribute(SESSION_COMMANDES);

		/* Si l'id de la commande et la Map des commandes ne sont pas vides */
		if (id != null && commandes != null) {
			try {
				/* Alors suppression de la commande de la BDD */
				commandeDao.supprimer(commandes.get(id));
				/* Puis suppression de la commande de la Map */
				commandes.remove(id);
			} catch (DAOException e) {
				e.printStackTrace();
			}

			/* Et remplacement de l'ancienne Map en session par la nouvelle */
			session.setAttribute(SESSION_COMMANDES, commandes);
		}

		/* Redirection vers la fiche r�capitulative */
		response.sendRedirect(request.getContextPath() + VUE);
	}

	/*
	 * M�thode utilitaire qui retourne null si un param�tre est vide, et son contenu
	 * sinon.
	 */
	private static String getValeurParametre(HttpServletRequest request, String nomChamp) {
		String valeur = request.getParameter(nomChamp);
		if (valeur == null || valeur.trim().length() == 0) {
			return null;
		} else {
			return valeur;
		}
	}

}
