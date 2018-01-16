/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package medewerkers;

/**
 *
 * @author Gabriel
 */
public class zoek_damage {
    private String Luggage_id;
    private String LuggageTag;
    private String Flight;
    private String Passenger;

    public zoek_damage(String Luggage_id, String LuggageTag, String Flight, String Passenger) {
        this.Luggage_id = Luggage_id;
        this.LuggageTag = LuggageTag;
        this.Flight = Flight;
        this.Passenger = Passenger;
    }

    public String getLuggage_id() {
        return Luggage_id;
    }

    public String getLuggageTag() {
        return LuggageTag;
    }

    public String getFlight() {
        return Flight;
    }

    public String getPassenger() {
        return Passenger;
    }

    public void setLuggage_id(String Luggage_id) {
        this.Luggage_id = Luggage_id;
    }

    public void setLuggageTag(String LuggageTag) {
        this.LuggageTag = LuggageTag;
    }

    public void setFlight(String Flight) {
        this.Flight = Flight;
    }

    public void setPassenger(String Passenger) {
        this.Passenger = Passenger;
    }
    
    
}
