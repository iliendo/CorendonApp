/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package supervisor;

/**
 *
 * @author Gabriel
 */
public class LuggageType {
    private int  LuggageType_id;
    private String LuggageType;

    public LuggageType(int LuggageType_id, String LuggageType) {
        this.LuggageType_id = LuggageType_id;
        this.LuggageType = LuggageType;
    }

    public int getLuggageType_id() {
        return LuggageType_id;
    }

    public String getLuggageType() {
        return LuggageType;
    }

    public void setLuggageType_id(int LuggageType_id) {
        this.LuggageType_id = LuggageType_id;
    }

    public void setLuggageType(String LuggageType) {
        this.LuggageType = LuggageType;
    }
    
    
}
