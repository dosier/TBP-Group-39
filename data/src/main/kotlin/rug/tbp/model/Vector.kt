package rug.tbp.model

import rug.tbp.util.bigSqrt
import java.awt.geom.Point2D
import java.math.BigDecimal
import kotlin.math.sqrt

/**
 * Represents a 2D Euclidean vector.
 *
 * @author  Stan van der Bend
 * @since   27/05/2020
 * @version 1.0
 */
data class Vector(val x: BigDecimal, val y: BigDecimal) {

    constructor(x: Double, y: Double) : this(BigDecimal(x), BigDecimal(y))

    val length = bigSqrt(this*this)

    operator fun plus(o: Vector) =
        Vector(x + o.x, y + o.y)

    operator fun minus(o: Vector) =
        Vector(x - o.x, y - o.y)

    operator fun times(o: Vector) = x*o.x + y*o.y

    operator fun times(scalar: BigDecimal) = Vector(scalar * x, scalar * y)

    operator fun div(scalar: BigDecimal) = Vector(x / scalar, y / scalar)

    fun toPoint() = Point2D.Float(x.toFloat(), y.toFloat())

    companion object {
        val ZERO = Vector(BigDecimal(0.0), BigDecimal(0.0))
    }
}