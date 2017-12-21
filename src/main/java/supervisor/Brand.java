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
public class Brand {
    private int  Brand_id;
    private String Brand;

    /**
     * This constructor connects the given values to the right attributes
     * @param Brand_id represents all Brand_id's for the object
     * @param Brand represents all Brand for the object
     */
    public Brand(int Brand_id, String Brand) {
        this.Brand_id = Brand_id;
        this.Brand = Brand;
    }

    
    public int getBrand_id() {
        return Brand_id;
    }

    public String getBrand() {
        return Brand;
    }

    public void setBrand_id(int Brand_id) {
        this.Brand_id = Brand_id;
    }

    public void setBrand(String Brand) {
        this.Brand = Brand;
    }
    
    
}
