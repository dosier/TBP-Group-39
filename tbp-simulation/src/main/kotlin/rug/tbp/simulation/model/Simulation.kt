package rug.tbp.simulation.model

abstract class Simulation(var bodies: Set<Body> = emptySet()) : Runnable {

    abstract var totalTime: Double

    abstract var dt: Double
}