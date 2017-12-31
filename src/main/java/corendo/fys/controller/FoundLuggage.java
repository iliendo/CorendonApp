package corendo.fys.controller;

import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author Ilias Azagagh
 */
public class FoundLuggage {
    // Found luggage attributes
    private String registrationNr;
    private String dateFound;
    private String timeFound;
    private String luggageType;
    private String brand;
    
    public static List<FoundLuggage> importFoundLuggageFromExcel(String fileName){
        // Create a new empty found luggage list
        List<FoundLuggage> luggageList = new ArrayList<>();
        
        // todo: process the excel file and build a list of found luggage
        
        // add some dummy found luggage
        luggageList.add(new FoundLuggage());
        luggageList.add(new FoundLuggage());
        
        return luggageList;
        
    }
    
}
