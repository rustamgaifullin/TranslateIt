package com.rm.translateit.api.translation.wiki

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.Ignore
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import rx.observers.TestSubscriber
import rx.plugins.RxJavaHooks
import rx.schedulers.Schedulers
import java.io.File


/**
 * TODO: Figure out how to load files from classpath
 * Right now it doesn't work because json file can't be loaded
 */
@RunWith(JUnitPlatform::class)
@Ignore
class WikiTranslaterSpek: Spek ({
    given("a wiki translater") {
        val word = "WORD"
        val from = "en"
        val to = "pl"

        RxJavaHooks.setOnIOScheduler { scheduler -> Schedulers.immediate() }
        val server = MockWebServer()
        server.start()

        val sut = WikiTranslater(server.url("").toString())

        val responsePath = WikiTranslaterSpek::class.java.classLoader.getResource("wiki_translation_response.json").path
        server.enqueue(MockResponse()
                .setResponseCode(200)
                .setBody(File(responsePath).readText()))

        val testSubscriber = TestSubscriber<String>()

        on("receiving response") {
            sut.translate(word, from, to).subscribe(testSubscriber)

            it("with no error") {
                testSubscriber.assertNoErrors()
            }

            it("with response") {
                testSubscriber.assertReceivedOnNext(listOf("Translate"))
            }
        }
    }

})