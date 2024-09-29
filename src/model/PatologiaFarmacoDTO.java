package model;

public class PatologiaFarmacoDTO {
	
	private Farmaco farmaco;
	private Patologia patologia;
	private String message;
	private Long statusCode;
	private String esito;
	
	public PatologiaFarmacoDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PatologiaFarmacoDTO(Farmaco farmaco, Patologia patologia, String message, Long statusCode,
			String esito) {
		super();
		this.farmaco = farmaco;
		this.patologia = patologia;
		this.message = message;
		this.statusCode = statusCode;
		this.esito = esito;
	}

	public Farmaco getFarmaco() {
		return farmaco;
	}

	public void setFarmaco(Farmaco farmaco) {
		this.farmaco = farmaco;
	}

	public Patologia getPatologia() {
		return patologia;
	}

	public void setPatologia(Patologia patologia) {
		this.patologia = patologia;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Long getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(Long statusCode) {
		this.statusCode = statusCode;
	}

	public String getEsito() {
		return esito;
	}

	public void setEsito(String esito) {
		this.esito = esito;
	}
	
	

}
