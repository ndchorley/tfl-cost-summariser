package com.xyphias.tflcostsummariser

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.YearMonth

class ToRecordTest {
    @Test fun `it should convert a line into a record`() {
        val line = "02/08/2017,-3.20"

        val record = toRecord(line)

        assertEquals(Record(YearMonth.of(2017, 8), 3.20), record)
    }
}
