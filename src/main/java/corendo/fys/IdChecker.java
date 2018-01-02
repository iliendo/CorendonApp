 package corendo.fys;

/**
 * Checkt of de waarde in het excel bestand een kenmerk is
 * @author Ilias Azagagh
 */
public class IdChecker {
    
    char id;
    
    public boolean isCharacter(String luggageId){
        id = luggageId.charAt(0);
        
        switch(id){
            case '0': return true;
            case '1': return true;
            case '2': return true;
            case '3': return true;
            case '4': return true;
            case '5': return true;
            case '6': return true;
            case '7': return true;
            case '8': return true;
            case '9': return true;
            default: return false;
        }
    }
    
}
