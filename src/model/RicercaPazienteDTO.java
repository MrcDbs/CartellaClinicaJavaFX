package model;

public class RicercaPazienteDTO {
	
	private String codiceFiscale;
	private String cognome;
	private String nome;
	private String patologia;
	
	
	public RicercaPazienteDTO() {
		super();
		// TODO Auto-generated constructor stub
	}


	public String getCodiceFiscale() {
		return codiceFiscale;
	}


	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}


	public String getCognome() {
		return cognome;
	}


	public void setCognome(String cognome) {
		this.cognome = cognome;
	}


	public String getNome() {
		return nome;
	}


	public void setNome(String nome) {
		this.nome = nome;
	}


	public String getPatologia() {
		return patologia;
	}


	public void setPatologia(String patologia) {
		this.patologia = patologia;
	}
	
	

}
