package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import model.Farmaco;
import model.Patologia;
import model.PatologiaCura;
import model.PazienteDTO;
import model.ResponseDTO;
import model.RicercaPazienteDTO;
import model.UserLoginDTO;

public class DataAccessRepository {
	private DataBaseConnection dataBaseConnection;

    public DataAccessRepository() {
        this.dataBaseConnection = new DataBaseConnection();
    }

//    public void addUser(String username, String password) throws SQLException {
//        String query = "INSERT INTO users (username, password) VALUES (?, ?)";
//        try (Connection conn = dataBaseConnection.getConnection();
//             PreparedStatement pstmt = conn.prepareStatement(query)) {
//            pstmt.setString(1, username);
//            pstmt.setString(2, password);
//            pstmt.executeUpdate();
//        }
//    }
    

    // RICERCA PAZIENTE - RICERCA PAZIENTI CON FILTRI
    
    public List<PazienteDTO> getPazienti(RicercaPazienteDTO ricercaDTO) throws SQLException {
    	 List<PazienteDTO> pazientiList = new ArrayList<>();
    	 StringBuilder queryBuilder = new StringBuilder("SELECT * FROM paziente");
    	 List<String> conditions = new ArrayList<>();
	     List<String> patologiaConditions = new ArrayList<>();
    	 if (ricercaDTO != null) {
    		 
    	        if (ricercaDTO.getCodiceFiscale() != null && !ricercaDTO.getCodiceFiscale().isEmpty()) {
    	            conditions.add("codice_fiscale = ?");
    	            patologiaConditions.add("rel_patologia_cura.cf_paziente = ?");
    	        }
    	        if (ricercaDTO.getCognome() != null && !ricercaDTO.getCognome().isEmpty()) {
    	            conditions.add("cognome = ?");
    	            patologiaConditions.add("paziente.cognome =?");
    	        }
    	        if (ricercaDTO.getNome() != null && !ricercaDTO.getNome().isEmpty()) {
    	            conditions.add("nome = ?");
    	            patologiaConditions.add("paziente.nome =?");
    	        }
    	        if (ricercaDTO.getPatologia() != null && !ricercaDTO.getPatologia().isEmpty()) {
    	            //conditions.add("patologia = ?");
    	        	
    	        	queryBuilder.append(" INNER JOIN rel_patologia_cura ON paziente.codice_fiscale = rel_patologia_cura.cf_paziente"
    	            		+ " INNER JOIN patologia ON patologia.id = rel_patologia_cura.patologia");
    	        	if (patologiaConditions.isEmpty()) {
    	        		
    	        		queryBuilder.append(" WHERE patologia.nome = ?");
    	        		
    	        	}else {
    	        		patologiaConditions.add("patologia.nome = ?");
    	        		queryBuilder.append(" WHERE ");
        	            queryBuilder.append(String.join(" AND ", patologiaConditions));
    	        	}
    	        	
    	        }else {
    	        	 if (!conditions.isEmpty()) {
    	    	            queryBuilder.append(" WHERE ");
    	    	            queryBuilder.append(String.join(" AND ", conditions));
    	    	        }
    	        }
    	 }
    	 String query = queryBuilder.toString();
    	 
    	 try (Connection conn = dataBaseConnection.getConnection();
    	         PreparedStatement pstmt = conn.prepareStatement(query)) {
    	        
    	        // SETTAGGIO PARAMETRI DINAMICAMENTE
    	        if (ricercaDTO != null) {
    	            int paramIndex = 1;
    	            if (ricercaDTO.getCodiceFiscale() != null && !ricercaDTO.getCodiceFiscale().isEmpty()) {
    	                pstmt.setString(paramIndex++, ricercaDTO.getCodiceFiscale());
    	            }
    	            if (ricercaDTO.getCognome() != null && !ricercaDTO.getCognome().isEmpty()) {
    	                pstmt.setString(paramIndex++, ricercaDTO.getCognome());
    	            }
    	            if (ricercaDTO.getNome() != null && !ricercaDTO.getNome().isEmpty()) {
    	                pstmt.setString(paramIndex++, ricercaDTO.getNome());
    	            }
    	            if (ricercaDTO.getPatologia() != null && !ricercaDTO.getPatologia().isEmpty()) {
    	            	if(conditions.isEmpty()) {
    	            		pstmt.setString(1, ricercaDTO.getPatologia());
    	            	}else {
    	            		pstmt.setString(paramIndex++, ricercaDTO.getPatologia());
    	            	}
    	            }
    	        }
    	        
    	        try (ResultSet rs = pstmt.executeQuery()) {
    	            while (rs.next()) {
    	            	Date sqlDate = rs.getDate("data_nascita");
    	                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    	                String formattedDate = dateFormat.format(sqlDate);
    	            	pazientiList.add(new PazienteDTO(
    	            			rs.getString("codice_fiscale"),
    	                        rs.getString("cognome"),
    	                        rs.getString("nome"),
    	            			formattedDate,
    	            			rs.getString("luogo_nascita"),
    	            			rs.getString("residenza"),
    	            			rs.getString("via"),
    	            			rs.getString("occupazione"),
    	            			rs.getString("telefono"),
    	            			rs.getString("sesso").charAt(0)));
    	            }
    	        }
    	    }
        
        return pazientiList;
    }
    
    public ResponseDTO<?> login(UserLoginDTO user) throws SQLException {
    	ResponseDTO<?> response = new ResponseDTO();
        String query = "SELECT * FROM Medico WHERE username = ?";

        try (Connection conn = dataBaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, user.getUsername());

            try (ResultSet rs = pstmt.executeQuery()) {

                if (!rs.next()) {
                    response.setMessage("Nessun medico trovato per questo username.");
                    response.setEsito("ERROR");
                    response.setStatusCode(404L);
                    return response; 
                }

                String storedPassword = rs.getString("password");

                if (storedPassword.equals(user.getPassword())) {
                    response.setMessage(rs.getString("cognome") + " " + rs.getString("nome") + " ha\neffettuato l'accesso");
                    response.setEsito("OK");
                    response.setStatusCode(200L);
                } else {
                    response.setMessage("Password errata per questo username.");
                    response.setEsito("ERROR");
                    response.setStatusCode(403L); 
                }
            }
        }

        return response;
    	
    }
    
    public ResponseDTO<?> salvaPaziente(PazienteDTO paziente) throws SQLException {
    	ResponseDTO<?> response = new ResponseDTO();
    	
    	String query = "INSERT INTO paziente (cognome, nome, codice_fiscale, telefono, via, residenza, data_nascita, "
    			+ "luogo_nascita, sesso, occupazione) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    	try (Connection conn = dataBaseConnection.getConnection();
    			PreparedStatement pstmt = conn.prepareStatement(query)) {
    	    pstmt.setString(1, paziente.getCognome());
    	    pstmt.setString(2, paziente.getNome());
    	    pstmt.setString(3, paziente.getCodiceFiscale());
    	    pstmt.setString(4, paziente.getNumeroTelefono());
    	    pstmt.setString(5, paziente.getVia());
    	    pstmt.setString(6, paziente.getResidenza());
    	    
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

            LocalDate localDate = LocalDate.parse(paziente.getDataNascita(), formatter);
    	    
    	    pstmt.setDate(7, Date.valueOf(localDate));
    	    pstmt.setString(8, paziente.getLuogoNascita());
    	    pstmt.setString(9, String.valueOf(paziente.getSesso()));
    	    pstmt.setString(10, paziente.getOccupazione());
    	    
    	    int rowsAffected = pstmt.executeUpdate();
    	    if(rowsAffected > 0) {
    	    	response.setEsito("OK");
    	    	response.setMessage("Paziente salvato correttamente");
    	    	response.setStatusCode(200L);
    	    }else {
    	    	response.setEsito("ERROR");
    	    	response.setMessage("Salvataggio non riuscito");
    	    	response.setStatusCode(401L);
    	    }
    	} 
		return response;
    }
    
    public ResponseDTO<?> modificaPaziente(PazienteDTO paziente) throws SQLException {
    	ResponseDTO<?> response = new ResponseDTO();
    	String updateQuery = "UPDATE paziente SET cognome = ?, nome = ?, telefono = ?, via = ?, residenza = ?, "
                + "data_nascita = ?, luogo_nascita = ?, sesso = ?, occupazione = ? WHERE codice_fiscale = ?";
    	try (Connection conn = dataBaseConnection.getConnection();
    			PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
    	    pstmt.setString(1, paziente.getCognome());
    	    pstmt.setString(2, paziente.getNome());
    	    pstmt.setString(3, paziente.getNumeroTelefono());
    	    pstmt.setString(4, paziente.getVia());
    	    pstmt.setString(5, paziente.getResidenza());
    	    
    	    
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyy");

            LocalDate localDate = LocalDate.parse(paziente.getDataNascita(), formatter);
    	    
    	    pstmt.setDate(6, Date.valueOf(localDate));
    	    pstmt.setString(7, paziente.getLuogoNascita());
    	    pstmt.setString(8, String.valueOf(paziente.getSesso()));
    	    pstmt.setString(9, paziente.getOccupazione());
    	    pstmt.setString(10, paziente.getCodiceFiscale());
    	    
    	    int rowsAffected = pstmt.executeUpdate();
    	    if(rowsAffected > 0) {
    	    	response.setEsito("OK");
    	    	response.setMessage("Paziente salvato correttamente");
    	    	response.setStatusCode(200L);
    	    }else {
    	    	response.setEsito("ERROR");
    	    	response.setMessage("Salvataggio non riuscito");
    	    	response.setStatusCode(401L);
    	    }
    	} 
		return response;
	
    }
    
    public List<PatologiaCura> listaPatologiaCura(String cfPaziente) throws SQLException{
    	List<PatologiaCura> patologiaCuraList = new ArrayList<>();
   	    String query = "SELECT * FROM rel_patologia_cura WHERE cf_paziente = ?";
	   	 try (Connection conn = dataBaseConnection.getConnection();
		         PreparedStatement pstmt = conn.prepareStatement(query)) {
	   		pstmt.setString(1, cfPaziente);
	   		try (ResultSet rs = pstmt.executeQuery()) {
	            while (rs.next()) {
	            	patologiaCuraList.add(new PatologiaCura(
	            			rs.getString("da"),
	            			rs.getString("a"),
	            			rs.getString("cf_paziente"),
	            			Long.valueOf(rs.getInt("patologia")),
	            			Long.valueOf(rs.getInt("farmaco")),
	            			Long.valueOf(rs.getInt("id_visita")),null,null));
	            	
	            }
	            return patologiaCuraList;
	   		}
	   	 }
    }
    
    public List<Patologia> listaPatologia() throws SQLException{
    	List<Patologia> patologiaList = new ArrayList<>();
    	String query = "SELECT * FROM patologia";
    	try (Connection conn = dataBaseConnection.getConnection();
		         PreparedStatement pstmt = conn.prepareStatement(query)) {
	   		ResultSet rs = pstmt.executeQuery();
	   		while (rs.next()) {
	   			patologiaList.add(new Patologia(
	   					Long.valueOf(rs.getInt("id")),
	   					rs.getString("nome"),
	   					rs.getString("cause"),
	   					rs.getString("effetti")));
	   		}
	   		return patologiaList;
	   		
    	}
    }
    
    public List<Farmaco> listaFarmaco() throws SQLException{
    	List<Farmaco> farmacoList = new ArrayList<>();
    	String query = "SELECT * FROM farmaco";
    	try (Connection conn = dataBaseConnection.getConnection();
		         PreparedStatement pstmt = conn.prepareStatement(query)) {
	   		ResultSet rs = pstmt.executeQuery();
	   		while (rs.next()) {
	   			farmacoList.add(new Farmaco(
	   					Long.valueOf(rs.getInt("id")),
	   					rs.getString("nome"),
	   					rs.getString("principio_attivo")));
	   		}
	   		return farmacoList;
	   		
    	}
    }
    
    public Farmaco getFarmacoById(Long idFarmaco) throws SQLException {
    	Farmaco farmaco = new Farmaco();
    	String query = "SELECT * FROM farmaco WHERE id = ?";
    	try (Connection conn = dataBaseConnection.getConnection();
		         PreparedStatement pstmt = conn.prepareStatement(query)) {
	   		pstmt.setInt(1, idFarmaco.intValue());
	   		try (ResultSet rs = pstmt.executeQuery()) {
	            while (rs.next()) {
	            	farmaco.setId(rs.getLong("id"));
	            	farmaco.setNome(rs.getString("nome"));
	            	farmaco.setPrincipioAttivo(rs.getString("principio_attivo"));
	            	
	            }
	            return farmaco;
	   		}
	   	 }
	   		
   	}
    	
    
    
    public Patologia getPatologiaById(Long idPatologia) throws SQLException {
    	Patologia patologia = new Patologia();
    	String query = "SELECT * FROM patologia WHERE id = ?";
    	try (Connection conn = dataBaseConnection.getConnection();
		         PreparedStatement pstmt = conn.prepareStatement(query)) {
	   		pstmt.setInt(1, idPatologia.intValue());
	   		try (ResultSet rs = pstmt.executeQuery()) {
	            while (rs.next()) {
	            	patologia.setId(rs.getLong("id"));
	            	patologia.setNome(rs.getString("nome"));
	            	patologia.setCause(rs.getString("cause"));
	            	patologia.setEffetti(rs.getString("effetti"));
	            	
	            }
	            return patologia;
	   		}
	   	 }
    }

}
