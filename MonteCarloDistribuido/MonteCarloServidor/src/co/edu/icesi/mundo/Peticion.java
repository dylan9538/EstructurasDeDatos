package co.edu.icesi.mundo;

import java.util.ArrayList;

public class Peticion {

	private ArrayList<Double> simulaciones;
	private int noSimulacionesEsperadas;
	private ReceptorDePeticiones receptor;
	
	private int e,t,s;
	private double r, sigma;
	private long seed;
	private String nombre;
	private long tiempoInicio;
	private long tiempoFin;
	
	/**
	 * Constructor de la clase:
	 * Esta clase representa una petición y se encarga
	 * de recolectar todas las simulacione que los clientes van generando.
	 * 
	 * Cuando tiene todas las simulaciones necesarias, le pide al Servidor que termine de hacer los calculos finales
	 * **/
	
	public Peticion(ReceptorDePeticiones receptor, int M, int s, double r, int e, double sigma, int t, long seed){
	
		simulaciones = new  ArrayList<Double>(M);
		noSimulacionesEsperadas = M;
		this.receptor = receptor;
		this.s = s;
		this.r = r;
		this.e = e;
		this.sigma = sigma;
		this.t = t;
		this.seed = seed;

		this.nombre = "";
	}

	/**
	 * Nombre: guardarSimulacion
	 * Descripción: Guarda una simulación que fue enviada por algun cliente
	 * @param: simulación, el ST que fue calculado por algun cliente
	 * **/
	
	public void guardarSimulacion(double simulacion){
	
		simulaciones.add(simulacion);
		
		if (simulaciones.size() == noSimulacionesEsperadas){
			receptor.terminarCalculo();
		}
		
	}

	/**
	 * Nombre: darSimulacion
	 * Descripcion: Retorna todas las simulaciones recibidas de los clientes
	 * **/
	
	public ArrayList<Double> darSimulaciones() {
		
		return simulaciones;
		
	}

	public int getM() {
		return noSimulacionesEsperadas;
	}
	
	
	public int getE() {
		return e;
	}

	public int getT() {
		return t;
	}

	public int getS() {
		return s;
	}

	public double getR() {
		return r;
	}

	public double getSigma() {
		return sigma;
	}

	public long getSeed() {
		return seed;
	}

	public String getNombre() {
		return nombre;
	}

	public void cambiarNombre(String nombre){
		
	this.nombre = nombre;
	}

	/**
	 * Descripcion: Registra el tiempo en milisegundos en el que se comenzó a atender la Peticion
	 * Nombre: registrarTiempoInicio
	 * * **/	
	
	public void registrarTiempoInicio() {

		this.tiempoInicio = System.currentTimeMillis();
	}
	
	/**
	 * Nombre: registrarTiempoFin
	 * Descripcion: Registra el tiempo en milisegundos en el que se terminó de atender la Peticion
	 * 
	 * **/	
	
	
	public void registrarTiempoFin(){
		this.tiempoFin = System.currentTimeMillis();
	}

	public long getTiempoInicio() {
		return tiempoInicio;
	}
	
	public long getTiempoFin() {
		return tiempoFin;
	}
	
}
