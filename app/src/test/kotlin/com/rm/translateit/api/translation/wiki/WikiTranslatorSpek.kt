package com.rm.translateit.api.translation.wiki

import com.rm.translateit.api.models.Language
import com.rm.translateit.api.models.translation.TranslationItem
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.Ignore
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
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
class WikiTranslatorSpek : Spek ({
    given("a wiki translater") {
        val wikiUrl = mock(WikiUrl::class.java)
        val word = "WORD"
        val from = Language("EN", "English")
        val to = Language("PL", "Polish")

        RxJavaHooks.setOnIOScheduler { scheduler -> Schedulers.immediate() }
        val server = MockWebServer()
        server.start()

        val sut = WikiTranslator(wikiUrl)

        val responsePath = WikiTranslatorSpek::class.java.classLoader.getResource("wiki_translation_response.json").path
        server.enqueue(MockResponse()
                .setResponseCode(200)
                .setBody(File(responsePath).readText()))
        `when`(wikiUrl.construct(word, from, to))
                .thenReturn(server.url("").toString())

        val testSubscriber = TestSubscriber<List<TranslationItem>>()

        on("receiving response") {
            sut.translate(word, from, to).subscribe(testSubscriber)

            it("with no error") {
                testSubscriber.assertNoErrors()
            }

            it("with response") {
                testSubscriber.assertReceivedOnNext(listOf(listOf(TranslationItem("Translate"))))
            }
        }
    }

})