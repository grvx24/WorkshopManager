/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package warsztat;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Kamil Sagalara
 */
public class AddItemWindowController implements Initializable {

    @FXML private TextField nameTF;
    @FXML private TextField typeTF;
    @FXML private TextField paramsTF;
    @FXML private TextField codeTF;
    @FXML private TextField quantityTF;
    @FXML private TextField usageTF;
    @FXML private TextField otherTF;
    @FXML private TextField missingTF;
    
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        quantityTF.setText("0");
    }
    
    public void addItemEvent(ActionEvent t)
    {
        try {
            addItem();
            
        } catch (Exception e) {
            
            //Alert  
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Okno błędu");
            alert.setHeaderText("Wystąpił błąd!");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    protected void addItem() throws Exception
    { 
        try {
            
            int quantity = Integer.parseInt(quantityTF.getText());
            String name = nameTF.getText();
            if(name.isEmpty())
            {
                throw new Exception("Pole nazwa nie może być puste!");
            }
            
            PartsDataModel item = new PartsDataModel(0,name ,
            typeTF.getText(), paramsTF.getText(),
            codeTF.getText(), quantity,
                    usageTF.getText(), otherTF.getText(), missingTF.getText());
            
            DatabaseManager db = new DatabaseManager("Workshop");
            db.AddItem(item);        
            observableList.add(item);
            //TODO
            //NAPRAWIĆ ID PRZEDMIOTÓW!!!
            

                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Powiadomienie");
                alert.setHeaderText("Sukces!");
                alert.setContentText("Pomyślnie dodano przedmiot!");
                alert.showAndWait();   
                
        } catch(NumberFormatException e){
            throw new NumberFormatException("Pole ilość musi być wartością numeryczną!");
        } 

    }
    
    private ObservableList<PartsDataModel> observableList;
    
    public void setObservabliList(ObservableList<PartsDataModel> list){
        this.observableList = list;
    }
    
    
}
