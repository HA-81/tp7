package com.sdzee.tp.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.sdzee.tp.dao.ClientDao;
import com.sdzee.tp.dao.CommandeDao;
import com.sdzee.tp.dao.DAOFactory;
import com.sdzee.tp.entities.Client;
import com.sdzee.tp.entities.Commande;
import com.sdzee.tp.forms.CreationCommandeForm;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet( urlPatterns = {"/creationCommande"}, initParams = @WebInitParams = @WebInitParam( name = "chemin", value = "/Users/Ali/fichiers/images"))
@MultipartConfig( location = "/tmp", maxFileSize = 2 * 1024 * 1024, maxRequest = 10 * 1024 * 1024, 
fileSizeThreshold = 1024 * 1024 )
public class CreationCommande extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public static final String CONF_DAO_FACTORY = "daofactory";
	public static final String CHEMIN = "chemin";
	public static final String ATT_COMMANDE = "commande";
	public static final String ATT_FORM = "form";
	public static final String SESSION_CLIENTS = "clients";
	public static final String SESSION_COMMANDES = "commandes";

	public static final String VUE_SUCCES = "/WEB-INF/afficherCommande.jsp";
	public static final String VUE_FORM = "/WEB-INF/creerCommande.jsp";

	@EJB
	private ClientDao clientDao;
	@EJB
	private CommandeDao commandeDao;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/* � la r�ception d'une requ�te GET, simple affichage du formulaire */
		this.getServletContext().getRequestDispatcher(VUE_FORM).forward(request, response);
	}

	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/*
		 * Lecture du param�tre 'chemin' pass� � la servlet via la d�claration dans le
		 * web.xml
		 */
		String chemin = this.getServletConfig().getInitParameter(CHEMIN);

		/* Pr�paration de l'objet formulaire */
		CreationCommandeForm form = new CreationCommandeForm(clientDao, commandeDao);

		/* Traitement de la requ�te et r�cup�ration du bean en r�sultant */
		Commande commande = form.creerCommande(request, chemin);

		/* Ajout du bean et de l'objet m�tier � l'objet requ�te */
		request.setAttribute(ATT_COMMANDE, commande);
		request.setAttribute(ATT_FORM, form);

		/* Si aucune erreur */
		if (form.getErreurs().isEmpty()) {
			/* Alors r�cup�ration de la map des clients dans la session */
			HttpSession session = request.getSession();
			Map<Long, Client> clients = (HashMap<Long, Client>) session.getAttribute(SESSION_CLIENTS);
			/* Si aucune map n'existe, alors initialisation d'une nouvelle map */
			if (clients == null) {
				clients = new HashMap<Long, Client>();
			}
			/* Puis ajout du client de la commande courante dans la map */
			clients.put(commande.getClient().getId(), commande.getClient());
			/* Et enfin (r�)enregistrement de la map en session */
			session.setAttribute(SESSION_CLIENTS, clients);

			/* Ensuite r�cup�ration de la map des commandes dans la session */
			Map<Long, Commande> commandes = (HashMap<Long, Commande>) session.getAttribute(SESSION_COMMANDES);
			/* Si aucune map n'existe, alors initialisation d'une nouvelle map */
			if (commandes == null) {
				commandes = new HashMap<Long, Commande>();
			}
			/* Puis ajout de la commande courante dans la map */
			commandes.put(commande.getId(), commande);
			/* Et enfin (r�)enregistrement de la map en session */
			session.setAttribute(SESSION_COMMANDES, commandes);
			/* Affichage de la fiche r�capitulative */
			this.getServletContext().getRequestDispatcher(VUE_SUCCES).forward(request, response);
		} else {
			/* Sinon, r�-affichage du formulaire de cr�ation avec les erreurs */
			this.getServletContext().getRequestDispatcher(VUE_FORM).forward(request, response);
		}
	}
}
