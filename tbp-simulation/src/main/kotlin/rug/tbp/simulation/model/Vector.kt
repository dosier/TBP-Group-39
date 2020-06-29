package rug.tbp.simulation.model

import java.awt.geom.Point2D
import kotlin.math.pow
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

    operator fun unaryMinus() = Vector(-x, -y)

    fun toNorm() = sqrt((x*x) + (y*y))

    fun toPoint() = Point2D.Float(x.toFloat(), y.toFloat())

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Vector) return false

        if (x != other.x) return false
        if (y != other.y) return false

        return true
    }

    override fun hashCode(): Int {
        var result = x.hashCode()
        result = 31 * result + y.hashCode()
        return result
    }

    companion object {
        val ZERO = Vector(0.0, 0.0)
    }
}