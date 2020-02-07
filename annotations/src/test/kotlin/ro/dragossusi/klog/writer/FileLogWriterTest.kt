package ro.dragossusi.klog.writer

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import ro.dragossusi.klog.logger.Logger
import java.io.File


/**
 * Klog
 *
 *
 * Copyright (C) 2020  Rachieru Dragos-Mihai
 *
 *
 * Klog is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 *
 * Klog is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 *
 * You should have received a copy of the GNU General Public License
 * along with Klog.  If not, see [License](http://www.gnu.org/licenses/) .
 */
@RunWith(JUnit4::class)
class FileLogWriterTest {

    lateinit var fileLogWriter: FileLogWriter

    @Before
    fun setUp() {
        val file = File("log.txt")
        fileLogWriter = object : FileLogWriter() {
            override val file: File = file
        }
    }

    @Test
    fun test() {
        val logger = object : Logger<String> {
            override fun log(logWriter: LogWriter, item: String) {
                logWriter.startLog()
                logWriter.write("mock" to item)
                logWriter.endLog()
            }
        }
        logger.log(fileLogWriter, "value")
        Assert.assertEquals(
            "bad result",
            fileLogWriter.file.readText(),
            "mock : value"
        )
    }

}