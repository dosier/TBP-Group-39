package rug.tbp.model

import rug.tbp.util.drawBody
import rug.tbp.util.drawTrail
import rug.tbp.util.gravityAcceleration
import rug.tbp.util.verlet
import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import java.awt.Graphics2D
import java.io.PrintWriter
import java.nio.file.Paths
import javax.swing.JPanel

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
 * @param dt The timestep
 * @param initBodies The set of initial bodies
 * @param areaWidth The simulation area width
 * @param areaHeight The simulation area height
 * @param nPos The number of historical positions to track, per body
 * @param nOld The number of old bodies to track after they've been removed (collided, escaped, etc)
 */
class Simulation(
    private val dt: Double,
    private var bodies: Set<Body> = emptySet(),
    private val areaWidth: Int = 100,
    private val areaHeight: Int = 100,
    private val nPos: Int = 0,
    private val nOld: Int = 0
) : JPanel(), Runnable {


    private var totalTime = 0.0
    private val file = Paths.get("simulation.csv").toFile()
    private val writer : PrintWriter

    init {
        preferredSize = Dimension(areaWidth, areaHeight)
        size = preferredSize
        file.createNewFile()
        writer = PrintWriter(file.writer())
        writer.println("time, body1_x, body1_y, body1_vx, body1_vy, body2_x, body2_y, body2_vx, body2_vy, body3_x, body3_y, body3_vx, body3_vy")
    }

    override fun run() {

        writer.print(totalTime)

        bodies.forEach {
            writer.print(',')
            writer.print(it.position.x)
            writer.print(',')
            writer.print(it.position.y)
            writer.print(',')
            writer.print(it.velocity.x)
            writer.print(',')
            writer.print(it.velocity.y)

            val (newPosition, newVelocity) = verlet(
                it.position,
                it.velocity,
                dt
            ) { oldPosition ->
                gravityAcceleration(oldPosition, bodies - it)
            }
            it.lastPositions.addLast(it.position)
            if(it.lastPositions.size >= nPos)
                it.lastPositions.removeFirst()
            it.position = newPosition
            it.velocity = newVelocity
        }

        writer.println()
        writer.flush()
        totalTime += dt
    }

    override fun paintComponent(g: Graphics) {
        val g2d = g as Graphics2D
        g2d.paint = Color.BLACK
        g2d.fillRect(0, 0, width, height)
        bodies.forEach {
            g2d.drawTrail(it.lastPositions, it.radius, width, height)
            g2d.drawBody(it, width, height)
        }
    }
}