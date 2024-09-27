package mainApplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import dao.EseguiQuery;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Farmaco;
import model.ListModelDTO;
import model.Medico;
import model.ModelPatologiaFarmacoDTO;
import model.Patologia;
import model.PatologiaCura;
import model.Paziente;
import model.ResponseDTO;
import model.RicercaPazienteDTO;
import model.UserLoginDTO;
import model.Visita;
import service.EventsService;

public class CartellaClinicaMain extends Application{
	 //implements EventHandler<ActionEvent>
	private EventsService eventService;
	private Button loginButton;
	private MenuItem loginItem;
	private boolean userLoggedIn = false;
	private String cfMedicoLoggato;
	private Label titleLabel;
	private Paziente pazienteSelezionato = null;
	private Menu fileMenu;
	private Menu pazienteMenu;
	private Menu sostitutoMenu;
	private Menu questionMark;
	private MenuItem accessoItem;
    private MenuItem logoutItem;
    private MenuItem ricercaPaziente;
    private MenuItem inserisciPaziente;
    private MenuItem dbItem;
    private MenuItem exitItem; 
    private boolean tastiRicercaTabDisabled;
	private TableView<Paziente> tableRicercaPaziente;
	private PatologiaCura modelForm = null;
	private Button impostaButton;
	private boolean cfPrimoAccessoValido;
	
	private List<String> listaCfMedici;
	private ObservableList<Paziente> data;
	private List<Paziente> pazientiRicercaList = new ArrayList<>();
	private List<PatologiaCura> relPatologiaCuraList = new ArrayList<>();
	
	private Map<String, Stage> stageList = new HashMap<String, Stage>();
	private Map<String, Button> buttonList = new HashMap<String, Button>();
	private Map<String, MenuItem> menuItemList = new HashMap<String, MenuItem>();
	
	
	
	public static void main(String[] args) {
		launch(args);

	}

	@Override
	public void start(Stage arg0) throws Exception {
		this.eventService = new EventsService(new EseguiQuery());
		this.stageList.put("mainStage", arg0);
		arg0.setTitle("Cartella clinica");
		
		MenuBar menuBar = new MenuBar();
		
	   fileMenu = new Menu("File");
       pazienteMenu = new Menu("Paziente");
       sostitutoMenu = new Menu("Sostituto");
       questionMark = new Menu("?");
        
        pazienteMenu.setDisable(!this.userLoggedIn);
        sostitutoMenu.setDisable(!this.userLoggedIn);
        questionMark.setDisable(!this.userLoggedIn);
        // File Menu Items
        loginItem = new MenuItem("Login");
        accessoItem = new MenuItem("Primo accesso");
        logoutItem = new MenuItem("Logout");
        dbItem = new MenuItem("Connessione DB");
        exitItem = new MenuItem("Esci");
        
        logoutItem.setDisable(!this.userLoggedIn);
        // Paziente menu items
        ricercaPaziente = new MenuItem("Ricerca paziente");
        inserisciPaziente = new MenuItem("Inserisci paziente");
        this.menuItemList.put("InserisciPazienteItem", inserisciPaziente);
        
        MenuItem abilitaSostitutoMenu = new MenuItem("Abilita sostituto");
        
        sostitutoMenu.getItems().addAll(abilitaSostitutoMenu);
        // Adding actions to menu items (optional)
        exitItem.setOnAction(e -> arg0.close()); // Close app when 'Exit' is clicked
        loginItem.setOnAction(e -> {
        	this.finestraLogin();
        });
        inserisciPaziente.setOnAction(e -> {
        	this.finestraInserimentoPaziente(true, null);
        });
        ricercaPaziente.setOnAction(e -> {
        	this.finestraCercaPaziente();
        });
        
        abilitaSostitutoMenu.setOnAction(e -> {
        	this.abilitaSostituto();
        });
        accessoItem.setOnAction(e -> {
        	this.finestraPrimoAccesso();
        });
        
        fileMenu.getItems().addAll(loginItem, accessoItem, logoutItem, dbItem, exitItem);
        pazienteMenu.getItems().addAll(ricercaPaziente, inserisciPaziente);
     
        menuBar.getMenus().addAll(fileMenu, pazienteMenu, sostitutoMenu, questionMark);
        StackPane layout = new StackPane();
        
        titleLabel = new Label("Gestione Cartella Clinica");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        layout.getChildren().add(titleLabel);
        StackPane.setAlignment(titleLabel, Pos.TOP_CENTER);
        BorderPane root = new BorderPane();
        root.setTop(menuBar);
        
        logoutItem.setOnAction(e -> {
        	this.userLoggedIn = false;
        	pazienteMenu.setDisable(!this.userLoggedIn);
            sostitutoMenu.setDisable(!this.userLoggedIn);
            questionMark.setDisable(!this.userLoggedIn);
            logoutItem.setDisable(!this.userLoggedIn);
        	StackPane stack = (StackPane)root.getCenter();
        	this.showTemporaryAlert(stack, "Logged Out", 3000);
        });
        
		Scene scene = new Scene(root, 475, 500);
		root.setCenter(layout);
		arg0.setScene(scene);
		arg0.show();
		
	}
	
	private void finestraInserimentoPaziente(boolean nuovoPaziente, Paziente pazienteDTO) {
		VBox vbox = new VBox(10);
		Button salvaPaziente = new Button("Aggiorna");
		this.buttonList.put("salvaPazienteButton", salvaPaziente);
		
		
		Label codiceFiscale = new Label("*Codice Fiscale:");
		codiceFiscale.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");
        Label cognome = new Label("*Cognome:");
        cognome.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");
        Label nome = new Label("*Nome:");
        nome.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");
        Label dataNascita = new Label("Data di Nascita:");
        dataNascita.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");
        Label luogoNascita = new Label("Luogo di Nascita:");
        luogoNascita.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");
        Label residenza = new Label("Residenza:");
        residenza.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");
        Label via = new Label("Via:");
        via.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");
        Label occupazione = new Label("Occupazione:");
        occupazione.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");
        Label numeroTelefono = new Label("Numero di Telefono:");
        numeroTelefono.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");
        Label sesso = new Label("Sesso:");
        sesso.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");
        
        Stage stage = new Stage();
        this.stageList.put("inserimentoPazienteStage", stage);
        stage.setTitle("Inserimento Paziente");
        
        StackPane layout = new StackPane();
        Label titoloModale = new Label("Inserimento dati paziente");
      
        BorderPane.setAlignment(titoloModale, Pos.TOP_CENTER);
        
        titoloModale.setStyle("-fx-font-size: 24px;");
        BorderPane root = new BorderPane();
        root.setTop(titoloModale);
        
        // Create input fields
        TextField codiceFiscaleField = new TextField();
        codiceFiscaleField.setDisable(!nuovoPaziente);
        TextField cognomeField = new TextField();
        TextField nomeField = new TextField();
        TextField dataNascitaField = new TextField();
        TextField luogoNascitaField = new TextField();
        TextField residenzaField = new TextField();
        TextField viaField = new TextField();
        TextField occupazioneField = new TextField();
        TextField numeroTelefonoField = new TextField();
        
        RadioButton sessoFieldM = new RadioButton("M");
        RadioButton sessoFieldF = new RadioButton("F");
        
        dataNascitaField.setPromptText("dd/MM/yyyy");
        ToggleGroup group = new ToggleGroup();
        sessoFieldM.setSelected(true);
        sessoFieldM.setToggleGroup(group);
        sessoFieldF.setToggleGroup(group);
        
        if(!nuovoPaziente) {
        	codiceFiscaleField.setText(pazienteDTO.getCodiceFiscale());;
            cognomeField.setText(pazienteDTO.getCognome());
            nomeField.setText(pazienteDTO.getNome());
            dataNascitaField.setText(pazienteDTO.getDataNascita());
            luogoNascitaField.setText(pazienteDTO.getLuogoNascita());
            residenzaField.setText(pazienteDTO.getResidenza());
            viaField.setText(pazienteDTO.getVia());
            occupazioneField.setText(pazienteDTO.getOccupazione());
            numeroTelefonoField.setText(pazienteDTO.getNumeroTelefono());
            if(pazienteDTO.getSesso() == 'M') {
            	sessoFieldM.setSelected(true);
            	sessoFieldF.setSelected(false);
            }else {
            	sessoFieldM.setSelected(false);
            	sessoFieldF.setSelected(true);
            }
        }

        
        salvaPaziente.setOnAction(e ->{
        	if(codiceFiscaleField.getText().equals("") || cognomeField.getText().equals("") || nomeField.getText().equals("")) {
        		this.showAlertWithMessage("Devi inserire i campi obbligatori!", AlertType.WARNING);
        	}else if(!this.eventService.isDateValid(dataNascitaField.getText())) {
        		this.showAlertWithMessage("La data non è valida o non è nel formato corretto", AlertType.WARNING);
        	}else {
        		Paziente paziente = new Paziente();
    			paziente.setCodiceFiscale(codiceFiscaleField.getText());
    			paziente.setCognome(cognomeField.getText());
    			paziente.setNome(nomeField.getText());
    			paziente.setDataNascita(dataNascitaField.getText());
    			paziente.setLuogoNascita(luogoNascitaField.getText());
    			paziente.setResidenza(residenzaField.getText());
    			paziente.setVia(viaField.getText());
    			paziente.setOccupazione(occupazioneField.getText());
    			paziente.setNumeroTelefono(numeroTelefonoField.getText());
    			RadioButton selectedRadioButton = (RadioButton) group.getSelectedToggle();
    			paziente.setSesso(selectedRadioButton.getText().charAt(0));
    			ResponseDTO<?> response = this.eventService.salvaPaziente(paziente, nuovoPaziente);
    			if(response.getStatusCode() == 200) {
    				if(!nuovoPaziente) {
    					this.ricercaPazienti(null, this.tableRicercaPaziente);
    				}
    				
    				this.stageList.get("inserimentoPazienteStage").close();
    				BorderPane rootPane = (BorderPane) this.stageList.get("mainStage").getScene().getRoot();
                    StackPane stack = (StackPane)rootPane.getCenter();
                    this.showTemporaryAlert(stack, response.getMessage(), 3000);
    			}
        	}
			
			
		});
        
        HBox row1 = new HBox(10, codiceFiscale, codiceFiscaleField);
        row1.setAlignment(Pos.CENTER_RIGHT);
        HBox row2 = new HBox(10, cognome, cognomeField);
        row2.setAlignment(Pos.CENTER_RIGHT);
        HBox row3 = new HBox(10, nome, nomeField);
        row3.setAlignment(Pos.CENTER_RIGHT);
        HBox row4 = new HBox(10, dataNascita, dataNascitaField);
        row4.setAlignment(Pos.CENTER_RIGHT);
        HBox row5 = new HBox(10, luogoNascita, luogoNascitaField);
        row5.setAlignment(Pos.CENTER_RIGHT);
        HBox row6 = new HBox(10, residenza, residenzaField);
        row6.setAlignment(Pos.CENTER_RIGHT);
        HBox row7 = new HBox(10, via, viaField);
        row7.setAlignment(Pos.CENTER_RIGHT);
        HBox row8 = new HBox(10, occupazione, occupazioneField);
        row8.setAlignment(Pos.CENTER_RIGHT);
        HBox row9 = new HBox(10, numeroTelefono,numeroTelefonoField);
        row9.setAlignment(Pos.CENTER_RIGHT);
        HBox row10 = new HBox(10, sesso, sessoFieldM, sessoFieldF);
        row10.setAlignment(Pos.CENTER_RIGHT);
        
        vbox.getChildren().addAll(row1, row2, row3, row4,
        		row5, row6, row7, row8, row9, row10);
        
        vbox.setMaxWidth(330);   
        vbox.setMaxHeight(400); 
        
        vbox.setPadding(new Insets(20)); 
        vbox.setAlignment(Pos.CENTER);  
        
        root.setCenter(vbox);
        layout.getChildren().add(salvaPaziente);
		layout.setPadding(new Insets(0, 0, 20, 0));
		root.setBottom(layout);
        Scene scene = new Scene(root, 430, 475);
		root.setBottom(layout);
		stage.setScene(scene);
		stage.show();
		
		
	}

	private void finestraLogin() {
		Label nameLabel = new Label("Username:");
        Label passwordLabel = new Label("Password:");
        loginButton  = new Button();

        TextField nameField = new TextField();
        PasswordField passwordField = new PasswordField(); 
        
     
        nameField.setPromptText("Enter your name");
        passwordField.setPromptText("Enter your password");
        
    
        loginButton.setOnAction(e -> {
            String username = nameField.getText();
            String password = passwordField.getText();

            //ResponseDTO<?> res = this.eventService.controlloDateSostituto(username, password);
            UserLoginDTO user = new UserLoginDTO();
            user.setUsername(username);
            user.setPassword(password);
            ResponseDTO<?> response = this.eventService.login(user);
            if(response.getStatusCode() != 200L) {
            	if(response.getStatusCode() != 500L) {
            		this.showAlertWithMessage(response.getMessage(), AlertType.WARNING);
            	}
            }else {
            	this.userLoggedIn = true;
            	this.stageList.get("loginStage").close();

            	this.cfMedicoLoggato = (String)response.getData().get(0);
            	pazienteMenu.setDisable(!this.userLoggedIn);
                sostitutoMenu.setDisable(!this.userLoggedIn);
                questionMark.setDisable(!this.userLoggedIn);
                logoutItem.setDisable(!this.userLoggedIn);
                
                BorderPane root = (BorderPane) this.stageList.get("mainStage").getScene().getRoot();
                StackPane stack = (StackPane)root.getCenter();
                this.showTemporaryAlert(stack, "Accesso effettuato", 3000);

                
            }
        });
        VBox vbox = new VBox(10); 
        vbox.getChildren().addAll(nameLabel, nameField, passwordLabel, passwordField);
        
        vbox.setPadding(new Insets(20, 20, 20, 20));

        
        BorderPane root = new BorderPane();
        root.setCenter(vbox);
        
		Stage arg1 = new Stage();
		this.stageList.put("loginStage", arg1);
		arg1.setTitle("Parametri Login");
		
		
		loginButton.setText("Login");
		
		StackPane layout = new StackPane();
		layout.getChildren().add(loginButton);
		layout.setPadding(new Insets(0, 0, 20, 0));
		root.setBottom(layout);
		Scene scene = new Scene(root, 300, 210);
		arg1.setScene(scene);
		arg1.show();
		
	}
	
	private void showAlertWithMessage(String message, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle("Messaggio");
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
	}
	
	private void showTemporaryAlert(StackPane parent, String message, int durationMillis) {
        Label alertLabel = new Label(message);
        alertLabel.setStyle("-fx-background-color: lightgreen; -fx-border-color: black; -fx-padding: 10px;");

        parent.getChildren().add(alertLabel);

        PauseTransition pause = new PauseTransition(Duration.millis(durationMillis));
        pause.setOnFinished(event -> parent.getChildren().remove(alertLabel));
        pause.play();
    }
	
	private void finestraCercaPaziente() {
		this.tastiRicercaTabDisabled = true;
		this.pazientiRicercaList.clear();
		Button ricercaPaziente = new Button("Cerca");
		Button cartellaClinica = new Button("Cartella Clinica");
		Button datiAnagrafici = new Button("Dati Anagrafici");
		cartellaClinica.setDisable(this.tastiRicercaTabDisabled);
		datiAnagrafici.setDisable(this.tastiRicercaTabDisabled);
		this.buttonList.put("ricercaPazienteButton", ricercaPaziente);
		this.buttonList.put("cartellaClinicaButton", cartellaClinica);
		this.buttonList.put("datiAnagraficiButton", datiAnagrafici);
		
		Stage stage = new Stage();
        this.stageList.put("ricercaPazienteStage", stage);
        stage.setTitle("Ricerca");
        
        Label titoloModale = new Label("Ricerca paziente");
        BorderPane.setAlignment(titoloModale, Pos.TOP_CENTER);
        
        titoloModale.setStyle("-fx-font-size: 24px;");
        BorderPane root = new BorderPane();
        root.setTop(titoloModale);
        
        Label codiceFiscale = new Label("Codice Fiscale:");
		codiceFiscale.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");
        Label cognome = new Label("Cognome:");
        cognome.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");
        Label nome = new Label("Nome:");
        nome.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");
        
        Label patologia = new Label("Patologia:");
		patologia.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");
		
		TextField codiceFiscaleField = new TextField();
		codiceFiscaleField.setId("codiceFiscaleField");
        TextField cognomeField = new TextField();
        cognomeField.setId("cognomeField");
        TextField nomeField = new TextField();
        nomeField.setId("nomeField");
        TextField patologiaField = new TextField();
        patologiaField.setId("patologiaField");
		
        
        
        Region spacer = new Region();
        
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        HBox row1 = new HBox(10, codiceFiscale, codiceFiscaleField);
        row1.setAlignment(Pos.CENTER_RIGHT);
        HBox row2 = new HBox(10, cognome, cognomeField);
        row2.setAlignment(Pos.CENTER_RIGHT);
        HBox row3 = new HBox(10, nome, nomeField);
        row3.setAlignment(Pos.CENTER_RIGHT);
        HBox row4 = new HBox(10, patologia, patologiaField);
        row4.setAlignment(Pos.CENTER_RIGHT);
       
        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(row1, row2, row3, row4, ricercaPaziente);
        
        vbox.setMaxWidth(400);   
        vbox.setMaxHeight(400); 
        
        vbox.setPadding(new Insets(20));
        vbox.setAlignment(Pos.CENTER);  
        
        
		
		// ############## TABLE ################
		
		TableView<Paziente> table = new TableView<>();
		table.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
		
		
		TableColumn<Paziente, Integer> codiceColumn = new TableColumn<>("Codice Fiscale");
        codiceColumn.setCellValueFactory(new PropertyValueFactory<>("codiceFiscale"));
        codiceColumn.setPrefWidth(140);
		TableColumn<Paziente, String> cognomeColumn = new TableColumn<>("Cognome");
		cognomeColumn.setCellValueFactory(new PropertyValueFactory<>("cognome"));
		cognomeColumn.setPrefWidth(120);
        TableColumn<Paziente, String> nomeColumn = new TableColumn<>("Nome");
        nomeColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));
        nomeColumn.setPrefWidth(95);

        table.getColumns().add(codiceColumn);
        table.getColumns().add(cognomeColumn);
        table.getColumns().add(nomeColumn);
        
		
        this.tableRicercaPaziente = table;
        ricercaPaziente.setOnAction(e -> {
        	RicercaPazienteDTO ricerca = new RicercaPazienteDTO();
        	ricerca.setCodiceFiscale(codiceFiscaleField.getText());
    		ricerca.setCognome(cognomeField.getText());
    		ricerca.setNome(nomeField.getText());
    		ricerca.setPatologia(patologiaField.getText());
        	this.ricercaPazienti(ricerca, table);
        	this.pazienteSelezionato = null;
        	this.tastiRicercaTabDisabled = false;
        	cartellaClinica.setDisable(false);
    		datiAnagrafici.setDisable(false);
        	
        });
        
        this.data = FXCollections.observableArrayList(
        		this.pazientiRicercaList
        );

        table.setItems(this.data);
        
        
        // PER IL CLICK DELLA ROW
        table.setRowFactory(tv -> {
            TableRow<Paziente> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getClickCount() == 1) {
                    this.pazienteSelezionato = row.getItem();
                }
            });
            return row;
        });
        
        HBox rowButtons = new HBox(20, cartellaClinica, datiAnagrafici);
        rowButtons.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(table, rowButtons);
        root.setCenter(vbox);
        Scene scene = new Scene(root, 400, 420);
        datiAnagrafici.setOnAction(e -> {
        	if(this.pazienteSelezionato != null) {
        		this.finestraInserimentoPaziente(false, this.pazienteSelezionato);
        	}else {
        		this.showAlertWithMessage("E' necessario selezionare almeno una riga", AlertType.INFORMATION);
        	}
        	
        });
        cartellaClinica.setOnAction(e -> {
        	if(this.pazienteSelezionato != null) {
        		this.finestraCartella(this.pazienteSelezionato);
        	}else {
        		this.showAlertWithMessage("E' necessario selezionare almeno una riga", AlertType.INFORMATION);
        	}
        });
        stage.setScene(scene);
        stage.show();
		
	}
	
//	private void openDatiAnagrafici(Paziente paziente) {
//		this.finestraInserimentoPaziente(false, paziente);
//		TextField textField = (TextField) scene.lookup("#codiceFiscaleField");
//
//	}
	
	private void finestraCartella(Paziente paziente) {
		this.relPatologiaCuraList.clear();
		Stage stage = new Stage();
		this.stageList.put("cartellaClinicaStage", stage);
		stage.setTitle("Cartella Clinica Paziente");
		List<Patologia> listaPatologie = this.eventService.getPatologiaList().getData();
		List<Farmaco> listaFarmaci = this.eventService.getFarmacoList().getData();
		this.relPatologiaCuraList = this.eventService.getPatologiaCuraList(paziente.getCodiceFiscale()).getData();
		
		Button storico = new Button("Storico");
        Button stampa = new Button("Stampa");
        Button salva = new Button("Salva");
		
		Label titoloModale = new Label("Cartella Clinica Paziente");
		titoloModale.setPadding(new Insets(5,0,20,0));
        BorderPane.setAlignment(titoloModale, Pos.TOP_CENTER);
        
        titoloModale.setStyle("-fx-font-size: 20px;"); 
        
        BorderPane root = new BorderPane();
        root.setTop(titoloModale);
        
        Label codiceFiscale = new Label("Codice Fiscale: " + paziente.getCodiceFiscale());
		codiceFiscale.setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        Label cognome = new Label("Cognome: " + paziente.getCognome());
        cognome.setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        Label nome = new Label("Nome: " + paziente.getNome());
        nome.setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        HBox rowLabel = new HBox(25, codiceFiscale, cognome, nome);
        rowLabel.setAlignment(Pos.CENTER);
        VBox vbox = new VBox(15, rowLabel);
        
        Label data = new Label("Data:");
        data.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");
        TextField dataField = new TextField();
        dataField.setPromptText("dd/MM/yyyy");
        HBox rowData = new HBox(10, data, dataField);
        rowData.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(rowData);
        
        Label dataFarmacoDa = new Label("Data Farmaco DA:");
        dataFarmacoDa.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");
        TextField dataFarmacoDaField = new TextField();
        dataFarmacoDaField.setPromptText("dd/MM/yyyy");
        Label dataFarmacoA = new Label("Data Farmaco A:");
        dataFarmacoA.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");
        TextField dataFarmacoAField = new TextField();
        dataFarmacoAField.setPromptText("dd/MM/yyyy");
        HBox rowDataDaA = new HBox(10, dataFarmacoDa, dataFarmacoDaField, dataFarmacoA, dataFarmacoAField);
        rowDataDaA.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(rowDataDaA);
        
        Label patologia = new Label("Patologia:");
        patologia.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");
        ComboBox<Patologia> patologiaField = new ComboBox<>();
        patologiaField.setItems(FXCollections.observableArrayList(listaPatologie));

        patologiaField.setValue(listaPatologie.get(0));
        Label cura = new Label("Cura:");
        cura.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");
        ComboBox<Farmaco> curaField = new ComboBox<>();
        curaField.setItems(FXCollections.observableArrayList(listaFarmaci));

        curaField.setValue(listaFarmaci.get(0));
        Button aggiungi = new Button("Aggiungi");
        Button elimina = new Button("Elimina");
        
        HBox selectRow = new HBox(20, patologia, patologiaField, cura, curaField, aggiungi, elimina);
        selectRow.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(selectRow);
        
        // ###### TABLE ############
        
        TableView<PatologiaCura> table = new TableView<>();
		table.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
		vbox.setMaxWidth(570);
		vbox.setMaxHeight(500);
		
		TableColumn<PatologiaCura, String> dataDaColumn = new TableColumn<>("Data Da");
		dataDaColumn.setCellValueFactory(new PropertyValueFactory<>("da"));
		dataDaColumn.setPrefWidth(130);
		TableColumn<PatologiaCura, String> dataAColumn = new TableColumn<>("Data A");
		dataAColumn.setCellValueFactory(new PropertyValueFactory<>("a"));
		dataAColumn.setPrefWidth(130);
		
		TableColumn<PatologiaCura, String> patologiaColumn = new TableColumn<>("Patologia");
		patologiaColumn.setCellValueFactory(cellData -> 
		    new SimpleStringProperty(cellData.getValue().getPatologia().getNome())
		);
		patologiaColumn.setPrefWidth(155);

		TableColumn<PatologiaCura, String> farmacoColumn = new TableColumn<>("Farmaco");
		farmacoColumn.setCellValueFactory(cellData -> 
		    new SimpleStringProperty(cellData.getValue().getFarmaco().getNome())
		);
		farmacoColumn.setPrefWidth(155);
 
        table.getColumns().add(dataDaColumn);
        table.getColumns().add(dataAColumn);
        table.getColumns().add(patologiaColumn);
        table.getColumns().add(farmacoColumn);
        ObservableList<PatologiaCura> dataTableCartella = FXCollections.observableArrayList(this.relPatologiaCuraList);
        table.setItems(dataTableCartella);
        vbox.getChildren().addAll(table);
        
        aggiungi.setOnAction(e -> {
        	if(!this.eventService.isDateValid(dataFarmacoAField.getText()) || !this.eventService.isDateValid(dataFarmacoDaField.getText())
        			|| !this.eventService.isDateValid(dataField.getText())) {
        		this.showAlertWithMessage("Alcune date non sono valide o non sono nel formato corretto", AlertType.WARNING);
        	}else {
        		Patologia patologiaEntity = this.eventService.getPatologiaById(patologiaField.getValue().getId()).getPatologia();
        		Farmaco farmacoEntity = this.eventService.getFarmacoById(curaField.getValue().getId()).getFarmaco();
        		PatologiaCura patologiaCura = new PatologiaCura();
            	patologiaCura.setA(dataFarmacoAField.getText());
            	patologiaCura.setDa(dataFarmacoDaField.getText());
            	patologiaCura.setPatologia(patologiaEntity);
            	patologiaCura.setFarmaco(farmacoEntity);
            	
            	this.addItemsToTable(table, dataTableCartella, patologiaCura, this.relPatologiaCuraList);
            	this.modelForm = patologiaCura;
            	
        	}
        	
        });
        
        Label note = new Label("Note:");
        patologia.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");
        TextArea noteField = new TextArea();
        noteField.setMaxWidth(370);
        noteField.setMaxHeight(70);
        HBox noteRow = new HBox(10, note, noteField);
        noteRow.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(noteRow);
        
        salva.setOnAction(e -> {
        	if(this.modelForm != null) {
        		Long idVisita = this.salvaVisita(paziente, dataField.getText(), noteField.getText());
            	if(idVisita != null) {
            		Patologia patologiaEntity = this.eventService.getPatologiaById(patologiaField.getValue().getId()).getPatologia();
            		Farmaco farmacoEntity = this.eventService.getFarmacoById(curaField.getValue().getId()).getFarmaco();
            		PatologiaCura patologiaCura = new PatologiaCura();
                	patologiaCura.setA(dataFarmacoAField.getText());
                	patologiaCura.setDa(dataFarmacoDaField.getText());
                	patologiaCura.setPatologia(patologiaEntity);
                	patologiaCura.setFarmaco(farmacoEntity);
                	patologiaCura.setIdVisita(idVisita);
                	patologiaCura.setCfPaziente(paziente.getCodiceFiscale());
                	this.eventService.salvaRel(patologiaCura);
                	this.relPatologiaCuraList = this.eventService.getPatologiaCuraList(paziente.getCodiceFiscale()).getData();
                	
            	}
        	}else {
        		this.showAlertWithMessage("Inserire un record prima di Salvare", AlertType.WARNING);
        	}
        	
        });
        storico.setOnAction(e -> {
        	this.finestraStoricoVisite(paziente);
        });
        HBox rowButtonEnd = new HBox(15, storico, stampa, salva);
        rowButtonEnd.setAlignment(Pos.CENTER);
        rowButtonEnd.setPadding(new Insets(0,0,10,0));
        vbox.getChildren().addAll(rowButtonEnd);
        
        
        root.setCenter(vbox);
        Scene scene = new Scene(root, 600, 580);
        stage.setScene(scene);
        stage.show();
 	
	}
	
	private void finestraStoricoVisite(Paziente paziente) {
		Stage stage = new Stage();
        stage.setTitle("Ricerca");
        List<Visita> storicoVisite = this.eventService.getStoricoVisite(paziente.getCodiceFiscale()).getData();
		 
		Label titoloModale = new Label("Storico Visite");
        //titoloModale.setPadding(new Insets(0,0,0,15));
        BorderPane.setAlignment(titoloModale, Pos.TOP_LEFT);
        
        titoloModale.setStyle("-fx-font-size: 24px;"); 
        BorderPane root = new BorderPane();
        root.setTop(titoloModale);
        
        Label codiceFiscale = new Label("Codice Fiscale: " + paziente.getCodiceFiscale());
		codiceFiscale.setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        Label cognome = new Label("Cognome: " + paziente.getCognome());
        cognome.setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        Label nome = new Label("Nome: " + paziente.getNome());
        nome.setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        HBox rowLabel = new HBox(25, codiceFiscale, cognome, nome);
        rowLabel.setAlignment(Pos.CENTER);
        VBox vbox = new VBox(15, rowLabel);
        
        TableView<Visita> table = new TableView<>();
		table.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
		vbox.setMaxWidth(570);
		vbox.setMaxHeight(500);
		
		
		TableColumn<Visita, Long> idVisitaColumn = new TableColumn<>("Id Visita");
		idVisitaColumn.setCellValueFactory(new PropertyValueFactory<>("idVisita"));
		idVisitaColumn.setPrefWidth(175);
		TableColumn<Visita, String> dataColumn = new TableColumn<>("Data Visita");
		dataColumn.setCellValueFactory(new PropertyValueFactory<>("data"));
		dataColumn.setPrefWidth(175);
        TableColumn<Visita, String> cfMedicoColumn = new TableColumn<>("CF Medico");
        cfMedicoColumn.setCellValueFactory(new PropertyValueFactory<>("cfMedico"));
        cfMedicoColumn.setPrefWidth(175);
        
        table.getColumns().add(idVisitaColumn);
        table.getColumns().add(dataColumn);
        table.getColumns().add(cfMedicoColumn);
        ObservableList<Visita> tableVisite = FXCollections.observableArrayList(storicoVisite);
        table.setItems(tableVisite);
        vbox.getChildren().addAll(table);
        
        Button visualizza = new Button("Visualizza");
        
      
        HBox rowButtonEnd = new HBox(15, visualizza);
        rowButtonEnd.setAlignment(Pos.CENTER);
        rowButtonEnd.setPadding(new Insets(0,0,10,0));
        vbox.getChildren().addAll(rowButtonEnd);
        
        
        root.setCenter(vbox);
        Scene scene = new Scene(root, 620, 500);
        stage.setScene(scene);
        stage.show();
		
	}
	
	private void abilitaSostituto() {
		this.listaCfMedici = this.eventService.listaMedici().getData();
		Stage stage = new Stage();
		this.stageList.put("abilitaSostitutoStage", stage);
		stage.setTitle("Abilita Sostituto");
		Button abilita = new Button("Abilita");
		
		Label titoloModale = new Label("Abilita Sostituto");
		titoloModale.setPadding(new Insets(5,0,20,0));
        BorderPane.setAlignment(titoloModale, Pos.TOP_CENTER);
        
        titoloModale.setStyle("-fx-font-size: 20px;"); 
        
        BorderPane root = new BorderPane();
        root.setTop(titoloModale);
        
//        Label cura = new Label("Cura:");
//        cura.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");
        ComboBox<String> cfMedicoField = new ComboBox<>();
        cfMedicoField.setItems(FXCollections.observableArrayList(this.listaCfMedici));

        
        //curaField.setValue(this.listaCfMedici.get(0));
        
        Label dataDal = new Label("Dal:");
        dataDal.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");
        TextField dataDalField = new TextField();
        dataDalField.setPromptText("dd/MM/yyyy");
        Label dataAl = new Label("Al:");
        dataAl.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");
        TextField dataAlField = new TextField();
        dataAlField.setPromptText("dd/MM/yyyy");
        HBox rowDataDaA = new HBox(10, cfMedicoField, dataDal, dataDalField, dataAl, dataAlField);
        rowDataDaA.setAlignment(Pos.CENTER);
        HBox abilitaRow = new HBox(20, abilita);
        abilitaRow.setAlignment(Pos.CENTER);
        VBox vbox = new VBox(35, rowDataDaA, abilitaRow);
        abilita.setOnAction(e -> {
        	if(!this.eventService.isDateValid(dataDalField.getText()) || !this.eventService.isDateValid(dataAlField.getText())) {
        		this.showAlertWithMessage("Le date non sono valide o non sono nel formato corretto", AlertType.WARNING);
        	}else if(cfMedicoField.getValue() == null || "".equals(cfMedicoField.getValue())){
        		this.showAlertWithMessage("Selezionare un medico da abilitare", AlertType.WARNING);
        	}
        	else {
        		ResponseDTO<?> response = this.eventService.abilitaSostituto(cfMedicoField.getValue(), dataDalField.getText(), dataAlField.getText());
        		if(response.getStatusCode() == 200) {
        			BorderPane mainRoot = (BorderPane) this.stageList.get("mainStage").getScene().getRoot();
                    StackPane stack = (StackPane)mainRoot.getCenter();
                    this.stageList.get("abilitaSostitutoStage").close();
                    this.showTemporaryAlert(stack, response.getMessage(), 3000);
        		}
        	}
        });
        root.setCenter(vbox);
        Scene scene = new Scene(root, 650, 180);
        stage.setScene(scene);
        stage.show();
  
	}
	
	public void finestraPrimoAccesso() {
		this.cfPrimoAccessoValido = false;
		Label cfLabel = new Label("Codice Fiscale:");
		Label userLabel = new Label("*User:");
        Label passwordLabel = new Label("*Password:");
        Label confermaPasswordLabel = new Label("*Conferma Password:");
        this.impostaButton  = new Button();
        Button cerca = new Button();
        cerca.setText("Cerca");
        this.impostaButton.setText("Imposta");

        TextField cfField = new TextField();
        PasswordField passwordField = new PasswordField(); 
        PasswordField confermaPasswordField = new PasswordField(); 
        TextField userField = new TextField();
        
        passwordField.setDisable(!this.cfPrimoAccessoValido);
        confermaPasswordField.setDisable(!this.cfPrimoAccessoValido);
        userField.setDisable(!this.cfPrimoAccessoValido);
        this.impostaButton.setDisable(!this.cfPrimoAccessoValido);
        HBox row1 = new HBox(10, cfLabel, cfField);
        row1.setAlignment(Pos.CENTER_RIGHT);
        HBox row2 = new HBox(10, cerca);
        row2.setAlignment(Pos.CENTER);
        HBox row3 = new HBox(10, userLabel, userField);
        row3.setAlignment(Pos.CENTER_RIGHT);
        HBox row4 = new HBox(10, passwordLabel, passwordField);
        row4.setAlignment(Pos.CENTER_RIGHT);
        HBox row5 = new HBox(10, confermaPasswordLabel, confermaPasswordField);
        row5.setAlignment(Pos.CENTER_RIGHT);
        HBox row6 = new HBox(10, this.impostaButton);
        row6.setAlignment(Pos.CENTER);
        VBox vbox = new VBox(15); 
        vbox.getChildren().addAll(row1, row2, row3, row4, row5, row6);
        
        vbox.setPadding(new Insets(20, 20, 20, 20));

        
        Label titoloModale = new Label("Dati per il primo accesso");
		titoloModale.setPadding(new Insets(5,0,20,0));
        BorderPane.setAlignment(titoloModale, Pos.TOP_CENTER);
        
        titoloModale.setStyle("-fx-font-size: 20px;"); 
        
        
        
        BorderPane root = new BorderPane();
        root.setCenter(vbox);
        root.setTop(titoloModale);
        
		Stage arg1 = new Stage();
		this.stageList.put("primoAccesso", arg1);
		arg1.setTitle("Parametri LoginAccesso");
		
		cerca.setOnAction(e ->{
			if(cfField.getText() == null || "".equals(cfField.getText())) {
				this.showAlertWithMessage("Inserire un Codice Fiscale", AlertType.WARNING);
			}else {
				ResponseDTO<?> response = this.eventService.isCfValid(cfField.getText());
				if(response.getStatusCode() == 200L) {
					this.cfPrimoAccessoValido = true;
					passwordField.setDisable(!this.cfPrimoAccessoValido);
			        confermaPasswordField.setDisable(!this.cfPrimoAccessoValido);
			        userField.setDisable(!this.cfPrimoAccessoValido);
			        this.impostaButton.setDisable(!this.cfPrimoAccessoValido);
				}else if(response.getStatusCode() == 404L){
					this.showAlertWithMessage(response.getMessage(), AlertType.WARNING);
				}
			}
		});
		this.impostaButton.setOnAction(e -> {
			if("".equals(passwordField.getText()) || "".equals(confermaPasswordField.getText()) || "".equals(userField.getText())) {
				this.showAlertWithMessage("Inserire i campi obbligatori", AlertType.WARNING);
			}else if(!passwordField.getText().equals(confermaPasswordField.getText())){
				this.showAlertWithMessage("Le password non coincidono tra di loro", AlertType.WARNING);
			}else {
				this.eventService.updateSostitutoLogin(cfField.getText(), userField.getText(), confermaPasswordField.getText());
				arg1.close();
			}
		});
		
		Scene scene = new Scene(root, 350, 350);
		arg1.setScene(scene);
		arg1.show();
	}
	
	private void ricercaPazienti(RicercaPazienteDTO ricerca, TableView<Paziente> table) {
		if(ricerca == null || (ricerca.getCodiceFiscale().equals("") && ricerca.getCognome().equals("") && ricerca.getNome().equals("") 
    			&& ricerca.getPatologia().equals(""))) {
    		ResponseDTO<Paziente> response = this.eventService.getListaPazienti(null);
    		if(response != null && response.getData().size() > 0) {
    			this.pazientiRicercaList = response.getData();
    		}
    		
    	}else {
    		
    		ResponseDTO<Paziente> response = this.eventService.getListaPazienti(ricerca);
    		if(response != null && response.getData() != null) {
    			this.pazientiRicercaList = response.getData();
    		}
    	}
		if(this.pazientiRicercaList.isEmpty()) {
			Label placeholderLabel = new Label("Nessun paziente trovato");

	        table.setPlaceholder(placeholderLabel);
		}
    	this.data = FXCollections.observableArrayList(
        		this.pazientiRicercaList
        );
        table.setItems(this.data);
	}
	
	private void addItemsToTable(TableView<PatologiaCura> table, ObservableList<PatologiaCura> dataTableCartella, PatologiaCura item, List<PatologiaCura> patologiaCuraList ) {
		patologiaCuraList.add(item);
		dataTableCartella = FXCollections.observableArrayList(patologiaCuraList);
		table.setItems(dataTableCartella);
	}
	
	private Long salvaVisita(Paziente paziente, String data, String note) {
		Long idVisita = null;
		if(this.cfMedicoLoggato != null) {
    		Visita visita = new Visita();
        	visita.setCfMedico(this.cfMedicoLoggato);
        	visita.setCfPaziente(paziente.getCodiceFiscale());
        	visita.setData(data);
        	visita.setNote(note);
        	ResponseDTO<?> response = this.eventService.salvaVisita(visita);
        	if(response.getStatusCode() == 200L) {
        		idVisita = response.getId();
        		this.showAlertWithMessage(response.getMessage(), AlertType.CONFIRMATION);
        	}else {
        		this.showAlertWithMessage(response.getMessage(), AlertType.ERROR);
        	}
    	}else {
    		this.showAlertWithMessage("Problema con il cf del medico", AlertType.ERROR);
    	}
		
		return idVisita;
	}
	

}
