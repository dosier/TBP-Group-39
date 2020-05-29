package rug.tbp.util

import org.nevec.rjm.BigDecimalMath
import rug.tbp.model.Body
import rug.tbp.model.Vector
import java.math.BigDecimal
import kotlin.math.pow

/**
 * https://github.com/ccampo133/NBodyJS/blob/master/src/main/kotlin/me/ccampo/nbody/util/Physics.kt
 */

val G = BigDecimal(1.0) // Gravitational constant
val SOFTENING_LENGTH = BigDecimal(2.0)

/**
 * Numerically integrate Newton's equations of motion using the "Velocity Verlet" algorithm.
 *
 * See [Wikipedia](https://en.wikipedia.org/wiki/Verlet_integration#Velocity_Verlet)
 *
 * @param x The position vector
 * @param v The velocity vector
 * @param dt The timestep
 * @param a The acceleration function
 */
fun verlet(x: Vector, v: Vector, dt: BigDecimal, a: (Vector) -> Vector): Pair<Vector, Vector> {
    val x1 = (x + (v * dt)) + (a(x) * (dt.pow(2) / BigDecimal(2.0)))
    val v1 = v + ((a(x) + a(x1)) * (dt / BigDecimal(2)))
    return Pair(x1, v1)
}

/**
 * Using Newton's law of universal gravitation, calculate the total acceleration vector on a two-dimension position
 * vector caused by a set of massive bodies.
 *
 * See [Wikipedia](https://en.wikipedia.org/wiki/Newton%27s_law_of_universal_gravitation)
 *
 * @param x The position vector to find the acceleration at.
 * @param bodies The set of bodies to used to contribute to the overall gravitational acceleration.
 */
fun gravityAcceleration(x: Vector, bodies: Set<Body>): Vector {
    /*
     * Newton's Law of Gravity. Here we're only returning the acceleration vector, not the force vector, according to
     * Newton's second law (F = ma)
     *
     * Also, a "softening length" is applied to modify gravitational interactions at small scales, avoiding a singularity
     * and hence crazy accelerations.
     *
     * See: http://www.scholarpedia.org/article/N-body_simulations_(gravitational)
     */
    fun gravity(pos: Vector, body2: Body): Vector {
        val r12 = body2.position - pos

        return r12 * (body2.mass * G) / BigDecimalMath.pow(r12.length.pow(2) + SOFTENING_LENGTH.pow(2),
                BigDecimal(3 / 2.0))
    }
    return bodies
        .map { body -> gravity(x, body) }
        .fold(Vector.ZERO) { a1, a2 -> a1 + a2 }
}