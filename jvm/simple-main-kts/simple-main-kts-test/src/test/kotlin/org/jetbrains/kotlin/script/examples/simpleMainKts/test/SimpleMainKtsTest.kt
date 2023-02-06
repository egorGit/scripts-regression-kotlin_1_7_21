/*
 * Copyright 2010-2020 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */
package org.jetbrains.kotlin.script.examples.simpleMainKts.test

import org.jetbrains.kotlin.script.examples.simpleMainKts.COMPILED_SCRIPTS_CACHE_DIR_PROPERTY
import org.jetbrains.kotlin.script.examples.simpleMainKts.MainKtsEvaluationConfiguration
import org.jetbrains.kotlin.script.examples.simpleMainKts.SimpleMainKtsScript
import org.junit.Assert
import org.junit.Test
import java.io.*
import java.net.URLClassLoader
import kotlin.script.experimental.api.*
import kotlin.script.experimental.host.toScriptSource
import kotlin.script.experimental.jvm.baseClassLoader
import kotlin.script.experimental.jvm.jvm
import kotlin.script.experimental.jvmhost.BasicJvmScriptingHost
import kotlin.script.experimental.jvmhost.createJvmCompilationConfigurationFromTemplate

fun evalFile(scriptFile: File, cacheDir: File? = null): ResultWithDiagnostics<EvaluationResult> =
        withMainKtsCacheDir(cacheDir?.absolutePath ?: "") {
            val scriptDefinition = createJvmCompilationConfigurationFromTemplate<SimpleMainKtsScript>()

            val evaluationEnv = MainKtsEvaluationConfiguration.with {
                jvm {
                    baseClassLoader(null)
                }
                constructorArgs(emptyArray<String>())
                enableScriptsInstancesSharing()
            }

            BasicJvmScriptingHost().eval(scriptFile.toScriptSource(), scriptDefinition, evaluationEnv)
        }


const val TEST_DATA_ROOT = "testData"

class SimpleMainKtsTest {

    @Test
    fun `dependant script return Unit, dependant function return Any`() {
        val res = evalFile(File("$TEST_DATA_ROOT/exampleA/runner.smain.kts"))
        assertSucceeded(res)
    }

    @Test
    fun `dependant script return Function ref, dependant function return Any`() {
        val res = evalFile(File("$TEST_DATA_ROOT/exampleB/runner.smain.kts"))
        assertSucceeded(res)
    }

    @Test
    fun `dependant script return Unit, dependant function return Model`() {
        val res = evalFile(File("$TEST_DATA_ROOT/exampleC/runner.smain.kts"))
        assertSucceeded(res)
    }
    @Test
    fun `dependant script return Unit, dependant function return Model, main runner fun returns Model`() {
        val res = evalFile(File("$TEST_DATA_ROOT/exampleC/runner_with_model_return.smain.kts"))
        assertSucceeded(res)
    }
    @Test
    fun `dependant script return Unit, dependant function return Model, main runner fun returns LOCAL Model`() {
        val res = evalFile(File("$TEST_DATA_ROOT/exampleC/runner_with_local_model_return.smain.kts"))
        assertSucceeded(res)
    }
    @Test
    fun `dependant script return Function ref, dependant function return Model`() {
        val res = evalFile(File("$TEST_DATA_ROOT/exampleD/runner.smain.kts"))
        assertSucceeded(res)
    }

    @Test
    fun `dependant script return Function ref, dependant function return Model, main runner with LOCAL model return`() {
        val res = evalFile(File("$TEST_DATA_ROOT/exampleD/runner_with_local_model_return.smain.kts"))
        assertSucceeded(res)
    }

    @Test
    fun `dependant script return Function ref, dependant function return Model, main runner has model script import`() {
        val res = evalFile(File("$TEST_DATA_ROOT/exampleD/runner_with_model_import.smain.kts"))
        assertSucceeded(res)
    }

    @Test
    fun `dependant script return Function ref, dependant function return Model, main runner return LOCAL model and no model script import`() {
        val res = evalFile(File("$TEST_DATA_ROOT/exampleD/runner_with_local_model_return_and_model_script_import.smain.kts"))
        assertSucceeded(res)
    }

    @Test
    fun `dependant script return Function ref, dependant function return Model, main runner return LOCAL model and model script import NOT first position`() {
        val res = evalFile(File("$TEST_DATA_ROOT/exampleD/runner_with_local_model_return_and_model_script_import_NOT_first_position.smain.kts"))
        assertSucceeded(res)
    }



    @Test
    fun `dependant script return Function ref, dependant function return Model, main runner return LOCAL_NON_GENERIC model and model script import `() {
        val res = evalFile(File("$TEST_DATA_ROOT/exampleD/runner_with_local_non_generic_model_return_and_model_script_import.smain.kts"))
        assertSucceeded(res)
    }
    private fun assertSucceeded(res: ResultWithDiagnostics<EvaluationResult>) {
        Assert.assertTrue(
            "test failed:\n  ${res.reports.joinToString("\n  ") { it.message + if (it.exception == null) "" else ": ${it.exception}" }}",
            res is ResultWithDiagnostics.Success
        )
    }
}

private fun <T> withMainKtsCacheDir(value: String?, body: () -> T): T {
    val prevCacheDir = System.getProperty(COMPILED_SCRIPTS_CACHE_DIR_PROPERTY)
    if (value == null) System.clearProperty(COMPILED_SCRIPTS_CACHE_DIR_PROPERTY)
    else System.setProperty(COMPILED_SCRIPTS_CACHE_DIR_PROPERTY, value)
    try {
        return body()
    } finally {
        if (prevCacheDir == null) System.clearProperty(COMPILED_SCRIPTS_CACHE_DIR_PROPERTY)
        else System.setProperty(COMPILED_SCRIPTS_CACHE_DIR_PROPERTY, prevCacheDir)
    }
}
