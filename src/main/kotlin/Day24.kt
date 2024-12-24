import java.nio.file.Path
import kotlin.io.path.readText


class Day24 : Day {
    override fun partOne(filename: String, verbose: Boolean): Long =
        filename.asPath().parseCircuitMap().run()

    override fun partTwo(filename: String, verbose: Boolean): String {
        val circuit = filename.asPath().parseCircuitList()
        val invalidEndWires = circuit.filter {
            it.name.first() == 'z' && it.name != "z45" && it.gate != Gate.XOR
        }

        val invalidMidWires = circuit.filter {
            it.name.first() != 'z'
                    && it.input1?.first() != 'x' && it.input1?.first() != 'y'
                    && it.input2?.first() != 'x' && it.input2?.first() != 'y'
                    && it.gate == Gate.XOR
        }

        invalidMidWires.forEach { wire ->
            val toSwitch = invalidEndWires.first { it.name == findFirstOutputFrom(circuit, wire.name) }
            val temp = wire.name
            wire.name = toSwitch.name
            toSwitch.name = temp
        }

        val xInput = interpretWireAsNumber('x', circuit)
        val yInput = interpretWireAsNumber('y', circuit)

        val diffFromActual = xInput + yInput xor circuit.run()
        val zeroBits = diffFromActual
            .countTrailingZeroBits()
            .toString()
            .padStart(2, '0')

        val invalidCarryWires = circuit.filter {
            it.input1?.endsWith(zeroBits) == true
                    && it.input2?.endsWith(zeroBits) == true
        }

        return (invalidEndWires + invalidMidWires + invalidCarryWires)
            .map { it.name }
            .sorted()
            .joinToString(",")
    }

    private fun MutableMap<String, Wire>.run() = this.keys
        .filter { Regex("""z\d+""").matches(it) }
        .sortedDescending()
        .map { if (getWireValue(it)) 1 else 0 }
        .joinToString("")
        .toLong(2)

    private fun List<Wire>.run() = this
        .filter { Regex("""z\d+""").matches(it.name) }
        .sortedByDescending { it.name }
        .map { if (getWireValue(it)) 1 else 0 }
        .joinToString("")
        .toLong(2)

    private fun Path.parseCircuitMap(): MutableMap<String, Wire> {
        val input = readText()
        val circuit = Regex("""(\w+): (\d)""").findAll(input).associateTo(mutableMapOf()) {
            val (name, value) = it.destructured
            name to Wire(name, value == "1", Gate.INPUT, null, null)
        }

        Regex("""(\w+) (AND|OR|XOR) (\w+) -> (\w+)""").findAll(input).forEach {
            val (input1, gate, input2, output) = it.destructured
            circuit[output] = Wire(output, null, Gate.valueOf(gate), input1, input2)
        }

        return circuit
    }

    private fun Path.parseCircuitList(): List<Wire> {
        val input = readText()
        return buildList {
            Regex("""(\w+): (\d)""").findAll(input).forEach {
                val (name, value) = it.destructured
                this += Wire(name, value == "1", Gate.INPUT, null, null)
            }
            Regex("""(\w+) (AND|OR|XOR) (\w+) -> (\w+)""").findAll(input).forEach {
                val (input1, gate, input2, output) = it.destructured
                this += Wire(output, null, Gate.valueOf(gate), input1, input2)
            }
        }
    }

    private fun MutableMap<String, Wire>.getWireValue(wireIdentifier: String): Boolean {
        val wireValue = this[wireIdentifier]?.value
        if (wireValue != null) return wireValue

        val wire = this[wireIdentifier]!!
        val value = when (wire.gate) {
            Gate.INPUT -> wire.value!!
            Gate.AND -> getWireValue(wire.input1!!) and getWireValue(wire.input2!!)
            Gate.OR -> getWireValue(wire.input1!!) or getWireValue(wire.input2!!)
            Gate.XOR -> getWireValue(wire.input1!!) xor getWireValue(wire.input2!!)
        }

        this[wire.name] = Wire(wire.name, value, wire.gate, wire.input1, wire.input2)
        return value
    }

    private fun List<Wire>.getWireValue(wire: Wire): Boolean {
        if (wire.value != null) return wire.value!!

        val input1 = find { it.name == wire.input1 }
        val input2 = find { it.name == wire.input2 }
        val value = when (wire.gate) {
            Gate.INPUT -> wire.value
            Gate.AND -> getWireValue(input1!!) and getWireValue(input2!!)
            Gate.OR -> getWireValue(input1!!) or getWireValue(input2!!)
            Gate.XOR -> getWireValue(input1!!) xor getWireValue(input2!!)
        }

        wire.value = value
        return value!!
    }

    private fun findFirstOutputFrom(circuit: List<Wire>, wire: String): String? {
        val parents = circuit.filter { it.input1 == wire || it.input2 == wire }

        val validOutput = parents.find { it.name.first() == 'z' }
        if (validOutput == null) return parents.firstNotNullOfOrNull { findFirstOutputFrom(circuit, it.name) }

        val previousOutputNumber = validOutput.name.drop(1).toInt() - 1
        return "z" + previousOutputNumber.toString().padStart(2, '0')
    }

    private fun interpretWireAsNumber(start: Char, circuit: List<Wire>) = circuit
        .filter { it.name.first() == start }
        .sortedByDescending(Wire::name)
        .map { if (it.value!!) '1' else '0' }
        .joinToString("")
        .toLong(2)

    private enum class Gate { AND, OR, XOR, INPUT }
    private data class Wire(var name: String, var value: Boolean?, val gate: Gate, val input1: String?, val input2: String?)

    companion object : Day.Main("Day24.txt") {
        @JvmStatic
        fun main(args: Array<String>) = main()
    }
}
