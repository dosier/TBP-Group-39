package rug.tbp.model

import rug.tbp.util.drawBody
import rug.tbp.util.drawTrail
import rug.tbp.util.gravityAcceleration
import rug.tbp.util.verlet
import java.awt.*
import java.awt.geom.AffineTransform
import java.io.PrintWriter
import java.nio.file.Paths
import javax.swing.JPanel
import kotlin.math.roundToInt


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
            if(it.lastPositions.size >= nPos)
                it.lastPositions.removeFirst()
            it.position = newPosition
            it.velocity = newVelocity
        }

        writeLine()
        totalTime += dt
    }

    private fun writeLine() {
        writer.println()
        writer.flush()
    }

    private fun writeTime() {
        writer.print(totalTime)
    }

    private fun writePositionAndVelocity(it: Body) {
        writer.print(',')
        writer.print(it.position.x)
        writer.print(',')
        writer.print(it.position.y)
        writer.print(',')
        writer.print(it.velocity.x)
        writer.print(',')
        writer.print(it.velocity.y)
    }

    override fun paintComponent(g: Graphics) {
        val g2d = g as Graphics2D
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY)
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY)

        g2d.background = Color(44, 44, 44, 255)
        g2d.clearRect(0, 0, width, height)
        g2d.paint = Color.WHITE
        g2d.drawString("Time: ${totalTime.roundToInt()} (dt = $dt)", 20, 20)
        val tr2 = AffineTransform()
        val zoom = 40.0
        tr2.translate(
                (this.width /2) - (width*(zoom))/2,
                (this.height /2) - (height*(zoom))/2
        )
        tr2.scale(zoom,zoom)
        bodies.forEach {
            g2d.drawTrail(tr2, it.lastPositions, it.radius, width, height)
            g2d.drawBody(tr2, it, width, height)
        }
    }
}