package rug.tbp.util

import rug.tbp.model.Body
import rug.tbp.model.Vector
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

fun Graphics2D.drawBody(transform: AffineTransform, body: Body, width: Int, height: Int){

    val x = (width.div(2) + body.position.x.toDouble())
    val y = (height.div(2) - body.position.y.toDouble())

    paint = Color.WHITE
    fill(transform.createTransformedShape(Arc2D.Double(x, y, body.radius, body.radius, 0.0, 360.0, Arc2D.OPEN)))
}

fun Graphics2D.drawTrail(
        transform: AffineTransform,
        positions: Collection<Vector>,
        radius: Double,
        width: Int,
        height: Int
){

    if(positions.isEmpty())
        return

    val first = positions.first()
    var x0 = (width.div(2) + radius + first.x.toDouble())
    var y0 = (height.div(2) - radius - first.y.toDouble())

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
        val x1 = (width.div(2) + radius.div(2) + vector.x.toDouble())
        val y1 = (height.div(2) + radius.div(2) - vector.y.toDouble())
        draw(transform.createTransformedShape(Line2D.Double(x0, y0, x1, y1)))
        x0 = x1
        y0 = y1
    }
}