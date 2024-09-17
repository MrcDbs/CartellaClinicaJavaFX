package service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dao.DataAccessRepository;
import model.ListModelDTO;
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

//	public ResponseDTO login(UserLoginDTO user) {
//		ResponseDTO response = new ResponseDTO();
//		response.setStatusCode(200L);
//		return response;
//	}

	public ResponseDTO<?> login(UserLoginDTO user) {
		ResponseDTO<?> response = new ResponseDTO();
		try {
			response = this.repository.login(user);
		} catch (SQLException e) {
			response.setData(null);
			response.setEsito("ERROR");
			response.setMessage(e.getMessage());
			response.setStatusCode(500L);
			e.printStackTrace();
		}
		return response;
	}
	
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
}
