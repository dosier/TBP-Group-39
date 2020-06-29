package rug.tbp.simulation.model

abstract class Simulation(var bodies: List<Body> = emptyList()) : Runnable {

    abstract var totalTime: Double

    abstract var dt: Double

    abstract fun restart()
}