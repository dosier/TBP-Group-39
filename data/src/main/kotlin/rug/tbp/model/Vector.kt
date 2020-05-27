package rug.tbp.model

import java.awt.geom.Point2D
import kotlin.math.sqrt

/**
 * Represents a 2D Euclidean vector.
 *
 * @author  Stan van der Bend
 * @since   27/05/2020
 * @version 1.0
 */
data class Vector(val x: Double, val y: Double) {

    val length = sqrt(this * this)

    operator fun plus(o: Vector) =
        Vector(x + o.x, y + o.y)

    operator fun minus(o: Vector) =
        Vector(x - o.x, y - o.y)

    operator fun times(o: Vector) = x*o.x + y*o.y

    operator fun times(scalar: Double) = Vector(scalar * x, scalar * y)

    operator fun div(scalar: Double) = Vector(x / scalar, y / scalar)

    fun toPoint() = Point2D.Float(x.toFloat(), y.toFloat())

    companion object {
        val ZERO = Vector(0.0, 0.0)
    }
}