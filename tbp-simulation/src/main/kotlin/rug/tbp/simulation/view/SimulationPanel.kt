package rug.tbp.simulation.view

import rug.tbp.simulation.model.Simulation
import rug.tbp.simulation.util.round
import rug.tbp.simulation.util.drawBody
import rug.tbp.simulation.util.drawTrail
import rug.tbp.simulation.util.drawXYAxis
import java.awt.*
import java.awt.geom.AffineTransform
import javax.swing.JPanel

/**
 * TODO: add documentation
 *
 * @author  Stan van der Bend
 * @since   28/05/2020
 * @version 1.0
 *
 * @param simulation    the [RealTimeSimulation]
 * @param zoom          a scaling multiplier (applied to bodies and their trails)
 * @param areaWidth     the simulation area width
 * @param areaHeight    the simulation area height
 */
class SimulationPanel(private val zoom: Double = 100.0,
                      areaWidth: Int = 100,
                      areaHeight: Int = 100)
    : JPanel() {

    var simulation: Simulation? = null

    private fun createTransform() = AffineTransform().also {
        it.translate(
                (this.width / 2) - (width * (zoom)) / 2,
                (this.height / 2) - (height * (zoom)) / 2
        )
        it.scale(zoom, zoom)
    }

    init {
        preferredSize = Dimension(areaWidth, areaHeight)
        size = preferredSize
    }

    override fun paintComponent(g: Graphics) {

        val sim = simulation?:return

        val g2d = g as Graphics2D
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY)
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY)

        g2d.background = Color(44, 44, 44, 255)
        g2d.clearRect(0, 0, width, height)

        g2d.paint = Color.WHITE
        g2d.drawString("Time: ${sim.totalTime.round(3)} (dt = ${sim.dt})", 20, 20)

        val transform = createTransform()
        sim.bodies.forEach {
            g2d.drawXYAxis()
            g2d.drawTrail(transform, it.lastPositions, it.radius, width, height)
            g2d.drawBody(transform, it, width, height)
        }
    }
}