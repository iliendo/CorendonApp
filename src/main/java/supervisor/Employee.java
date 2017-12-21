                      package supervisor;

/**
 *
 * @author Ilias Azagagh
 */
public class Employee {
    private String Firstname;
    private String Lastname;
    private String Email;
    private String Function_name;
    private String Country_name;

    public Employee(String Firstname, String Lastname, String Email, String Function_name, String Country_name) {
        this.Firstname = Firstname;
        this.Lastname = Lastname;
        this.Email = Email;
        this.Function_name = Function_name;
        this.Country_name = Country_name;
    }
   
    public void setFirstname(String Firstname) {
        this.Firstname = Firstname;
    }
    
    public void setLastname(String Lastname) {
        this.Lastname = Lastname;
    }
    
    public void setEmail(String Email) {
        this.Email = Email;
    }
    
    public void setFunction_name(String Function_name) {
        this.Function_name = Function_name;
    }
    
    public void setCountry_name(String Country_name){
        this.Country_name = Country_name;
    }

    public String getFirstname() {
        return Firstname;
    }
    
    public String getLastname() {
        return Lastname;
    }
    
    public String getEmail() {
        return Email;
    }
    
    public String getFunction_name(){
        return Function_name;
    }
    
    public String getCountry_name(){
        return Country_name;
    }
}