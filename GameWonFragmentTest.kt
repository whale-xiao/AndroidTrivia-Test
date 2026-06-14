package com.example.android.navigation

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [28])
class GameWonFragmentTest {

    /**
     * 辅助函数：启动 Fragment 并注入 Bundle 参数
     * 测试目标：封装 Fragment 启动逻辑，确保每次测试都在一致的环境中初始化
     */
    private fun launchFragment(): Pair<AppCompatActivity, GameWonFragment> {
        val controller = Robolectric.buildActivity(AppCompatActivity::class.java).setup()
        val activity = controller.get()
        activity.setTheme(R.style.AppTheme)

        val args = Bundle().apply {
            putInt("numQuestions", 3)
            putInt("numCorrect", 3)
        }

        val fragment = GameWonFragment()
        fragment.arguments = args

        activity.supportFragmentManager.beginTransaction()
                .add(android.R.id.content, fragment)
                .commitNow()

        return Pair(activity, fragment)
    }

    /**
     * [编号 9] & [编号 10] 验证 Fragment 挂载并成功接收参数 & 验证胜利页面的视图渲染
     * 测试目标：
     * 1. 验证 GameWonFragment 能否被成功实例化。
     * 2. 验证 Fragment 的视图（View）能被正确加载。
     * 3. 验证 Bundle 参数能被正确接收（在 testArgumentsReceived 中单独验证）。
     * 验证手段：使用 Robolectric 托管 Fragment，并通过 assertNotNull 断言。
     */
    @Test
    fun testFragmentNotNull() {
        val (_, fragment) = launchFragment()
        assertNotNull("Fragment 实例不应为空", fragment)
        assertNotNull("Fragment 视图不应为空", fragment.view)
    }

    /**
     * [编号 11] & [编号 12] 验证“再玩一次”按钮是否存在 & 验证按钮点击后的跳转逻辑
     * 测试目标：
     * 1. 确保 UI 中的“再玩一次”按钮正常显示。
     * 2. 验证点击按钮后触发正确的导航动作。
     * 验证手段：使用 Espresso 检查按钮可见性，使用 MockK 模拟 NavController 并验证 navigate 调用。
     */
    @Test
    fun testNextMatchButtonExists() {
        launchFragment()
        onView(withId(R.id.nextMatchButton)).check(matches(isDisplayed()))
    }

    @Test
    fun testNextMatchButtonNavigatesToGame() {
        val (_, fragment) = launchFragment()

        // 1. 创建 Mock
        val mockNavController = mockk<NavController>(relaxed = true)

        // 2. 核心：只绑定根视图（官方推荐做法）
        Navigation.setViewNavController(fragment.requireView(), mockNavController)

        // 3. 终极修复：使用原生 performClick()，彻底告别 Espresso 的 90% 面积限制
        val nextMatchButton = fragment.requireView().findViewById<View>(R.id.nextMatchButton)
        nextMatchButton?.performClick()

        // 4. 验证导航动作（保留你原来的验证逻辑）
        verify { mockNavController.navigate(any<androidx.navigation.ActionOnlyNavDirections>()) }
    }

    /**
     * [编号 13] 数据测试：验证通过 Bundle 传递的分数信息是否被正确接收
     * 测试目标：
     * 1. 验证 Fragment 的 arguments 不为空。
     * 2. 验证 Bundle 中传递的分数信息（numCorrect 和 numQuestions）正确无误。
     * 验证手段：使用 JUnit 的 assertEquals 断言 Bundle 中的值。
     */
    @Test
    fun testArgumentsReceived() {
        val (_, fragment) = launchFragment()
        val args = fragment.arguments
        assertNotNull("Arguments 不应为空", args)
        assertEquals("numCorrect 应该等于 3", 3, args?.getInt("numCorrect"))
        assertEquals("numQuestions 应该等于 3", 3, args?.getInt("numQuestions"))
    }

    /**
     * [编号 14] 功能测试：验证分享 Intent 是否被正确构建（逻辑模拟）
     * 测试目标：
     * 1. 验证点击分享菜单项后，系统发出了正确的 Intent。
     * 2. 验证 Intent 的 action 和 type 是否符合分享文本的预期。
     * 验证手段：使用 Robolectric 的 ShadowActivity 拦截并验证 Intent。
     */
    @Test
    fun testShareIntentCreated() {
        val (activity, fragment) = launchFragment()

        val mockMenuItem = mockk<MenuItem>()
        every { mockMenuItem.itemId } returns R.id.share

        // 直接调用菜单点击
        fragment.onOptionsItemSelected(mockMenuItem)

        // 使用 Robolectric Shadow 验证 Intent
        val shadowActivity = shadowOf(activity)
        val startedIntent = shadowActivity.nextStartedActivity

        assertNotNull("分享操作应当触发一个 Intent", startedIntent)
        assertEquals("Intent action 应为 ACTION_SEND", Intent.ACTION_SEND, startedIntent.action)
        assertEquals("Intent type 应为 text/plain", "text/plain", startedIntent.type)
    }

    /**
     * [编号 15] UI 测试：验证 OptionsMenu（分享菜单）是否存在
     * 测试目标：
     * 1. 验证 GameWonFragment 正确声明并显示了分享菜单。
     * 验证手段：使用 Fragment 的 hasOptionsMenu() 方法断言。
     */
    @Test
    fun testOptionsMenuExists() {
        val (_, fragment) = launchFragment()
        assertTrue("胜利界面应当注入分享菜单", fragment.hasOptionsMenu())
    }
}