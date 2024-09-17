package mainApplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dao.DataAccessRepository;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
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
import model.ListModelDTO;
import model.ModelDTO;
import model.PazienteDTO;
import model.ResponseDTO;
import model.RicercaPazienteDTO;
import model.UserLoginDTO;
import service.MainService;

public class MainApp extends Application implements EventHandler<ActionEvent>{

	private MainService service;
	private Button button;
	private Button submitButton;
	private MenuItem newItem;
	private boolean userLoggedIn = false;
	private Label titleLabel = new Label("Gestione Cartella Clinica");
	private PazienteDTO pazienteSelezionato = null;
	private Menu fileMenu;
	private Menu editMenu;
	private Menu helpMenu;
	private Menu questionMark;
	private TableView<PazienteDTO> tableRicercaPaziente;
	
	private ObservableList<PazienteDTO> data;
	private List<PazienteDTO> pazientiRicercaList = new ArrayList<>();
	private Map<String, Stage> stageList = new HashMap<String, Stage>();
	private Map<String, Button> buttonList = new HashMap<String, Button>();
	private Map<String, MenuItem> menuItemList = new HashMap<String, MenuItem>();
	//private Map<String, Control> pazienteFields = new HashMap<String, Control>();
	
	public static void main(String[] args) {
		launch(args);

	}

	@Override
	public void start(Stage arg0) throws Exception {
		this.service = new MainService(new DataAccessRepository());
		this.stageList.put("mainStage", arg0);
		arg0.setTitle("Cartella clinica");
		
		MenuBar menuBar = new MenuBar();
		
	   fileMenu = new Menu("File");
       editMenu = new Menu("Paziente");
       helpMenu = new Menu("Sostituto");
       questionMark = new Menu("?");
        
        editMenu.setDisable(!this.userLoggedIn);
        helpMenu.setDisable(!this.userLoggedIn);
        questionMark.setDisable(!this.userLoggedIn);
     // Step 3: Add MenuItems to each Menu
        // File Menu Items
        newItem = new MenuItem("Login");
        MenuItem openItem = new MenuItem("Primo accesso");
        MenuItem saveItem = new MenuItem("Logout");
        MenuItem dbItem = new MenuItem("Connessione DB");
        MenuItem exitItem = new MenuItem("Esci");
        
        // Paziente menu items
        MenuItem ricercaPaziente = new MenuItem("Ricerca paziente");
        MenuItem inserisciPaziente = new MenuItem("Inserisci paziente");
        this.menuItemList.put("InserisciPazienteItem", inserisciPaziente);
        // Adding actions to menu items (optional)
        exitItem.setOnAction(e -> arg0.close()); // Close app when 'Exit' is clicked
        newItem.setOnAction(this);
        inserisciPaziente.setOnAction(this);
        ricercaPaziente.setOnAction(e -> {
        	this.openModaleRicerca();
        });
        fileMenu.getItems().addAll(newItem, openItem, saveItem, dbItem, exitItem);
        editMenu.getItems().addAll(ricercaPaziente, inserisciPaziente);
     // Step 4: Add Menus to MenuBar
        menuBar.getMenus().addAll(fileMenu, editMenu, helpMenu, questionMark);
        // Create layout
        StackPane layout = new StackPane();
     // Create the label
        
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;"); 
        // Add the label to the StackPane
        layout.getChildren().add(titleLabel);

        // Set the alignment of the label to the top center of the StackPane
        StackPane.setAlignment(titleLabel, Pos.TOP_CENTER);
     // Step 5: Add the MenuBar to the Scene/Stage layout
        BorderPane root = new BorderPane();
        root.setTop(menuBar); // Place the menu bar at the top of the layout
        
        // Create the scene with the layout and set it on the primary stage
        //Scene scene = new Scene(root, 600, 400);
        //arg0.setScene(scene);
        
		button = new Button();
		button.setText("Click");
		button.setOnAction(this);
		
		
		//layout.getChildren().add(button);
		Scene scene = new Scene(root, 475, 500);
		root.setCenter(layout);
		arg0.setScene(scene);
		arg0.show();
		
	}

	@Override
	public void handle(ActionEvent arg0) {
		System.out.println(arg0.toString());
		System.out.println(arg0.getSource().toString());
		System.out.println(arg0.getEventType());
		if(arg0.getSource() == button) {
			System.out.println("SOURCE EQUALS TO BUTTON");
		}else if(arg0.getSource() == newItem){
			System.out.println("SOURCE EQUALS TO Login menu item");
			System.out.println(arg0.getSource() == newItem);
			this.openLoginModal();
		}else if(arg0.getSource().equals(this.menuItemList.get("InserisciPazienteItem"))) {
			System.out.println("Inserisci paziente tasto cliccato");
			System.out.println(this.menuItemList.get("InserisciPazienteItem"));
			this.openInserisciPazienteModal(true, null);
		}
//		else if(arg0.getSource().equals(this.buttonList.get("salvaPazienteButton"))){
//			this.aggiornaInserisciPaziente()
//		}
		
		
	}
	
	private void openInserisciPazienteModal(boolean nuovoPaziente, PazienteDTO pazienteDTO) {
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
        //titoloModale.setPadding(new Insets(0,0,0,15));
        BorderPane.setAlignment(titoloModale, Pos.TOP_CENTER);
        
        titoloModale.setStyle("-fx-font-size: 24px;"); 
        // Add the label to the StackPane
        //layout.getChildren().add(titoloModale);
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
        
        dataNascitaField.setPromptText("dd-MM-yyyy");
     // Create a ToggleGroup to group the RadioButtons
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
       
        //String sessoValue = null;
//        group.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
//            if (newValue != null) {
//                RadioButton selected = (RadioButton) newValue;
//                //sessoValue = selected.getText();
//            }
//        });
        
        salvaPaziente.setOnAction(e ->{
        	if(codiceFiscaleField.getText().equals("") || cognomeField.getText().equals("") || nomeField.getText().equals("")) {
        		this.showAlertWithMessage("Devi inserire i campi obbligatori!", AlertType.WARNING);
        	}else {
        		PazienteDTO paziente = new PazienteDTO();
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
    			System.out.println(paziente.toString());
    			System.out.println(codiceFiscaleField.getText());
    			System.out.println(codiceFiscaleField.getText().getClass());
    			ResponseDTO<?> response = this.service.salvaPaziente(paziente, nuovoPaziente);
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
//        row1.getChildren().addAll(codiceFiscale, codiceFiscaleField);
//        row2.getChildren().addAll(cognome, cognomeField);
//        row3.getChildren().addAll(nome, nomeField);
//        row4.getChildren().addAll(dataNascita, dataNascitaField);
//        row5.getChildren().addAll(luogoNascita, luogoNascitaField);
//        row6.getChildren().addAll(residenza, residenzaField);
//        row7.getChildren().addAll(via, viaField);
//        row8.getChildren().addAll(occupazione, occupazioneField);
//        row9.getChildren().addAll(numeroTelefono,numeroTelefonoField);
//        row10.getChildren().addAll(sesso, sessoFieldM, sessoFieldF);
        
        vbox.getChildren().addAll(row1, row2, row3, row4,
        		row5, row6, row7, row8, row9, row10);
        
        vbox.setMaxWidth(330);   
        vbox.setMaxHeight(400); 
        
        vbox.setPadding(new Insets(20));  // 20 pixels padding around the VBox
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

	private void openLoginModal() {
		Label nameLabel = new Label("Username:");
        Label passwordLabel = new Label("Password:");
        submitButton  = new Button();
//        ResponseDTO response = null;
        
        
        // Create input fields
        TextField nameField = new TextField();        // Single-line input for name
        PasswordField passwordField = new PasswordField();  // Single-line masked input for password
        
     // Optional: Set Prompt Text (Placeholder Text)
        nameField.setPromptText("Enter your name");
        passwordField.setPromptText("Enter your password");
        
     // Action when the button is clicked (retrieve values from input fields)
        submitButton.setOnAction(e -> {
            String username = nameField.getText(); // Retrieve name input
            String password = passwordField.getText(); // Retrieve password input

            UserLoginDTO user = new UserLoginDTO();
            user.setUsername(username);
            System.out.println("Username: " + username);
            user.setPassword(password);
            System.out.println("Password: " + password);
            ResponseDTO<?> response = this.service.login(user);
            if(response.getStatusCode() != 200L) {
            	if(response.getStatusCode() != 500L) {
            		this.showAlertWithMessage(response.getMessage(), AlertType.WARNING);
            	}
            }else {
            	this.userLoggedIn = true;
            	this.stageList.get("loginStage").close();

            	editMenu.setDisable(!this.userLoggedIn);
                helpMenu.setDisable(!this.userLoggedIn);
                questionMark.setDisable(!this.userLoggedIn);
                
                BorderPane root = (BorderPane) this.stageList.get("mainStage").getScene().getRoot();
                StackPane stack = (StackPane)root.getCenter();
                this.showTemporaryAlert(stack, "Accesso effettuato", 3000);
//                Label accessTitleLabel = new Label(response.getMessage());
//                accessTitleLabel.setStyle("-fx-font-size: 20px;"); 
//                stack.getChildren().remove(this.titleLabel);
//                StackPane.setAlignment(accessTitleLabel, Pos.CENTER);
//                stack.getChildren().add(accessTitleLabel);
//                root.setCenter(stack);
                
            }
        });
        // Arrange the components in a VBox layout
        VBox vbox = new VBox(10); // 10px spacing between elements
        vbox.getChildren().addAll(nameLabel, nameField, passwordLabel, passwordField);
        
     // Add padding around the VBox to create space between the inputs and the window border
        vbox.setPadding(new Insets(20, 20, 20, 20)); // 20px padding on all sides (top, right, bottom, left)

        
        BorderPane root = new BorderPane();
        root.setCenter(vbox);
        
		Stage arg1 = new Stage();
		this.stageList.put("loginStage", arg1);
		arg1.setTitle("Parametri Login");
		
		
		submitButton.setText("Login");
		
		StackPane layout = new StackPane();
		layout.getChildren().add(submitButton);
		layout.setPadding(new Insets(0, 0, 20, 0));
		root.setBottom(layout);
		Scene scene = new Scene(root, 300, 210);
		arg1.setScene(scene);
		arg1.show();
		
	}
	
	private void showAlertWithMessage(String message, AlertType type) {
		// Create an information alert
        Alert alert = new Alert(type);
        alert.setTitle("Messaggio");
        alert.setHeaderText(null);  // No header
        alert.setContentText(message);

        // Show the alert and wait for user interaction
        alert.showAndWait();
	}
	
	private void showTemporaryAlert(StackPane parent, String message, int durationMillis) {
        // Create the label for the alert
        Label alertLabel = new Label(message);
        alertLabel.setStyle("-fx-background-color: lightgreen; -fx-border-color: black; -fx-padding: 10px;");

        // Add the label to the parent container
        parent.getChildren().add(alertLabel);

        // Create a PauseTransition to remove the label after a specified duration
        PauseTransition pause = new PauseTransition(Duration.millis(durationMillis));
        pause.setOnFinished(event -> parent.getChildren().remove(alertLabel));
        pause.play();
    }
	
	private void openModaleRicerca() {
		this.pazientiRicercaList.clear();
		Button ricercaPaziente = new Button("Cerca");
		Button cartellaClinica = new Button("Cartella Clinica");
		Button datiAnagrafici = new Button("Dati Anagrafici");
		this.buttonList.put("ricercaPazienteButton", ricercaPaziente);
		this.buttonList.put("cartellaClinicaButton", cartellaClinica);
		this.buttonList.put("datiAnagraficiButton", datiAnagrafici);
		
		Stage stage = new Stage();
        this.stageList.put("ricercaPazienteStage", stage);
        stage.setTitle("Ricerca");
        
        //StackPane layout = new StackPane();
        Label titoloModale = new Label("Ricerca paziente");
        //titoloModale.setPadding(new Insets(0,0,0,15));
        BorderPane.setAlignment(titoloModale, Pos.TOP_CENTER);
        
        titoloModale.setStyle("-fx-font-size: 24px;"); 
        // Add the label to the StackPane
        //layout.getChildren().add(titoloModale);
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
        
        vbox.setPadding(new Insets(20));  // 20 pixels padding around the VBox
        vbox.setAlignment(Pos.CENTER);  
        
        
		
		// ############## TABLE ################
		
		TableView<PazienteDTO> table = new TableView<>();
		table.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
		
		
		TableColumn<PazienteDTO, Integer> codiceColumn = new TableColumn<>("Codice Fiscale");
        codiceColumn.setCellValueFactory(new PropertyValueFactory<>("codiceFiscale"));
        codiceColumn.setPrefWidth(140);
		TableColumn<PazienteDTO, String> cognomeColumn = new TableColumn<>("Cognome");
		cognomeColumn.setCellValueFactory(new PropertyValueFactory<>("cognome"));
		cognomeColumn.setPrefWidth(120);
        TableColumn<PazienteDTO, String> nomeColumn = new TableColumn<>("Nome");
        nomeColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));
        nomeColumn.setPrefWidth(95);

        

        // Add columns to the table
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
        	
        });
        
        this.data = FXCollections.observableArrayList(
        		this.pazientiRicercaList
        );

        table.setItems(this.data);
        
        
        // PER IL CLICK DELLA ROW
        table.setRowFactory(tv -> {
            TableRow<PazienteDTO> row = new TableRow<>();
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
        		this.openDatiAnagrafici(this.pazienteSelezionato);
        	}else {
        		this.showAlertWithMessage("E' necessario selezionare almeno una riga", AlertType.INFORMATION);
        	}
        	
        });
        cartellaClinica.setOnAction(e -> {
        	if(this.pazienteSelezionato != null) {
        		this.openCartellaClinicaModale(this.pazienteSelezionato);
        	}
        });
        stage.setScene(scene);
        stage.show();
		
	}
	
	private void openDatiAnagrafici(PazienteDTO paziente) {
		this.openInserisciPazienteModal(false, paziente);
//		TextField textField = (TextField) scene.lookup("#codiceFiscaleField");

	}
	
	private void openCartellaClinicaModale(PazienteDTO paziente) {
		Stage stage = new Stage();
		this.stageList.put("cartellaClinicaStage", stage);
		stage.setTitle("Ricerca");
		
		Label titoloModale = new Label("Cartella Clinica Paziente");
		titoloModale.setPadding(new Insets(5,0,20,0));
        BorderPane.setAlignment(titoloModale, Pos.TOP_CENTER);
        
        titoloModale.setStyle("-fx-font-size: 20px;"); 
        
        BorderPane root = new BorderPane();
        root.setTop(titoloModale);
        
        Label codiceFiscale = new Label("Codice Fiscale: " + paziente.getCodiceFiscale());
		codiceFiscale.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");
        Label cognome = new Label("Cognome: " + paziente.getCognome());
        cognome.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");
        Label nome = new Label("Nome: " + paziente.getNome());
        nome.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");
        HBox rowLabel = new HBox(25, codiceFiscale, cognome, nome);
        rowLabel.setAlignment(Pos.CENTER);
        VBox vbox = new VBox(15, rowLabel);
        
        Label data = new Label("Data:");
        data.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");
        TextField dataField = new TextField();
        HBox rowData = new HBox(10, data, dataField);
        rowData.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(rowData);
        
        Label dataFarmacoDa = new Label("Data Farmaco DA:");
        dataFarmacoDa.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");
        TextField dataFarmacoDaField = new TextField();
        Label dataFarmacoA = new Label("Data Farmaco A:");
        dataFarmacoA.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");
        TextField dataFarmacoAField = new TextField();
        HBox rowDataDaA = new HBox(10, dataFarmacoDa, dataFarmacoDaField, dataFarmacoA, dataFarmacoAField);
        rowDataDaA.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(rowDataDaA);
        
        Label patologia = new Label("Patologia:");
        patologia.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");
        ComboBox<String> patologiaField = new ComboBox<>();
        patologiaField.getItems().addAll("Patologia1","Patologia2");
        Label cura = new Label("Cura:");
        cura.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");
        ComboBox<String> curaField = new ComboBox<>();
        curaField.getItems().addAll("Cura1","Cura2");
        Button aggiungi = new Button("Aggiungi");
        Button elimina = new Button("Elimina");
        
        HBox selectRow = new HBox(20, patologia, patologiaField, cura, curaField, aggiungi, elimina);
        selectRow.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(selectRow);
        
        // ###### TABLE ############
        
        TableView<PazienteDTO> table = new TableView<>();
		table.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
		vbox.setMaxWidth(570);
		vbox.setMaxHeight(500);
		
		TableColumn<PazienteDTO, Integer> dataDaColumn = new TableColumn<>("Data Da");
		dataDaColumn.setCellValueFactory(new PropertyValueFactory<>("dataDa"));
		dataDaColumn.setPrefWidth(130);
		TableColumn<PazienteDTO, String> dataAColumn = new TableColumn<>("Data A");
		dataAColumn.setCellValueFactory(new PropertyValueFactory<>("dataA"));
		dataAColumn.setPrefWidth(130);
        TableColumn<PazienteDTO, String> patologiaColumn = new TableColumn<>("Patologia");
        patologiaColumn.setCellValueFactory(new PropertyValueFactory<>("patologia"));
        patologiaColumn.setPrefWidth(155);
        TableColumn<PazienteDTO, String> farmacoColumn = new TableColumn<>("Farmaco");
        farmacoColumn.setCellValueFactory(new PropertyValueFactory<>("farmaco"));
        farmacoColumn.setPrefWidth(155);

        

        // Add columns to the table
        table.getColumns().add(dataDaColumn);
        table.getColumns().add(dataAColumn);
        table.getColumns().add(patologiaColumn);
        table.getColumns().add(farmacoColumn);
        
        vbox.getChildren().addAll(table);
        
        Label note = new Label("Note:");
        patologia.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");
        TextArea noteField = new TextArea();
        noteField.setMaxWidth(370);
        noteField.setMaxHeight(70);
        HBox noteRow = new HBox(10, note, noteField);
        noteRow.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(noteRow);
        
        Button storico = new Button("Storico");
        Button stampa = new Button("Stampa");
        Button salva = new Button("Salva");
        HBox rowButtonEnd = new HBox(15, storico, stampa, salva);
        rowButtonEnd.setAlignment(Pos.CENTER);
        rowButtonEnd.setPadding(new Insets(0,0,10,0));
        vbox.getChildren().addAll(rowButtonEnd);
        
        
        root.setCenter(vbox);
        Scene scene = new Scene(root, 600, 580);
        stage.setScene(scene);
        stage.show();
        
        
		
		
		
	}
	
	private void ricercaPazienti(RicercaPazienteDTO ricerca, TableView<PazienteDTO> table) {
		if(ricerca == null || (ricerca.getCodiceFiscale().equals("") && ricerca.getCognome().equals("") && ricerca.getNome().equals("") 
    			&& ricerca.getPatologia().equals(""))) {
    		ResponseDTO<PazienteDTO> response = this.service.getListaPazienti(null);
    		if(response != null && response.getData().size() > 0) {
    			this.pazientiRicercaList = response.getData();
    		}
    		
    	}else {
    		
    		ResponseDTO<PazienteDTO> response = this.service.getListaPazienti(ricerca);
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
	

}
