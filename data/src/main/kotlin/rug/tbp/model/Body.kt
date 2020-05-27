package rug.tbp.model

import java.util.ArrayDeque
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
    var velocity: Vector
) {
    val lastPositions = ArrayDeque<Vector>()
    fun getMomentum() = velocity * mass
    fun getEnergy() = 0.5 * mass * velocity.length.pow(2)
}