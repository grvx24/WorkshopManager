/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package warsztat;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

/**
 *
 * @author Kamil Sagalara
 */
public class WorkshopOverviewController implements Initializable {

    private DatabaseManager db  = new DatabaseManager("Workshop");
    private ObservableList<PartsDataModel> observableList;
    

   
    @FXML private TableView<PartsDataModel> tableView;
    @FXML private TableColumn<PartsDataModel,String> nameColumn;
    @FXML private TableColumn<PartsDataModel,String> typeColumn;
    @FXML private TableColumn<PartsDataModel,String> parametersColumn;
    @FXML private TableColumn<PartsDataModel,String> codeColumn;
    @FXML private TableColumn<PartsDataModel,Integer> quantityColumn;
    @FXML private TableColumn<PartsDataModel,String> usageColumn;
    @FXML private TableColumn<PartsDataModel,String> otherColumn;
    @FXML private TableColumn<PartsDataModel,String> missingColumn;
    
    
    @FXML protected void addItemEvent(ActionEvent event)
    {
        Parent root;
        
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Warsztat.class.getResource("AddItemWindow.fxml"));
            root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Dodaj przedmiot");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            AddItemWindowController controller = loader.getController();
            controller.setObservabliList(observableList);
            stage.show();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @FXML protected void deleteSelectedRows(ActionEvent event)
    {
        ObservableList<PartsDataModel> itemsToRemove = tableView.getSelectionModel().getSelectedItems();
        
        tableView.getItems().removeAll(itemsToRemove);
        
        try {
        
            for(PartsDataModel item : itemsToRemove)
            {
                db.DeleteItem(item);
            }
        } catch (Exception e) {
            throw e;
        }
        
        
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    
        initTable();
        loadData();
    }

    private void initTable(){

        initCols();
    }
    
    private void initCols(){
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("Type"));
        parametersColumn.setCellValueFactory(new PropertyValueFactory<>("Parameters"));
        codeColumn.setCellValueFactory(new PropertyValueFactory<>("Code"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("Quantity"));
        usageColumn.setCellValueFactory(new PropertyValueFactory<>("Usage"));
        otherColumn.setCellValueFactory(new PropertyValueFactory<>("Other"));
        missingColumn.setCellValueFactory(new PropertyValueFactory<>("Missing"));
        
        editableCols();
        
    }   
    
    private void editableCols(){
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        
        nameColumn.setOnEditCommit(e->
        {
            PartsDataModel data = e.getTableView().getItems().get(e.getTablePosition().getRow());
            data.setName(e.getNewValue());
            
            db.EditItem(data);
            
        });
             
        typeColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        
        typeColumn.setOnEditCommit(e->
        {
            PartsDataModel data = e.getTableView().getItems().get(e.getTablePosition().getRow());
            data.setType(e.getNewValue());
            
            db.EditItem(data);
        });
            
        parametersColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        
        parametersColumn.setOnEditCommit(e->
        {
            PartsDataModel data = e.getTableView().getItems().get(e.getTablePosition().getRow());
            data.setParameters(e.getNewValue());
            
            db.EditItem(data);
        });
           
        codeColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        
        codeColumn.setOnEditCommit(e->
        {
            PartsDataModel data = e.getTableView().getItems().get(e.getTablePosition().getRow());
            data.setCode(e.getNewValue());
            
            db.EditItem(data);
        });
          
        quantityColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        
        quantityColumn.setOnEditCommit(e->
        {
            PartsDataModel data = e.getTableView().getItems().get(e.getTablePosition().getRow());
            data.setQuantity(e.getNewValue());
            
            db.EditItem(data);
        });
            
        usageColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        
        usageColumn.setOnEditCommit(e->
        {
            PartsDataModel data = e.getTableView().getItems().get(e.getTablePosition().getRow());
            data.setUsage(e.getNewValue());
            
            db.EditItem(data);
        });
           
        otherColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        
        otherColumn.setOnEditCommit(e->
        {
            PartsDataModel data = e.getTableView().getItems().get(e.getTablePosition().getRow());
            data.setOther(e.getNewValue());
            
            db.EditItem(data);
        });
               
        missingColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        
        missingColumn.setOnEditCommit(e->
        {
            PartsDataModel data = e.getTableView().getItems().get(e.getTablePosition().getRow());
            data.setMissing(e.getNewValue());
            
            db.EditItem(data);
        });
        
        tableView.setEditable(true);

    }
    
    private void loadData(){
        observableList = FXCollections.observableArrayList(db.GetAllItems());
        tableView.setItems(observableList);
    }
 
}
