package rug.tbp.view

import rug.tbp.model.Simulation
import rug.tbp.util.drawBody
import rug.tbp.util.drawTrail
import java.awt.*
import java.awt.geom.AffineTransform
import javax.swing.JPanel
import kotlin.math.roundToInt

/**
 * TODO: add documentation
 *
 * @author  Stan van der Bend
 * @since   28/05/2020
 * @version 1.0
 *
 * @param simulation    the [Simulation]
 * @param areaWidth     the simulation area width
 * @param areaHeight    the simulation area height
 */
class SimulationPanel(private val simulation: Simulation,
                      areaWidth: Int = 100,
                      areaHeight: Int = 100)
    : JPanel() {

    init {
        preferredSize = Dimension(areaWidth, areaHeight)
        size = preferredSize
    }

    override fun paintComponent(g: Graphics) {
        val g2d = g as Graphics2D
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY)
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY)

        g2d.background = Color(44, 44, 44, 255)
        g2d.clearRect(0, 0, width, height)
        g2d.paint = Color.WHITE
        g2d.drawString("Time: ${simulation.totalTime.roundToInt()} (dt = ${simulation.dt})", 20, 20)
        val tr2 = AffineTransform()
        val zoom = 40.0
        tr2.translate(
                (this.width /2) - (width*(zoom))/2,
                (this.height /2) - (height*(zoom))/2
        )
        tr2.scale(zoom,zoom)
        simulation.bodies.forEach {
            g2d.drawTrail(tr2, it.lastPositions, it.radius, width, height)
            g2d.drawBody(tr2, it, width, height)
        }
    }
}