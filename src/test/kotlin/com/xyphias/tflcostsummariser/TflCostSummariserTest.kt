package com.xyphias.tflcostsummariser

import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.io.File

class TflCostSummariserTest {
    @Test fun `it should produce the total for a month`() {
        `there is a file called`("tfl-August-2017.csv") with("""Date,Daily Charge (GBP)
            |02/08/2017,-3.20
            |03/08/2017,-6.90
            |04/08/2017,-3.40
            """.trimMargin())

        summarise(arrayOf("tfl-August-2017.csv"), OutputCaptor::writeln)

        `the output should be`("""August 2017: 13.50
        |""".trimMargin())
    }

    @Test fun `it should display an error if the file does not exist`() {
        summarise(arrayOf("tfl-costs-January-1905.csv"), OutputCaptor::writeln)

        `the output should be`("""No such file: tfl-costs-January-1905.csv
        |""".trimMargin())
    }

    @Test fun `it should produce the total for a month over several files`() {
        `there is a file called`("tfl-August-2017-card1.csv") with("""Date,Daily Charge (GBP)
            |02/08/2017,-3.20
            |03/08/2017,-6.90
            """.trimMargin())

        `there is a file called`("tfl-August-2017-card2.csv") with("""Date,Daily Charge (GBP)
            |04/08/2017,-5.60
            """.trimMargin())

        summarise(arrayOf("tfl-August-2017-card1.csv", "tfl-August-2017-card2.csv"), OutputCaptor::writeln)

        `the output should be`("""August 2017: 15.70
        |""".trimMargin())
    }

    private fun `there is a file called`(name: String): File {
        fileNames.add(name)
        return File(name)
    }

    private infix fun File.with(contents: String) = this.writeText(contents)

    private fun `the output should be`(expectedOutput: String) {
        assertEquals(expectedOutput, OutputCaptor.written)
    }

    @Before fun clearCaptor() {
        OutputCaptor.written = ""
    }

    @After fun removeFiles() {
        fileNames.forEach { File(it).delete() }
        fileNames.clear()
    }

    private val fileNames = ArrayList<String>()
}

object OutputCaptor {
    var written: String = ""

    fun writeln(s: String) {
        written += "$s\n"
    }
}
