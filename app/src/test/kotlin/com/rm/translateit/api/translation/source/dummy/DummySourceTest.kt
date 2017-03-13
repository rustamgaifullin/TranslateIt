package com.rm.translateit.api.translation.source.dummy

import com.rm.translateit.api.models.Language
import com.rm.translateit.api.models.translation.TranslationItem
import com.rm.translateit.api.models.translation.Words.Companion.words
import org.junit.After
import org.junit.Before
import org.junit.Test
import rx.observers.TestSubscriber
import rx.plugins.RxJavaHooks
import rx.schedulers.Schedulers

class DummySourceTest {

    @Before
    fun setUp() {
        RxJavaHooks.setOnIOScheduler { scheduler -> Schedulers.immediate() }
    }

    @After
    fun tearDown() {
        RxJavaHooks.reset()
    }

    @Test
    fun translate() {
        //given
        val testSubscriber = TestSubscriber<List<TranslationItem>>()
        val sut = DummySource()
        val word = "AWESOME"
        val from = Language("pl", "Polish")
        val to = Language("en", "English")

        //when
        sut.translate(word, from, to).subscribe(testSubscriber)

        //then
        testSubscriber.assertNoErrors()
        testSubscriber.assertReceivedOnNext(listOf(listOf(TranslationItem(words("Translation")))))
    }

    @Test
    fun suggestions() {
        //given
        val testSubscriber = TestSubscriber<String>()
        val sut = DummySource()

        //when
        sut.suggestions("AWESOME", "FU", 0)

        //then
        testSubscriber.assertNoErrors()
        testSubscriber.assertReceivedOnNext(listOf())
    }
}