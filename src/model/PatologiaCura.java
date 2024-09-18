package model;

public class PatologiaCura {
	
	private String da;
	private String a;
	private String cfPaziente;
	private Long patologia;
	private Long farmaco;
	private Long idVisita;
	private String patologiaNome;
	private String farmacoNome;
	
	public PatologiaCura() {
		super();
		// TODO Auto-generated constructor stub
	}

	

	public PatologiaCura(String da, String a, String cfPaziente, Long patologia, Long farmaco, Long idVisita,
			String patologiaNome, String farmacoNome) {
		super();
		this.da = da;
		this.a = a;
		this.cfPaziente = cfPaziente;
		this.patologia = patologia;
		this.farmaco = farmaco;
		this.idVisita = idVisita;
		this.patologiaNome = patologiaNome;
		this.farmacoNome = farmacoNome;
	}



	public String getDa() {
		return da;
	}

	public void setDa(String da) {
		this.da = da;
	}

	public String getA() {
		return a;
	}

	public void setA(String a) {
		this.a = a;
	}

	public String getCfPaziente() {
		return cfPaziente;
	}

	public void setCfPaziente(String cfPaziente) {
		this.cfPaziente = cfPaziente;
	}

	public Long getPatologia() {
		return patologia;
	}

	public void setPatologia(Long patologia) {
		this.patologia = patologia;
	}

	public Long getFarmaco() {
		return farmaco;
	}

	public void setFarmaco(Long farmaco) {
		this.farmaco = farmaco;
	}

	public Long getIdVisita() {
		return idVisita;
	}

	public void setIdVisita(Long idVisita) {
		this.idVisita = idVisita;
	}



	public String getPatologiaNome() {
		return patologiaNome;
	}



	public void setPatologiaNome(String patologiaNome) {
		this.patologiaNome = patologiaNome;
	}



	public String getFarmacoNome() {
		return farmacoNome;
	}



	public void setFarmacoNome(String farmacoNome) {
		this.farmacoNome = farmacoNome;
	}
	
	
	
	
	
}
