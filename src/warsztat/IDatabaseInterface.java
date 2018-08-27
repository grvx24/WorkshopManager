/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package warsztat;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Kamil Sagalara
 */
public interface IDatabaseInterface {
    
    void AddItem(PartsDataModel parts) throws SQLException;
    void EditItem(PartsDataModel parts);
    void DeleteItem(PartsDataModel part);
    ArrayList<PartsDataModel> GetAllItems();
    
}
