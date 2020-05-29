package rug.tbp.model

import java.math.BigDecimal
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
    val mass: BigDecimal,
    val radius: Double,
    var position: Vector,
    var velocity: Vector
) {

    constructor(mass: Double, radius: Double, position: Vector, velocity: Vector)
            : this(BigDecimal(mass), radius, position, velocity)

    val lastPositions = ArrayDeque<Vector>()
    fun getMomentum() = velocity * mass
    fun getEnergy() = BigDecimal(0.5) * mass * velocity.length.pow(2)
}