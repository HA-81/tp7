package com.sdzee.tp.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.sdzee.tp.dao.ClientDao;
import com.sdzee.tp.dao.DAOException;
import com.sdzee.tp.entities.Client;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(urlPatterns = { "/suppressionClient" })
public class SuppressionClient extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public static final String PARAM_ID_CLIENT = "idClient";
	public static final String SESSION_CLIENTS = "clients";

	public static final String VUE = "/listeClients";

	@EJB
	private ClientDao clientDao;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/* Récupération du paramètre */
		String idClient = getValeurParametre(request, PARAM_ID_CLIENT);
		Long id = Long.parseLong(idClient);

		/* Récupération de la Map des clients enregistrés en session */
		HttpSession session = request.getSession();
		@SuppressWarnings("unchecked")
		Map<Long, Client> clients = (HashMap<Long, Client>) session.getAttribute(SESSION_CLIENTS);

		/* Si l'id du client et la Map des clients ne sont pas vides */
		if (id != null && clients != null) {
			try {
				/* Alors suppression du client de la BDD */
				clientDao.supprimer(clients.get(id));
				/* Puis suppression du client de la Map */
				clients.remove(id);
			} catch (DAOException e) {
				e.printStackTrace();
			}

			/* Et remplacement de l'ancienne Map en session par la nouvelle */
			session.setAttribute(SESSION_CLIENTS, clients);
		}

		/* Redirection vers la fiche récapitulative */
		response.sendRedirect(request.getContextPath() + VUE);
	}

	/*
	 * Méthode utilitaire qui retourne null si un paramètre est vide, et son contenu
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
