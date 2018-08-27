/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package warsztat;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Kamil Sagalara
 */
public class PartsDataModel {
    private int ID;
    private String Name;
    private String Type;
    private String Parameters;
    private String Code;
    private int Quantity;
    private String Usage;
    private String Other;
    private String Missing;

    public PartsDataModel(int ID, String Name, String Type, String Parameters, String Code, int Quantity, String Usage, String Other, String Missing) {
        this.ID = ID;
        this.Name = Name;
        this.Type = Type;
        this.Parameters = Parameters;
        this.Code = Code;
        this.Quantity = Quantity;
        this.Usage = Usage;
        this.Other = Other;
        this.Missing = Missing;
    }

    
    
    /**
     * @return the ID
     */
    public int getID() {
        return ID;
    }

    /**
     * @return the Name
     */
    public String getName() {
        return Name;
    }

    /**
     * @param Name the Name to set
     */
    public void setName(String Name) {
        this.Name = Name;
    }

    /**
     * @return the Type
     */
    public String getType() {
        return Type;
    }

    /**
     * @param Type the Type to set
     */
    public void setType(String Type) {
        this.Type = Type;
    }

    /**
     * @return the Parameters
     */
    public String getParameters() {
        return Parameters;
    }

    /**
     * @param Parameters the Parameters to set
     */
    public void setParameters(String Parameters) {
        this.Parameters = Parameters;
    }

    /**
     * @return the Code
     */
    public String getCode() {
        return Code;
    }

    /**
     * @param Code the Code to set
     */
    public void setCode(String Code) {
        this.Code = Code;
    }

    /**
     * @return the Quantity
     */
    public int getQuantity() {
        return Quantity;
    }

    /**
     * @param Quantity the Quantity to set
     */
    public void setQuantity(int Quantity) {
        this.Quantity = Quantity;
    }

    /**
     * @return the Usage
     */
    public String getUsage() {
        return Usage;
    }

    /**
     * @param Usage the Usage to set
     */
    public void setUsage(String Usage) {
        this.Usage = Usage;
    }

    /**
     * @return the Other
     */
    public String getOther() {
        return Other;
    }

    /**
     * @param Other the Other to set
     */
    public void setOther(String Other) {
        this.Other = Other;
    }

    /**
     * @return the Missing
     */
    public String getMissing() {
        return Missing;
    }

    /**
     * @param Missing the Missing to set
     */
    public void setMissing(String Missing) {
        this.Missing = Missing;
    }
    
}
