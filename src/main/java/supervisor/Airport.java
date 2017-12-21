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
public class Airport {
    private int Airport_id;
    private String Airport_name;

    public Airport(int Airport_id, String Airport_name) {
        this.Airport_id = Airport_id;
        this.Airport_name = Airport_name;
    }

    public void setAirport_id(int Airport_id) {
        this.Airport_id = Airport_id;
    }

    public void setAirport_name(String Airport_name) {
        this.Airport_name = Airport_name;
    }

    public int getAirport_id() {
        return Airport_id;
    }

    public String getAirport_name() {
        return Airport_name;
    }
    
    
}
