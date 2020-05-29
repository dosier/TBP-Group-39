package rug.tbp.util

import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode


/**
 * TODO: add documentation
 *
 * @author  Stan van der Bend (https://www.rune-server.ee/members/StanDev/)
 * @since   29/05/2020
 * @version 1.0
 */

private val SQRT_DIG = BigDecimal(150)
private val SQRT_PRE = BigDecimal(10).pow(SQRT_DIG.intValueExact())

/**
 * Private utility method used to compute the square root of a BigDecimal.
 *
 * @author Luciano Culacciatti
 * @url http://www.codeproject.com/Tips/257031/Implementing-SqrtRoot-in-BigDecimal
 */
fun sqrtNewtonRaphson(c: BigDecimal, xn: BigDecimal, precision: BigDecimal): BigDecimal {
    val fx = xn.pow(2).add(c.negate())
    val fpx = xn.multiply(BigDecimal(2))
    var xn1: BigDecimal = fx.divide(fpx, 2 * SQRT_DIG.intValueExact(), RoundingMode.HALF_DOWN)
    xn1 = xn.add(xn1.negate())
    val currentSquare = xn1.pow(2)
    var currentPrecision = currentSquare.subtract(c)
    currentPrecision = currentPrecision.abs()
    return if (currentPrecision.compareTo(precision) <= -1) {
        xn1
    } else sqrtNewtonRaphson(c, xn1, precision)
}

/**
 * Uses Newton Raphson to compute the square root of a BigDecimal.
 *
 * @author Luciano Culacciatti
 * @url http://www.codeproject.com/Tips/257031/Implementing-SqrtRoot-in-BigDecimal
 */
fun bigSqrt(c: BigDecimal): BigDecimal {
    return sqrtNewtonRaphson(c, BigDecimal(1), BigDecimal(1).divide(SQRT_PRE))
}
