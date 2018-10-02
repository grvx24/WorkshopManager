/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package warsztat;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;

/**
 *
 * @author Kamil
 */
public class TsvToDbConverter {
    
    public static void ConvertFromTsvToMainWorkshop() throws SQLException{
        DatabaseManager db = new DatabaseManager("MainWorkshop");
        
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(
                    "mag.tsv"));
            String line = reader.readLine();
            while (line != null) {
                //System.out.println(line);
                
                String[] parts=line.split("\t");
                PartsDataModel model = new PartsDataModel(0,"","","","",0,"","","");
                
                for (int i = 0; i < parts.length; i++) {
                    System.out.print(parts[i]+" ");
                    switch(i)
                    {
                        case 0:
                        {
                            model.setName(parts[i]);
                            break;
                        }case 1:
                        {
                            model.setType(parts[i]);
                            break;
                        }case 2:
                        {
                            model.setParameters(parts[i]);
                            break;
                        }case 3:
                        {
                            model.setCode(parts[i]);
                            break;
                        }case 4:
                        {
                            model.setQuantity(Integer.parseInt(parts[i]));
                            break;
                        }case 5:
                        {
                            model.setUsage(parts[i]);
                            break;
                        }case 6:
                        {
                            model.setOther(parts[i]);
                            break;
                        }case 7:
                        {
                            if(model.getQuantity()>0){
                                model.setMissing(WorkshopOverviewController.missingFalseText);
                            }else{
                                model.setMissing(WorkshopOverviewController.missingTrueText);
                            }
                            break;
                        }
                    }
                    
                    db.AddItem(model);
                }
                // read next line
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
}
