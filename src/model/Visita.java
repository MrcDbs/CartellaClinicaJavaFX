package model;

public class Visita {
	
	private Long idVisita;
	private String cfPaziente;
	private String cfMedico;
	private String data;
	private String note;
	private byte[] allegato;
	
	public Visita() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Visita(Long idVisita, String cfPaziente, String cfMedico, String data, String note, byte[] allegato) {
		super();
		this.idVisita = idVisita;
		this.cfPaziente = cfPaziente;
		this.cfMedico = cfMedico;
		this.data = data;
		this.note = note;
		this.allegato = allegato;
	}

	public Long getIdVisita() {
		return idVisita;
	}

	public void setIdVisita(Long idVisita) {
		this.idVisita = idVisita;
	}

	public String getCfPaziente() {
		return cfPaziente;
	}

	public void setCfPaziente(String cfPaziente) {
		this.cfPaziente = cfPaziente;
	}

	public String getCfMedico() {
		return cfMedico;
	}

	public void setCfMedico(String cfMedico) {
		this.cfMedico = cfMedico;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public byte[] getAllegato() {
		return allegato;
	}

	public void setAllegato(byte[] allegato) {
		this.allegato = allegato;
	}
	
	
	

}
