package model;

import java.sql.Date;

public class Medico {
	private String codiceFiscale;
	private String cognome;
	private String nome;
	private String dataNascita;
	private String luogoNascita;
	private String residenza;
	private String numeroTelefono;
	private String username;
	private String password;
	private Date dal;
	private Date al;
	private Long attivo;
	private Long responsabile;
	private char sesso;
	
	public Medico() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Medico(String codiceFiscale, String cognome, String nome, String dataNascita, String luogoNascita,
			String residenza, String numeroTelefono, String username, String password, Date dal, Date al, Long attivo,
			Long responsabile, char sesso) {
		super();
		this.codiceFiscale = codiceFiscale;
		this.cognome = cognome;
		this.nome = nome;
		this.dataNascita = dataNascita;
		this.luogoNascita = luogoNascita;
		this.residenza = residenza;
		this.numeroTelefono = numeroTelefono;
		this.username = username;
		this.password = password;
		this.dal = dal;
		this.al = al;
		this.attivo = attivo;
		this.responsabile = responsabile;
		this.sesso = sesso;
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

	public String getNumeroTelefono() {
		return numeroTelefono;
	}

	public void setNumeroTelefono(String numeroTelefono) {
		this.numeroTelefono = numeroTelefono;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getDal() {
		return dal;
	}

	public void setDal(Date dal) {
		this.dal = dal;
	}

	public Date getAl() {
		return al;
	}

	public void setAl(Date al) {
		this.al = al;
	}

	public Long getAttivo() {
		return attivo;
	}

	public void setAttivo(Long attivo) {
		this.attivo = attivo;
	}

	public Long getResponsabile() {
		return responsabile;
	}

	public void setResponsabile(Long responsabile) {
		this.responsabile = responsabile;
	}

	public char getSesso() {
		return sesso;
	}

	public void setSesso(char sesso) {
		this.sesso = sesso;
	}
	
	
	
	
	

}
