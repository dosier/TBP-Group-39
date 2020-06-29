package rug.tbp.simulation.util

import rug.tbp.simulation.model.Body
import rug.tbp.simulation.model.Vector
import java.awt.Color
import java.awt.Graphics2D
import java.awt.geom.AffineTransform
import java.awt.geom.Arc2D
import java.awt.geom.Line2D


/**
 * TODO: add documentation
 *
 * @author  Stan van der Bend
 * @since   27/05/2020
 * @version 1.0
 */

fun Graphics2D.drawBody(
    transform: AffineTransform,
    body: Body
){

    val x = (clip.bounds.width.div(2) + body.position.x)
    val y = (clip.bounds.height.div(2) - body.position.y)
    val shape = transform.createTransformedShape(Arc2D.Double(x, y, body.radius, body.radius, 0.0, 360.0, Arc2D.OPEN))
    paint = Color.WHITE
    fill(shape)

    paint = Color.CYAN
    drawString("${body.position.x.round(3)}, ${body.position.y.round(3)}", shape.bounds.maxX.toInt(), shape.bounds.maxY.toInt())
}

fun Graphics2D.drawXYAxis(){

    val centerX = clip.bounds.centerX.toInt()
    val centerY = clip.bounds.centerY.toInt()

    drawLine(0, centerY, clip.bounds.width, centerY)
    drawLine(centerX, 0, centerX, clip.bounds.height)

}

fun Graphics2D.drawTrail(
    transform: AffineTransform,
    positions: Collection<Vector>,
    radius: Double
){

    if(positions.isEmpty())
        return

    val width = clipBounds.width
    val height = clipBounds.height

    val first = positions.first()
    var x0 = (width.div(2) + radius + first.x)
    var y0 = (height.div(2) - radius - first.y)

    var opacity = 0
    paint = Color(255, 255, 255, opacity)
    var index = 0
    positions.drop(1).forEach { vector ->
        val newOpacity = index.toDouble().div(positions.size).times(255).toInt()
        index++
        if(newOpacity != opacity){
            paint = Color(255, 255, 255, opacity)
            opacity = newOpacity
        }
        val x1 = (width.div(2) + radius.div(2) + vector.x)
        val y1 = (height.div(2) + radius.div(2) - vector.y)
        draw(transform.createTransformedShape(Line2D.Double(x0, y0, x1, y1)))
        x0 = x1
        y0 = y1
    }
}