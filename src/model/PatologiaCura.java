package model;

public class PatologiaCura {
	
	private String da;
	private String a;
	private String cfPaziente;
	private Patologia patologia;
	private Farmaco farmaco;
	private Long idVisita;
	
	public PatologiaCura() {
		super();
		// TODO Auto-generated constructor stub
	}

	

	public PatologiaCura(String da, String a, String cfPaziente, Patologia patologia, Farmaco farmaco, Long idVisita) {
		super();
		this.da = da;
		this.a = a;
		this.cfPaziente = cfPaziente;
		this.patologia = patologia;
		this.farmaco = farmaco;
		this.idVisita = idVisita;

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

	public Long getIdVisita() {
		return idVisita;
	}

	public void setIdVisita(Long idVisita) {
		this.idVisita = idVisita;
	}



	public Patologia getPatologia() {
		return patologia;
	}



	public void setPatologia(Patologia patologia) {
		this.patologia = patologia;
	}



	public Farmaco getFarmaco() {
		return farmaco;
	}



	public void setFarmaco(Farmaco farmaco) {
		this.farmaco = farmaco;
	}

	
	
	
	
	
}
