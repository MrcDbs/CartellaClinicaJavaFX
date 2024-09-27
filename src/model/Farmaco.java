package model;

public class Farmaco {
	private Long id;
	private String nome;
	private String principioAttivo;
	
	public Farmaco() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Farmaco(Long id, String nome, String principioAttivo) {
		super();
		this.id = id;
		this.nome = nome;
		this.principioAttivo = principioAttivo;
	}

	
	
	public Farmaco(Long id) {
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

	public String getPrincipioAttivo() {
		return principioAttivo;
	}

	public void setPrincipioAttivo(String principio_attivo) {
		this.principioAttivo = principio_attivo;
	}

	@Override
	public String toString() {
		return nome;
	}
	
	
	

}
