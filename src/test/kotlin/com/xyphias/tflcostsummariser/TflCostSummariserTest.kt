package com.xyphias.tflcostsummariser

import org.junit.AfterClass
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.io.File

class TflCostSummariserTest {
    @Test fun `it should produce the total for a month`() {
        `there is a file with`("""Date,Daily Charge (GBP)
            |02/08/2017,-3.20
            |03/08/2017,-6.90
            |04/08/2017,-3.40
            """.trimMargin())

        summarise(fileName, OutputCaptor::writeln)

        `the output should be`("""August 2017: 13.50
        |""".trimMargin())
    }

    @Test fun `it should display an error if the file does not exist`() {
        summarise("tfl-costs-January-1905.csv", OutputCaptor::writeln)

        `the output should be`("""No such file: tfl-costs-January-1905.csv
        |""".trimMargin())
    }

    @Before fun clearCaptor() {
        OutputCaptor.written = ""
    }

    private fun `there is a file with`(contents: String) {
        val file = File(fileName)
        file.writeText(contents)
    }

    private fun `the output should be`(expectedOutput: String) {
        assertEquals(expectedOutput, OutputCaptor.written)
    }

    companion object {
        val fileName = createTempFile().absolutePath

        @AfterClass @JvmStatic fun removeFile() {
            File(fileName).delete()
        }
    }
}

object OutputCaptor {
    var written: String = ""

    fun writeln(s: String) {
        written += "$s\n"
    }
}
