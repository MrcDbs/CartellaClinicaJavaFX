package model;

public class Patologia {
	
	private Long id;
	private String nome;
	private String cause;
	private String effetti;
	
	public Patologia() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Patologia(Long id, String nome, String cause, String effetti) {
		super();
		this.id = id;
		this.nome = nome;
		this.cause = cause;
		this.effetti = effetti;
	}
	
	

	public Patologia(Long id) {
		super();
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCause() {
		return cause;
	}

	public void setCause(String cause) {
		this.cause = cause;
	}

	public String getEffetti() {
		return effetti;
	}

	public void setEffetti(String effetti) {
		this.effetti = effetti;
	}
	
	public String toString() {
		return nome;
	}

}
