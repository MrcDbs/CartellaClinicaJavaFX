package model;

public class PazienteDTO extends ModelDTO{
	
	private String codiceFiscale;
	private String cognome;
	private String nome;
	private String dataNascita;
	private String luogoNascita;
	private String residenza;
	private String via;
	private String occupazione;
	private String numeroTelefono;
	private char sesso;
	
	public PazienteDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	

	public PazienteDTO(String codiceFiscale, String cognome, String nome, String dataNascita, String luogoNascita,
			String residenza, String via, String occupazione, String numeroTelefono, char sesso) {
		super();
		this.codiceFiscale = codiceFiscale;
		this.cognome = cognome;
		this.nome = nome;
		this.dataNascita = dataNascita;
		this.luogoNascita = luogoNascita;
		this.residenza = residenza;
		this.via = via;
		this.occupazione = occupazione;
		this.numeroTelefono = numeroTelefono;
		this.sesso = sesso;
	}




	public PazienteDTO(String codiceFiscale, String cognome, String nome) {
		super();
		this.codiceFiscale = codiceFiscale;
		this.cognome = cognome;
		this.nome = nome;
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

	public String getDataNascita() {
		return dataNascita;
	}

	public void setDataNascita(String dataNascita) {
		this.dataNascita = dataNascita;
	}

	public String getLuogoNascita() {
		return luogoNascita;
	}

	public void setLuogoNascita(String luogoNascita) {
		this.luogoNascita = luogoNascita;
	}

	public String getResidenza() {
		return residenza;
	}

	public void setResidenza(String residenza) {
		this.residenza = residenza;
	}

	public String getVia() {
		return via;
	}

	public void setVia(String via) {
		this.via = via;
	}

	public String getOccupazione() {
		return occupazione;
	}

	public void setOccupazione(String occupazione) {
		this.occupazione = occupazione;
	}

	public String getNumeroTelefono() {
		return numeroTelefono;
	}

	public void setNumeroTelefono(String numeroTelefono) {
		this.numeroTelefono = numeroTelefono;
	}

	public char getSesso() {
		return sesso;
	}

	public void setSesso(char sesso) {
		this.sesso = sesso;
	}

	@Override
	public String toString() {
		return "PazienteDTO [codiceFiscale=" + codiceFiscale + ", cognome=" + cognome + ", nome=" + nome
				+ ", dataNascita=" + dataNascita + ", luogoNascita=" + luogoNascita + ", residenza=" + residenza
				+ ", via=" + via + ", occupazione=" + occupazione + ", numeroTelefono=" + numeroTelefono + ", sesso="
				+ sesso + "]";
	}
	
	
	
	

}
