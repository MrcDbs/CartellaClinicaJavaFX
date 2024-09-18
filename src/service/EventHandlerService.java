package service;

import java.util.Map;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;

public class EventHandlerService implements EventHandler<ActionEvent> {
    private Map<String, Button> buttonMap;
    private Map<String, TextField> textFieldMap;
    private Map<String, MenuItem> menuItemMap;

    // Constructor to accept the three maps
    public EventHandlerService(Map<String, Button> buttonMap, Map<String, TextField> textFieldMap, Map<String, MenuItem> menuItemMap) {
        this.buttonMap = buttonMap;
        this.textFieldMap = textFieldMap;
        this.menuItemMap = menuItemMap;
    }

	@Override
    public void handle(ActionEvent event) {
        // Check if the event source is a Button
        if (event.getSource() instanceof Button) {
            Button clickedButton = (Button) event.getSource();
            handleButtonClick(clickedButton);
        }

        // Check if the event source is a MenuItem
        if (event.getSource() instanceof MenuItem) {
            MenuItem clickedMenuItem = (MenuItem) event.getSource();
            handleMenuItemClick(clickedMenuItem);
        }
    }

    private void handleButtonClick(Button clickedButton) {
        // Loop through the map to find the associated key
        buttonMap.forEach((key, button) -> {
            if (button == clickedButton) {
                // Access the corresponding TextField using the key
                TextField associatedField = textFieldMap.get(key);
                if (associatedField != null) {
                    String text = associatedField.getText();
                    System.out.println("Button '" + key + "' clicked! TextField value: " + text);
                }
            }
        });
    }

    private void handleMenuItemClick(MenuItem clickedMenuItem) {
        // Loop through the map to find the associated key
        menuItemMap.forEach((key, menuItem) -> {
            if (menuItem == clickedMenuItem) {
                System.out.println("MenuItem '" + key + "' clicked!");
            }
        });
    }
}


