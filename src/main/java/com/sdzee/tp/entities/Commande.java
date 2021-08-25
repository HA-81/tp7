package com.sdzee.tp.entities;

import java.io.Serializable;

import org.joda.time.DateTime;

@Entity
public class Commande implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne
	@JoinColumn(name = "id_client")
	private Client client;
	@Column(columnDefinition = "TIMESTAMP")
	@Converter(name = "dateTimeConverter", converterClass = JodaDateTimeConverter.class)
	@Convert("dateTimeConverter")
	private DateTime date;
	private Double montant;
	@Column(name = "mode_paiement")
	private String modePaiement;
	@Column(name = "statut_paiement")
	private String statutPaiement;
	@Column(name = "mode_livraison")
	private String modeLivraison;
	@Column(name = "statut_livraison")
	private String statutLivraison;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public DateTime getDate() {
		return date;
	}

	public void setDate(DateTime date) {
		this.date = date;
	}

	public Double getMontant() {
		return montant;
	}

	public void setMontant(Double montant) {
		this.montant = montant;
	}

	public String getModePaiement() {
		return modePaiement;
	}

	public void setModePaiement(String modePaiement) {
		this.modePaiement = modePaiement;
	}

	public String getStatutPaiement() {
		return statutPaiement;
	}

	public void setStatutPaiement(String statutPaiement) {
		this.statutPaiement = statutPaiement;
	}

	public String getModeLivraison() {
		return modeLivraison;
	}

	public void setModeLivraison(String modeLivraison) {
		this.modeLivraison = modeLivraison;
	}

	public String getStatutLivraison() {
		return statutLivraison;
	}

	public void setStatutLivraison(String statutLivraison) {
		this.statutLivraison = statutLivraison;
	}

}
