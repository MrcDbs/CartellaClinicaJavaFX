package service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import dao.EseguiQuery;
import model.Farmaco;
import model.PatologiaFarmacoDTO;
import model.Patologia;
import model.PatologiaCura;
import model.Paziente;
import model.ResponseDTO;
import model.RicercaPazienteDTO;
import model.UserLoginDTO;
import model.Visita;

public class EventsService {
	
	private EseguiQuery repository;
	
	

	public EventsService(EseguiQuery repository) {
		super();
		this.repository = repository;
	}

	public ResponseDTO<?> login(UserLoginDTO user) {
		ResponseDTO<?> response = new ResponseDTO();
		try {
			response = this.repository.verificaLogin(user);
		} catch (SQLException e) {
			response.setDati(null);
			response.setEsito("ERROR");
			response.setMessage(e.getMessage());
			response.setStatusCode(500L);
			e.printStackTrace();
		}
		return response;
	}

	
	public ResponseDTO<?> salvaPaziente(Paziente paziente, boolean nuovo) {
		ResponseDTO<?> response = new ResponseDTO();
		try {
			response = this.repository.aggiornaPaziente(paziente, nuovo);
			
		} catch (SQLException e) {
			response.setDati(null);
			response.setEsito("ERROR");
			response.setMessage(e.getMessage());
			response.setStatusCode(500L);
			e.printStackTrace();
		}
		return response;
	}
	
	
	public ResponseDTO<Paziente> getListaPazienti(RicercaPazienteDTO ricerca) {
		ResponseDTO<Paziente> response = new ResponseDTO<Paziente>();
		
		try {
			List<Paziente> result = this.repository.ricercaPazienti(ricerca);
			//model = new ListModelDTO<PazienteDTO>(result);
			response.setDati(result);
			response.setEsito("OK");
			response.setMessage("Dati recuperati con successo");
			response.setStatusCode(200L);
		} catch (SQLException e) {
			response.setDati(null);
			response.setEsito("ERROR");
			response.setMessage(e.getMessage());
			response.setStatusCode(500L);
			e.printStackTrace();
		}
		return response;
		
	}
	
	public ResponseDTO<Patologia> getPatologiaList(){
		ResponseDTO<Patologia> response = new ResponseDTO<Patologia>();
		try {
			List<Patologia> result = this.repository.listaPatologia();
			response.setDati(result);
			response.setEsito("OK");
			response.setMessage("Dati recuperati con successo");
			response.setStatusCode(200L);
		} catch (SQLException e) {
			response.setDati(null);
			response.setEsito("ERROR");
			response.setMessage(e.getMessage());
			response.setStatusCode(500L);
			e.printStackTrace();
		}
		return response;
		
	}
	
	public ResponseDTO<Farmaco> getFarmacoList(){
		ResponseDTO<Farmaco> response = new ResponseDTO<Farmaco>();
		try {
			List<Farmaco> result = this.repository.listaFarmaco();
			response.setDati(result);
			response.setEsito("OK");
			response.setMessage("Dati recuperati con successo");
			response.setStatusCode(200L);
		} catch (SQLException e) {
			response.setDati(null);
			response.setEsito("ERROR");
			response.setMessage(e.getMessage());
			response.setStatusCode(500L);
			e.printStackTrace();
		}
		return response;
		
	}
	
	public ResponseDTO<PatologiaCura> getPatologiaCuraList(String cfPaziente){
		ResponseDTO<PatologiaCura> response = new ResponseDTO<PatologiaCura>();
		try {
			List<PatologiaCura> result = this.repository.listaPatologiaCura(cfPaziente);
			result.stream().forEach(rel -> {
				String nomeFarmaco = this.getFarmacoById(rel.getFarmaco().getId()).getFarmaco().getNome();
				String nomePatologia = this.getPatologiaById(rel.getPatologia().getId()).getPatologia().getNome();
				rel.getFarmaco().setNome(nomeFarmaco);
				rel.getPatologia().setNome(nomePatologia);
			});
			response.setDati(result);
			response.setEsito("OK");
			response.setMessage("Dati recuperati con successo");
			response.setStatusCode(200L);
		} catch (SQLException e) {
			response.setDati(null);
			response.setEsito("ERROR");
			response.setMessage(e.getMessage());
			response.setStatusCode(500L);
			e.printStackTrace();
		}
		return response;
		
	}
	
	public PatologiaFarmacoDTO getFarmacoById(Long idFarmaco) {
		PatologiaFarmacoDTO response = new PatologiaFarmacoDTO();
		try {
			Farmaco result = this.repository.getFarmacoById(idFarmaco);
			response.setFarmaco(result);
			response.setPatologia(null);
			response.setEsito("OK");
			response.setMessage("Dati recuperati con successo");
			response.setStatusCode(200L);
		} catch (SQLException e) {
			response.setFarmaco(null);
			response.setPatologia(null);
			response.setEsito("ERROR");
			response.setMessage(e.getMessage());
			response.setStatusCode(500L);
			e.printStackTrace();
		}
		return response;
	}
	
	public PatologiaFarmacoDTO getPatologiaById(Long idPatologia) {
		PatologiaFarmacoDTO response = new PatologiaFarmacoDTO();
		try {
			Patologia result = this.repository.getPatologiaById(idPatologia);
			response.setFarmaco(null);
			response.setPatologia(result);
			response.setEsito("OK");
			response.setMessage("Dati recuperati con successo");
			response.setStatusCode(200L);
		} catch (SQLException e) {
			response.setFarmaco(null);
			response.setPatologia(null);
			response.setEsito("ERROR");
			response.setMessage(e.getMessage());
			response.setStatusCode(500L);
			e.printStackTrace();
		}
		return response;
	}
	
	public ResponseDTO<Long> salvaVisita(Visita visita){
		ResponseDTO<Long> response = new ResponseDTO<Long>();
		try {
			response = this.repository.salvaVisita(visita);
			
		} catch (SQLException e) {
			response.setDati(null);
			response.setEsito("ERROR");
			response.setMessage(e.getMessage());
			response.setStatusCode(500L);
			e.printStackTrace();
		}
		return response;
	}
	
	public ResponseDTO<?> salvaRelPatologiaCura(PatologiaCura entity){
		ResponseDTO<?> response = new ResponseDTO();
		try {
			response = this.repository.salvaRelPatologiaCura(entity);
			
		} catch (SQLException e) {
			response.setDati(null);
			response.setEsito("ERROR");
			response.setMessage(e.getMessage());
			response.setStatusCode(500L);
			e.printStackTrace();
		}
		return response;
	}
	
	public boolean isDateValid(String dateString) {
		 DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	     if (!dateString.matches("^\\d{2}/\\d{2}/\\d{4}$")) {
	         return false; 
	     }

	     try {
	         LocalDate parsedDate = LocalDate.parse(dateString, dateFormatter);
	         // SE IL PARSE NON DA ERRORE, LA DATA E` VALIDA
	         return true;
	     } catch (DateTimeParseException e) {
	         return false;
	     }
	}
	
	public ResponseDTO<Visita> getStoricoVisite(String cfPaziente){
		ResponseDTO<Visita> response = new ResponseDTO<Visita>();
		try {
			response.setDati(this.repository.getStoricoVisite(cfPaziente));
			response.setEsito("ERROR");
			response.setMessage("Dati recuperati con successo");
			response.setStatusCode(200L);
			
		} catch (SQLException e) {
			response.setDati(null);
			response.setEsito("ERROR");
			response.setMessage(e.getMessage());
			response.setStatusCode(500L);
			e.printStackTrace();
		}
		return response;
	}
	
	public ResponseDTO<String> listaMedici(){
		ResponseDTO<String> response = new ResponseDTO<String>();
		try {
			response.setDati(this.repository.listaMedici());
			response.setEsito("ERROR");
			response.setMessage("Dati recuperati con successo");
			response.setStatusCode(200L);
			
		} catch (SQLException e) {
			response.setDati(null);
			response.setEsito("ERROR");
			response.setMessage(e.getMessage());
			response.setStatusCode(500L);
			e.printStackTrace();
		}
		return response;
	}
	
	public ResponseDTO<?> abilitaSostituto(String cfMedico, String dataDal, String dataAl) {
		ResponseDTO<?> response = new ResponseDTO();
		String cf = cfMedico.split("-")[0];
		try {
			this.repository.abilitaSostituto(cf, dataDal, dataAl);
			response.setEsito("OK");
			response.setMessage("Medico con Codice Fiscale " + cf + " abilitato");
			response.setStatusCode(200L);
			
		} catch (SQLException e) {
			response.setEsito("ERROR");
			response.setMessage(e.getMessage());
			response.setStatusCode(500L);
			e.printStackTrace();
		}
		return response;
		
		
	}

	public ResponseDTO<?> isCfValid(String cf) {
		ResponseDTO<?> response = new ResponseDTO();
		try {
			if(this.repository.verificaCf(cf)) {
				response.setEsito("OK");
				response.setMessage("Codice Fiscale valido");
				response.setStatusCode(200L);
			}else {
				response.setEsito("NOT FOUND");
				response.setMessage("Codice Fiscale non trovato o non valido");
				response.setStatusCode(404L);
			}
			
			
		} catch (SQLException e) {
			response.setEsito("ERROR");
			response.setMessage(e.getMessage());
			response.setStatusCode(500L);
			e.printStackTrace();
		}
		return response;
		
	}
	
	public ResponseDTO<?> updateSostitutoLogin(String cf, String user, String password){
		ResponseDTO<?> response = new ResponseDTO();
		try {
			this.repository.updateSostitutoLogin(cf, user, password);
			response.setEsito("OK");
			response.setMessage("Utente registrato");
			response.setStatusCode(200L);
			
		} catch (SQLException e) {
			response.setEsito("ERROR");
			response.setMessage(e.getMessage());
			response.setStatusCode(500L);
			e.printStackTrace();
		}
		return response;
	}
	
	
}
