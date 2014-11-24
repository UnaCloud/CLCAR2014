
package tasks.utils;

import java.util.Random;

/**
 *
 * Ing. Mario Jose Villamizar Cano
 * Universidad de los Andes
 * Bogota - Colombia
 * Maestria en Ingenieria - Sistemas y Computacion
 * mjvc007@hotmail.com - mj.villamizar24@uniandes.edu.co
 * 2009
 * 
 */

public class MyUtilies {
    /**
    * Metodo utilizado para generar un numero aleatorio double.
    * @return double - número aleatorio generado desde la distribución uniforme en el intervalo unitario [0.0,1.0)
    */
    public static double generateAleatoryDoubleNumber()
    {
        //The method random.nextDouble() returns random double >=0.0 and < 1.0
        Random random = new Random();
        double valor = 0;
        do
        {
            valor = random.nextDouble();
        }
        while(valor==(double)0);
        return  valor;
    }
}
