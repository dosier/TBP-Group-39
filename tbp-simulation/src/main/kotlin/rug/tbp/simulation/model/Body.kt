package rug.tbp.simulation.model

import java.awt.Color
import java.util.*
import kotlin.math.pow

/**
 * Represents a celestial body moving in 2D space
 *
 * @author  Stan van der Bend
 * @since   27/05/2020
 * @version 1.0
 */
data class Body(
    val mass: Double,
    val radius: Double,
    var position: Vector,
    val color: Color,
    var velocity: Vector = Vector(0.0, 0.0)

) {
    val lastPositions = ArrayDeque<Vector>()
    fun getMomentum() = velocity * mass
    fun getEnergy() = 0.5 * mass * velocity.length.pow(2)
}