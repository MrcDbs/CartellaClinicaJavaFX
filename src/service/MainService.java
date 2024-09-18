package service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import dao.DataAccessRepository;
import model.Farmaco;
import model.ListModelDTO;
import model.ModelPatologiaFarmacoDTO;
import model.Patologia;
import model.PatologiaCura;
import model.PazienteDTO;
import model.ResponseDTO;
import model.RicercaPazienteDTO;
import model.UserLoginDTO;

public class MainService {
	
	private DataAccessRepository repository;
	
	

	public MainService(DataAccessRepository repository) {
		super();
		this.repository = repository;
	}

	public ResponseDTO login(UserLoginDTO user) {
		ResponseDTO response = new ResponseDTO();
		response.setStatusCode(200L);
		return response;
	}

//	public ResponseDTO<?> login(UserLoginDTO user) {
//		ResponseDTO<?> response = new ResponseDTO();
//		try {
//			response = this.repository.login(user);
//		} catch (SQLException e) {
//			response.setData(null);
//			response.setEsito("ERROR");
//			response.setMessage(e.getMessage());
//			response.setStatusCode(500L);
//			e.printStackTrace();
//		}
//		return response;
//	}
	
//	public ResponseDTO salvaPaziente(PazienteDTO paziente) {
//		ResponseDTO response = new ResponseDTO();
//		response.setStatusCode(200L);
//		response.setMessage("Paziente salvato correttamente");
//		return response;
//	}
	
	public ResponseDTO<?> salvaPaziente(PazienteDTO paziente, boolean nuovo) {
		ResponseDTO<?> response = new ResponseDTO();
		try {
			if(nuovo) {
				response = this.repository.salvaPaziente(paziente);
			}else {
				response = this.repository.modificaPaziente(paziente);
			}
			
			System.out.println("response salva paziente" + response.toString());
		} catch (SQLException e) {
			response.setData(null);
			response.setEsito("ERROR");
			response.setMessage(e.getMessage());
			response.setStatusCode(500L);
			e.printStackTrace();
		}
		return response;
	}
	
	
	public ResponseDTO<PazienteDTO> getListaPazienti(RicercaPazienteDTO ricerca) {
		ResponseDTO<PazienteDTO> response = new ResponseDTO<PazienteDTO>();
		
		try {
			List<PazienteDTO> result = this.repository.getPazienti(ricerca);
			//model = new ListModelDTO<PazienteDTO>(result);
			response.setData(result);
			response.setEsito("OK");
			response.setMessage("Dati recuperati con successo");
			response.setStatusCode(200L);
		} catch (SQLException e) {
			response.setData(null);
			response.setEsito("ERROR");
			response.setMessage(e.getMessage());
			response.setStatusCode(500L);
			e.printStackTrace();
		}
		System.out.println(response.toString());
		return response;
		
	}
	
	public ResponseDTO<Patologia> getPatologiaList(){
		ResponseDTO<Patologia> response = new ResponseDTO<Patologia>();
		try {
			List<Patologia> result = this.repository.listaPatologia();
			response.setData(result);
			response.setEsito("OK");
			response.setMessage("Dati recuperati con successo");
			response.setStatusCode(200L);
		} catch (SQLException e) {
			response.setData(null);
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
			response.setData(result);
			response.setEsito("OK");
			response.setMessage("Dati recuperati con successo");
			response.setStatusCode(200L);
		} catch (SQLException e) {
			response.setData(null);
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
			response.setData(result);
			response.setEsito("OK");
			response.setMessage("Dati recuperati con successo");
			response.setStatusCode(200L);
		} catch (SQLException e) {
			response.setData(null);
			response.setEsito("ERROR");
			response.setMessage(e.getMessage());
			response.setStatusCode(500L);
			e.printStackTrace();
		}
		return response;
		
	}
	
	public ModelPatologiaFarmacoDTO getFarmacoById(Long idFarmaco) {
		ModelPatologiaFarmacoDTO response = new ModelPatologiaFarmacoDTO();
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
	
	public ModelPatologiaFarmacoDTO getPatologiaById(Long idPatologia) {
		ModelPatologiaFarmacoDTO response = new ModelPatologiaFarmacoDTO();
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
}
