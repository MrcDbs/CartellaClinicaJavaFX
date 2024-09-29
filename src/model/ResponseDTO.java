package model;

import java.util.List;

public class ResponseDTO<T> {
	
	private String message;
	private Long statusCode;
	private String esito;
	private List<T> dati;
	private Long id;
	
	
	
	public ResponseDTO() {
		super();
		// TODO Auto-generated constructor stub
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
	public List<T> getDati() {
		return dati;
	}
	public void setDati(List<T> data) {
		this.dati = data;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@Override
	public String toString() {
		return "ResponseDTO [message=" + message + ", statusCode=" + statusCode + ", esito=" + esito + ", data=" + dati
				+ "]";
	}
	
	

}
