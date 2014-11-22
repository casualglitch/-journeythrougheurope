/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package journeythrougheurope.game;

import application.Main;
import java.util.HashMap;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import properties_manager.PropertiesManager;
 
public class CityCoordinates {

    private HashMap cityCoords;

   /* public CityCoordinates(){
        cityCoords = new HashMap();
        cityCoords.put(this, this)
    }*/
    
    public static void main(String[] args) { 
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String citiesFile = props.getProperty(Main.JTEPropertyType.CITY_COORDS_FILE_NAME);
        BufferedReader br = null;
        String line = "";
        String delimiter = " ";
        try {
            br = new BufferedReader(new FileReader(citiesFile));
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(delimiter);
                for (String token : tokens) {
                    //cityCoords.put(token, br)
                    System.out.println(token);
                }
            }
        }
        catch (Exception e) {
            System.out.println("File encountered null line.");
        }
        
    }

}
