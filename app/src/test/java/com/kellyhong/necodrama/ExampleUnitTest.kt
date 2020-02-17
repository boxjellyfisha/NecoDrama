package com.kellyhong.necodrama

import org.junit.Test

import org.junit.Assert.*
import java.time.OffsetDateTime

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun testDateTrans() {
        val created = "2017-12-06T22:09:45.000Z".replace("Z", "+08:00")
        print(created)
        val time = OffsetDateTime.parse(created)
        print(time.month)
    }

    @Test
    fun testKeywordTrans() {
        val keyword = "101 kk==ji3 ".trim()
        if(keyword.contains(" ")) {
            var final = ""
            val terms = keyword.split(" ")
            for (term in terms) {
                val tmp = term.replace(Regex("[^\\p{L}\\p{N}]"), "")
                final += if(final.isEmpty()) "%$tmp%" else " AND %$tmp%"
            }
            print(final)
        }
    }
}
