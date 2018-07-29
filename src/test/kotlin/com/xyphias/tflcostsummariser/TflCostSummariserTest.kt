package com.xyphias.tflcostsummariser

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
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

    @Test fun `it should also display an error about each file that does not exist`() {
        `there is a file called`("tfl-September-2017.csv") with("""Date,Daily Charge (GBP)
            |14/09/2017,-3.20
            """.trimMargin())

        summarise(arrayOf("tfl-costs-January-1905.csv", "tfl-September-2017.csv", "tfl-September-2017-VISA.csv"),
                OutputCaptor::writeln)

        `the output should be`("""September 2017: 3.20
                                 |
                                 |No such file: tfl-costs-January-1905.csv
                                 |No such file: tfl-September-2017-VISA.csv
                                 |""".trimMargin())
    }

    @Test fun `it should display an error if no files were supplied`() {
        summarise(arrayOf(), OutputCaptor::writeln)

        `the output should be`("""No files given
            |""".trimMargin())
    }

    @Test fun `it should produce the total for each month`() {
        `there is a file called`("tfl-September-2017.csv") with("""Date,Daily Charge (GBP)
            |14/09/2017,-3.20
            |15/09/2017,-6.90
            """.trimMargin())

        `there is a file called`("tfl-August-2017.csv") with("""Date,Daily Charge (GBP)
            |04/08/2017,-5.60
            |12/08/2017,-3.40
            """.trimMargin())

        summarise(arrayOf("tfl-September-2017.csv", "tfl-August-2017.csv"), OutputCaptor::writeln)

        `the output should be`("""August 2017: 9.00
            |September 2017: 10.10
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

    @BeforeEach fun clearCaptor() {
        OutputCaptor.written = ""
    }

    @AfterEach fun removeFiles() {
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
