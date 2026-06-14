package com.example.android.navigation

import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [28])
class GameFragmentTest {

    private lateinit var navController: TestNavHostController

    @Before
    fun setup() {
        navController = TestNavHostController(ApplicationProvider.getApplicationContext())
        try { navController.setGraph(R.navigation.navigation) } catch (_: Exception) {}
    }

    // 在 AppCompatActivity 中托管并返回 GameFragment
    private fun hostFragment(): GameFragment {
        val ctrl = Robolectric.buildActivity(AppCompatActivity::class.java).setup()
        ctrl.get().setTheme(R.style.AppTheme)
        val f = GameFragment()
        ctrl.get().supportFragmentManager.beginTransaction()
            .add(android.R.id.content, f).commitNow()
        return f
    }

    // ============================================================
    // 1. Fragment 不为 null
    // ============================================================
    @Test
    fun testFragmentNotNull() {
        assertNotNull(hostFragment())
    }

    // ============================================================
    // 2. onCreateView 视图不为 null
    // ============================================================
    @Test
    fun testOnCreateView() {
        assertNotNull(hostFragment().view)
    }

    // ============================================================
    // 3. randomizeQuestions() 重置 questionIndex = 0
    // ============================================================
    @Test
    fun testRandomizeQuestions() {
        val f = hostFragment()
        val m = GameFragment::class.java.getDeclaredMethod("randomizeQuestions")
        m.isAccessible = true; m.invoke(f)
        val idx = GameFragment::class.java.getDeclaredField("questionIndex")
        idx.isAccessible = true
        assertEquals(0, idx.getInt(f))
    }

    // ============================================================
    // 4. setQuestion() 正确设置题目
    // ============================================================
    @Test
    fun testSetQuestion() {
        val f = hostFragment()
        val m = GameFragment::class.java.getDeclaredMethod("randomizeQuestions")
        m.isAccessible = true; m.invoke(f)
        assertNotNull(f.currentQuestion)
        assertTrue(f.currentQuestion.text.isNotEmpty())
        assertEquals(4, f.currentQuestion.answers.size)
    }

    // ============================================================
    // 5. numQuestions = 3
    // ============================================================
    @Test
    fun testNumQuestions() {
        val f = hostFragment()
        val field = GameFragment::class.java.getDeclaredField("numQuestions")
        field.isAccessible = true
        assertEquals(3, field.getInt(f))
    }

    // ============================================================
    // 6. 正确答案 → questionIndex 自增
    // ============================================================
    @Test
    fun testCorrectAnswerNavigatesToNext() {
        val f = hostFragment()
        val idx = GameFragment::class.java.getDeclaredField("questionIndex")
        idx.isAccessible = true
        val ci = idx.getInt(f)
        val nf = GameFragment::class.java.getDeclaredField("numQuestions")
        nf.isAccessible = true
        if (ci >= nf.getInt(f) - 1) {
            idx.setInt(f, 0)
            f.javaClass.getDeclaredMethod("setQuestion").apply { isAccessible = true }.invoke(f)
        }
        val rg = f.view?.findViewById<RadioGroup>(R.id.questionRadioGroup)
        val ai = f.answers.indexOf(f.currentQuestion.answers[0])
        rg?.check(listOf(R.id.firstAnswerRadioButton, R.id.secondAnswerRadioButton,
            R.id.thirdAnswerRadioButton, R.id.fourthAnswerRadioButton)[ai])
        f.view?.findViewById<android.view.View>(R.id.submitButton)?.callOnClick()
        assertTrue(idx.getInt(f) >= 1)
    }

    // ============================================================
    // 7. 最后一题答对 → gameWonFragment
    // ============================================================
    @Test
    fun testCorrectAnswerOnLastQuestionNavigatesToWin() {
        val f = hostFragment()
        Navigation.setViewNavController(f.requireView(), navController)
        navController.navigate(R.id.gameFragment)
        val idx = GameFragment::class.java.getDeclaredField("questionIndex")
        idx.isAccessible = true; idx.setInt(f, 2)
        f.javaClass.getDeclaredMethod("setQuestion").apply { isAccessible = true }.invoke(f)

        val rg = f.view?.findViewById<RadioGroup>(R.id.questionRadioGroup)
        val ai = f.answers.indexOf(f.currentQuestion.answers[0])
        rg?.check(listOf(R.id.firstAnswerRadioButton, R.id.secondAnswerRadioButton,
            R.id.thirdAnswerRadioButton, R.id.fourthAnswerRadioButton)[ai])
        f.view?.findViewById<android.view.View>(R.id.submitButton)?.callOnClick()
        assertEquals(R.id.gameWonFragment, navController.currentDestination?.id)
    }

    // ============================================================
    // 8. 错误答案 → gameOverFragment
    // ============================================================
    @Test
    fun testWrongAnswerNavigatesToGameOver() {
        val f = hostFragment()
        Navigation.setViewNavController(f.requireView(), navController)
        navController.navigate(R.id.gameFragment)
        val rg = f.view?.findViewById<RadioGroup>(R.id.questionRadioGroup)
        val correct = f.currentQuestion.answers[0]
        var wi = 0
        for (i in f.answers.indices) { if (f.answers[i] != correct) { wi = i; break } }
        rg?.check(listOf(R.id.firstAnswerRadioButton, R.id.secondAnswerRadioButton,
            R.id.thirdAnswerRadioButton, R.id.fourthAnswerRadioButton)[wi])
        f.view?.findViewById<android.view.View>(R.id.submitButton)?.callOnClick()
        assertEquals(R.id.gameOverFragment2, navController.currentDestination?.id)
    }

    // ============================================================
    // 9. 未选答案 → 点击无操作
    // ============================================================
    @Test
    fun testNoAnswerSelectedDoesNothing() {
        val f = hostFragment()
        val idx = GameFragment::class.java.getDeclaredField("questionIndex")
        idx.isAccessible = true
        val before = idx.getInt(f)
        f.view?.findViewById<RadioGroup>(R.id.questionRadioGroup)?.clearCheck()
        f.view?.findViewById<android.view.View>(R.id.submitButton)?.callOnClick()
        assertEquals(before, idx.getInt(f))
    }

    // ============================================================
    // 10. 4个 RadioButton 存在
    // ============================================================
    @Test
    fun testRadioButtonsExist() {
        val v = hostFragment().view
        assertNotNull(v?.findViewById<RadioButton>(R.id.firstAnswerRadioButton))
        assertNotNull(v?.findViewById<RadioButton>(R.id.secondAnswerRadioButton))
        assertNotNull(v?.findViewById<RadioButton>(R.id.thirdAnswerRadioButton))
        assertNotNull(v?.findViewById<RadioButton>(R.id.fourthAnswerRadioButton))
    }
}
