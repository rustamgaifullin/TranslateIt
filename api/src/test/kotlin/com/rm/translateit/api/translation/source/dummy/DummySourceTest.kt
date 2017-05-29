package com.rm.translateit.api.translation.source.dummy

import com.rm.translateit.api.models.LanguageModel
import com.rm.translateit.api.models.translation.Details
import com.rm.translateit.api.models.translation.Translation
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
        RxJavaHooks.setOnIOScheduler { Schedulers.immediate() }
    }

    @After
    fun tearDown() {
        RxJavaHooks.reset()
    }

    @Test
    fun translate() {
        //given
        val testSubscriber = TestSubscriber<Translation>()
        val sut = DummySource()
        val word = "AWESOME"
        val from = LanguageModel("pl", "Polish")
        val to = LanguageModel("en", "English")

        //when
        sut.translate(word, from, to).subscribe(testSubscriber)

        //then
        testSubscriber.assertNoErrors()
        testSubscriber.assertReceivedOnNext(expectedTranslationResult())
    }

    private fun expectedTranslationResult() = listOf(
            Translation(translationItemList(), details())
    )

    private fun translationItemList() = listOf(TranslationItem(words("Translation")))

    private fun details() = Details("", "")

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