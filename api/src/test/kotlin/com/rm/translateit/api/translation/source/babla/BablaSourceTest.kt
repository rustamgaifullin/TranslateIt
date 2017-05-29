package com.rm.translateit.api.translation.source.babla

import com.rm.translateit.api.models.LanguageModel
import com.rm.translateit.api.models.translation.Details
import com.rm.translateit.api.models.translation.Tags.Companion.tags
import com.rm.translateit.api.models.translation.Translation
import com.rm.translateit.api.models.translation.TranslationItem
import com.rm.translateit.api.models.translation.Words.Companion.words
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.HttpException
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import rx.observers.TestSubscriber
import rx.plugins.RxJavaHooks
import rx.schedulers.Schedulers

class BablaSourceTest {
    private val bablaUrl = mock(BablaUrl::class.java)
    private val bablaHtmlParser = mock(BablaHtmlParser::class.java)
    private val bablaRestService: BablaRestService = Retrofit.Builder()
            .baseUrl("http://bab.la")
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .build()
            .create(BablaRestService::class.java)

    private val word = "WORD"
    private val from = LanguageModel("EN", "English")
    private val to = LanguageModel("PL", "Polish")

    private lateinit var server : MockWebServer
    private lateinit var sut : BablaSource
    private lateinit var testSubscriber : TestSubscriber<Translation>

    @Before
    fun setUp() {
        server = MockWebServer()
        server.start()

        testSubscriber = TestSubscriber<Translation>()

        sut = BablaSource(bablaRestService, bablaUrl, bablaHtmlParser)

        `when`(bablaUrl.construct(word, from, to))
                .thenReturn(server.url("").toString())
        `when`(bablaHtmlParser.getTranslateItemsFrom(""))
                .thenReturn(emptyList())

        RxJavaHooks.setOnIOScheduler { Schedulers.immediate() }
    }

    @After
    fun tearDown() {
        server.shutdown()

        RxJavaHooks.reset()
    }

    @Test
    fun should_successfully_return_response_with_translation() {
        //when
        server.enqueue(successfulResponseWithTranslation())
        `when`(bablaHtmlParser.getTranslateItemsFrom(""))
                .thenReturn(expectedTranslationItemList())
        sut.translate(word, from, to).subscribe(testSubscriber)

        //expect
        testSubscriber.assertNoErrors()
        testSubscriber.assertReceivedOnNext(expectedTranslation())
        testSubscriber.assertCompleted()
    }

    private fun expectedDetails() = Details("", "")

    @Test
    fun should_successfully_return_response_without_translation() {
        //when
        server.enqueue(successfulResponseWithoutTranslation())
        sut.translate(word, from, to).subscribe(testSubscriber)

        //expect
        testSubscriber.assertNoErrors()
        testSubscriber.assertReceivedOnNext(emptyList())
        testSubscriber.assertCompleted()
    }

    @Test
    fun should_successfully_handle_error() {
        //when
        server.enqueue(errorResponse())
        sut.translate(word, from, to).subscribe(testSubscriber)

        //expect
        testSubscriber.assertError(HttpException::class.java)
    }

    private fun expectedTranslation() = listOf(
            Translation(expectedTranslationItemList(), expectedDetails())
    )

    private fun expectedTranslationItemList() = listOf(
            TranslationItem(words("witaj"), tags("interjection")),
            TranslationItem(words("witam"), tags("interjection"))
    )

    private fun successfulResponseWithTranslation(): MockResponse? {
        return MockResponse()
                .setResponseCode(200)
                .setBody("")
    }

    private fun successfulResponseWithoutTranslation(): MockResponse? {
        return MockResponse()
                .setResponseCode(200)
                .setBody("")
    }

    private fun errorResponse(): MockResponse? {
        val body = "ERROR"

        return MockResponse()
                .setResponseCode(500)
                .setBody(body)
    }

    @Test
    @Ignore
    fun suggestions() {

    }

}