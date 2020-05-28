package rug.tbp.model

import rug.tbp.util.gravityAcceleration
import rug.tbp.util.verlet
import java.io.PrintWriter
import java.nio.file.Paths


/**
 * TODO: add documentation
 *
 * based on https://github.com/ccampo133/NBodyJS/blob/master/src/main/kotlin/me/ccampo/nbody/model/SimulationContext.kt
 *
 * @author  Stan van der Bend
 * @since   27/05/2020
 * @version 1.0
 *
 * Runs an n-body gravity simulation using Verlet integration.
 *
 * @param dt        the time interval for calculations
 * @param bodies    the set of [Body] objects
 * @param write     set to true if data should be written to a file
 * @param maxTrail  the number of historical positions to track, per body
 */
class Simulation(
        val dt: Double,
        var bodies: Set<Body> = emptySet(),
        private val write: Boolean = false,
        private val maxTrail: Int = 0
) : Runnable {

    var totalTime = 0.0
    private val file = Paths.get("simulation.csv").toFile()
    private lateinit var writer : PrintWriter

    init {
        if (write) {
            file.createNewFile()
            writer = PrintWriter(file.writer())
            writer.println("time, body1_x, body1_y, body1_vx, body1_vy, body2_x, body2_y, body2_vx, body2_vy, body3_x, body3_y, body3_vx, body3_vy")
        }
    }

    override fun run() {

        writeTime()

        bodies.forEach {
            writePositionAndVelocity(it)

            val (newPosition, newVelocity) = verlet(
                it.position,
                it.velocity,
                dt
            ) { oldPosition ->
                gravityAcceleration(oldPosition, bodies - it)
            }
            it.lastPositions.addLast(it.position)
            if(it.lastPositions.size >= maxTrail)
                it.lastPositions.removeFirst()
            it.position = newPosition
            it.velocity = newVelocity
        }

        writeLine()
        totalTime += dt
    }

    private fun writeLine() {
        if(!this::writer.isInitialized)
            return
        writer.println()
        writer.flush()
    }

    private fun writeTime() {
        if(!this::writer.isInitialized)
            return
        writer.print(totalTime)
    }

    private fun writePositionAndVelocity(it: Body) {
        if(!this::writer.isInitialized)
            return
        writer.print(',')
        writer.print(it.position.x)
        writer.print(',')
        writer.print(it.position.y)
        writer.print(',')
        writer.print(it.velocity.x)
        writer.print(',')
        writer.print(it.velocity.y)
    }
}