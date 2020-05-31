package rug.tbp.simulation.ode

typealias dy_dx = (Double, Double) -> Double


fun solveRungeKutta(f: dy_dx, x0: Double, y0: Double, x: Double, h: Double): Double{

    val steps = x.minus(x0).div(h).toInt()

    var k1: Double; var k2: Double; var k3: Double; var k4: Double

    var currentY = y0
    var currentX = x0
    for(i in 1..steps){
        k1 = h * (f.invoke(x0, currentY))
        k2 = h * (f.invoke(x0 + 0.5 * h, currentY + 0.5 * k1))
        k3 = h * (f.invoke(x0 + 0.5 * h, currentY + 0.5 * k2))
        k4 = h * (f.invoke(x0 + h, currentY + k3))

        // Update next value of y
        currentY += (1.0 / 6.0) * (k1 + 2 * k2 + 2 * k3 + k4)

        // Update next value of x
        currentX += h
    }
    return currentY
}