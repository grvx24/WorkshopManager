/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package warsztat;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.FutureTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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
    private ObservableList<PartsDataModel> editableList;
    
    public static final String missingTrueText = "Tak"; 
    public static final String missingFalseText = "Nie";
   
    @FXML private TableView<PartsDataModel> tableView;
    @FXML private TableColumn<PartsDataModel,String> nameColumn;
    @FXML private TableColumn<PartsDataModel,String> typeColumn;
    @FXML private TableColumn<PartsDataModel,String> parametersColumn;
    @FXML private TableColumn<PartsDataModel,String> codeColumn;
    @FXML private TableColumn<PartsDataModel,Integer> quantityColumn;
    @FXML private TableColumn<PartsDataModel,String> usageColumn;
    @FXML private TableColumn<PartsDataModel,String> otherColumn;
    @FXML private TableColumn<PartsDataModel,String> missingColumn;
    
    @FXML private Label uniqueItemsCount;
    @FXML private Label sumLabel;
    @FXML private Label missingItemsSumLabel;
    @FXML private TextField searchTextField;
    @FXML private ComboBox categoryComboBox;
    
    @FXML private Label infoLoadingLabel;
    
    
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
            controller.setParentController(this);
            stage.show();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    @FXML protected void deleteSelectedRows(ActionEvent event)
    {
        ObservableList<PartsDataModel> itemsToRemove = tableView.getSelectionModel().getSelectedItems();
        
        try {
        
            for(PartsDataModel item : itemsToRemove)
            {
                db.DeleteItem(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        tableView.getItems().removeAll(itemsToRemove);
        
        initInformationsAsync();
    }
    
    @FXML protected void searchByCategory(ActionEvent event){
        
        int selectedIndex=categoryComboBox.getSelectionModel().getSelectedIndex();
        String text = searchTextField.getText();
        
        if(text.isEmpty()||text==null){
            tableView.setItems(observableList);
            return;
        }
        
        List<PartsDataModel> tempList = observableList;
        
        try
        {
            switch(selectedIndex){
                case 0:
                {
                    tempList=observableList.stream()
                    .filter(model->model.getName().contains(text))
                    .collect(Collectors.toList());                   
                    break;
                }
                case 1:
                {
                    tempList=observableList.stream()
                    .filter(model->model.getType().contains(text))
                    .collect(Collectors.toList());                
                    break;
                }
                case 2:
                {
                    tempList=observableList.stream()
                    .filter(model->model.getParameters().contains(text))
                    .collect(Collectors.toList());               
                    break;
                }
                case 3:
                {
                    tempList=observableList.stream()
                    .filter(model->model.getCode().contains(text))
                    .collect(Collectors.toList());                
                    break;
                }
                case 4:
                {
                    tempList=observableList.stream()
                    .filter(model->model.getQuantity()==Integer.parseInt(text))
                    .collect(Collectors.toList());                
                    break;
                }
                case 5:
                {
                    tempList=observableList.stream()
                    .filter(model->model.getUsage().contains(text))
                    .collect(Collectors.toList());                
                    break;
                }
                case 6:
                {
                    tempList=observableList.stream()
                    .filter(model->model.getOther().contains(text))
                    .collect(Collectors.toList());                
                    break;
                }
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
            return;
        }  
        
        System.out.println(selectedIndex);
        
        editableList = FXCollections.observableArrayList(tempList);
        tableView.setItems(editableList);
    }
        

        


    @Override
    public void initialize(URL url, ResourceBundle rb) {
    
        initControls();
        initTable();
        initCategoryCombobox();
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
    
    public void initInformationsAsync(){

        Platform.runLater(new Runnable(){
            @Override
            public void run(){
                infoLoadingLabel.setVisible(true);
                setUniqueItemsLabel();
                setSumLabel();
                setMissingItemsSumLabel();
                infoLoadingLabel.setVisible(false);
            }
        });
    }
    
    private void setUniqueItemsLabel(){
        Integer uniqueItems = observableList.size();
        uniqueItemsCount.setText(uniqueItems.toString());
    }
    
    private void setSumLabel(){
        Integer sum = countSumOfAllItems();
        sumLabel.setText(sum.toString());
    }
    
    private void setMissingItemsSumLabel(){
        Integer sum = countMissingItems();
        
        missingItemsSumLabel.setText(sum.toString()+"/"+observableList.size());
    }
    
    
    private int countSumOfAllItems(){
        
        Integer total = 0;
        
        for(PartsDataModel item : observableList){
            total+=item.getQuantity();
        }
        return total;
    }
    
    private int countMissingItems(){
        List<PartsDataModel> missingItems =observableList
                .stream()
                .filter(m->m.getMissing()==missingTrueText)
                .collect(Collectors.toList());
        
        return missingItems.size();
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
            
            if(data.getQuantity()>0){
                data.setMissing(missingFalseText);
            }else{
                data.setMissing(missingTrueText);
            }
            
            db.EditItem(data);
            tableView.refresh();
            
            initInformationsAsync();
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
               
//        missingColumn.setCellFactory(TextFieldTableCell.forTableColumn());
//        
//        missingColumn.setOnEditCommit(e->
//        {
//            PartsDataModel data = e.getTableView().getItems().get(e.getTablePosition().getRow());
//            data.setMissing(e.getNewValue());
//            
//            db.EditItem(data);
//        });
        
        tableView.setEditable(true);

    }
    
    private void changeMissingColumn(int value){

    }
    
    private void loadData(){
        observableList = FXCollections.observableArrayList(db.GetAllItems());
        tableView.setItems(observableList);
    }
    
    private void initCategoryCombobox(){
        int columnsSize=tableView.getColumns().size(); 

        for (int i = 0; i < columnsSize-1; i++) {
            categoryComboBox.getItems().add(tableView.getColumns().get(i).textProperty().getValue());            
        }
        categoryComboBox.getSelectionModel().selectFirst();
    }
    
    private void initControls(){
        infoLoadingLabel.setVisible(false);
        initInformationsAsync();

    }
 
}
