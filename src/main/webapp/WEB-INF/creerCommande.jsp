<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="joda" uri="http://www.joda.org/joda/time/tags" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Création d'une commande</title>
<link type="text/css" rel="stylesheet" href="<c:url value="/inc/style.css"></c:url>" />
</head>
<body>
	<c:import url="/inc/menu.jsp" ></c:import>
	<div>
		<form method="post" action="<c:url value="/creationCommande"></c:url>" enctype="multipart/form-data">

			<fieldset>
				<legend>Informations client</legend>
				<%-- Si et seulement si la Map des clients en session n'est pas vide, alors on propose un choix à l'utilisateur --%>
				<c:if test="${ !empty sessionScope.clients }">
					<label for="choixNouveauClient">Nouveau client ? <span class="requis">*</span></label>
					<input type="radio" id="choixNouveauClient" name="choixNouveauClient" value="nouveauClient" checked /> Oui
					<input type="radio" id="choixNouveauClient" name="choixNouveauClient" value="ancienClient" /> Non <br />
				</c:if>

				<c:set var="client" value="${ commande.client }" scope="request"></c:set>
				<div id="nouveauClient">
					<c:import url="/inc/inc_client_form.jsp"></c:import>
				</div>
				<%-- Si et seulement si la Map des clients en session n'est pas vide, alors on crée la liste déroulante --%>
				<c:if test="${ !empty sessionScope.clients }">
					<div id="ancienClient">
						<select name="listeClients" id="listeClients">
							<option value="">Choisissez un client...</option>
							<%-- Boucle sur la map des clients --%>
							<c:forEach items="${ sessionScope.clients }" var="mapClients">
								<%--  L'expression EL ${mapClients.value} permet de cibler l'objet Client stocké en tant que valeur dans la Map, 
                                  et on cible ensuite simplement ses propriétés nom et prenom comme on le ferait avec n'importe quel bean. --%>
								<option value="${ mapClients.key }">${ mapClients.value.prenom } ${ mapClients.value.nom }</option>
							</c:forEach>
						</select>
					</div>
				</c:if>
			</fieldset>

			<fieldset>
				<legend>Informations commande</legend>

				<label for="dateCommande">Date <span class="requis">*</span></label>
				<input type="text" id="v" name="dateCommande" value="<joda:format value="${ commande.date }" 
				pattern="dd/MM/yyyy HH:mm:ss"></joda:format>" size="30" maxlength="30" disabled /> 
				<span class="erreur">${form.erreurs['dateCommande']}</span>
				<br /> 
				<label for="montantCommande">Montant <span class="requis">*</span></label> 
				<input type="text" id="montantCommande" name="montantCommande" value="<c:out value="${ commande.montant }" />" size="30" maxlength="30" /> 
				<span class="erreur">${form.erreurs['montantCommande']}</span>
				<br /> 
				<label for="modePaiementCommande">Mode de paiement <span class="requis">*</span> </label> 
				<input type="text" id="modePaiementCommande" name="modePaiementCommande" value="<c:out value="${ commande.modePaiement }" />" size="20" maxlength="20" /> 
				<span class="erreur">${form.erreurs['modePaiementCommande']}</span>
				<br /> 
				<label for="statutPaiementCommande">Statut du paiement</label> 
				<input type="text" id="statutPaiementCommande" name="statutPaiementCommande" value="<c:out value="${ commande.statutPaiement }" />" size="20" maxlength="20" /> 
				<span class="erreur">${form.erreurs['statutPaiementCommande']}</span>
				<br /> 
				<label for="modeLivraisonCommande">Mode de livraison <span class="requis">*</span></label> 
				<input type="text" id="modeLivraisonCommande" name="modeLivraisonCommande" value="<c:out value="${ commande.modeLivraison }" />" size="20" maxlength="20" /> 
				<span class="erreur">${form.erreurs['modeLivraisonCommande']}</span>
				<br /> 
				<label for="statutLivraisonCommande">Statut de la livraison</label> 
				<input type="text" id="statutLivraisonCommande" name="statutLivraisonCommande" value="<c:out value="${ commande.statutLivraison }" />" size="20" maxlength="20" /> 
				<span class="erreur">${form.erreurs['statutLivraisonCommande']}</span>
				<br />

				<p class="info">${ form.resultat }</p>
			</fieldset>
			<input type="submit" value="Valider" /> 
			<input type="reset" value="Remettre à zéro" /> <br />
		</form>
	</div>

	<script src="<c:url value="/inc/jquery.js" ></c:url>"></script>
	<%-- Petite fonction jQuery permettant le remplacement de la première partie du formulaire par la liste déroulante, 
	au clic sur le bouton radio. --%>
	<script>
		jQuery(document).ready(function() {
			/* 1 - Au lancement de la page, on cache le bloc d'éléments du formulaire correspondant aux clients existants */
			$("div#ancienClient").hide();
			/* 2 - Au clic sur un des deux boutons radio "choixNouveauClient", on affiche le bloc d'éléments correspondant (nouveau ou ancien client) */
			jQuery('input[name=choixNouveauClient]:radio').click(function() {
				$("div#nouveauClient").hide();
				$("div#ancienClient").hide();
				var divId = jQuery(this).val();
				$("div#" + divId).show();
			});
		});
	</script>
</body>
</html>