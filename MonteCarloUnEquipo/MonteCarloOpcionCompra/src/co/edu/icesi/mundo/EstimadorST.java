package co.edu.icesi.mundo;

import java.util.Random;

public class EstimadorST{

	public double simularST(int S, int E, double r, double sigma, int T, Random x)  
		
{
		
		double STGenerado = -1;
		
		double otrovalor = 0.5* (Math.pow(sigma, 2));
		
		double temp = S* (  Math.exp(  (r- otrovalor)*T + ( sigma*Math.pow(T, 0.5) ) * x.nextGaussian() ) );

		STGenerado = Math.exp(-r*T)*Math.max(temp-E,0);
		
		return STGenerado;
		
	}


}
