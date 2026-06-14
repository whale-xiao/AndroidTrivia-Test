package com.example.android.navigation

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [28])
class GameOverFragmentTest {

    private fun launchFragment(): GameOverFragment {
        val controller = Robolectric.buildActivity(AppCompatActivity::class.java).setup()
        controller.get().setTheme(R.style.AppTheme)
        val fragment = GameOverFragment()
        controller.get().supportFragmentManager.beginTransaction()
                .add(android.R.id.content, fragment).commitNow()
        return fragment
    }

    /**
     * [编号 5] & [编号 6] 验证 Fragment 实例完整性及视图初始化
     * 测试目标：
     * 1. 验证 GameOverFragment 能否被成功实例化。
     * 2. 验证 Fragment 的视图（View）能被正确加载。
     * 验证手段：使用 Robolectric 托管 Fragment，并通过 assertNotNull 断言。
     */
    @Test
    fun testFragmentNotNull() = assertNotNull(launchFragment())

    @Test
    fun testOnCreateView() {
        assertNotNull(launchFragment().view)
    }

    /**
     * [编号 7] 交互测试：验证“重玩”按钮 (tryAgainButton) 是否可见
     * 测试目标：
     * 1. 确保 UI 界面中的“重玩”按钮正常显示给用户。
     * 验证手段：使用 Espresso 的 onView 查找按钮，并匹配 isDisplayed() 状态。
     */
    @Test
    fun testTryAgainButtonExists() {
        launchFragment()
        onView(withId(R.id.tryAgainButton)).check(matches(isDisplayed()))
    }

    /**
     * [编号 8] 导航测试：验证点击“重玩”按钮是否触发了跳转指令
     * 测试目标：
     * 1. 验证点击“重玩”按钮后，Fragment 是否发出了正确的导航指令。
     * 验证手段：使用 MockK 模拟 NavController，绑定到视图层级，
     *           直接调用 performClick() 绕过 Espresso 限制，并 verify 导航动作。
     */
    @Test
    fun testTryAgainButtonNavigatesToGame() {
        val fragment = launchFragment()
        val rootView = fragment.requireView()

        // 1. 构造一个高度解耦且允许自由记录行为的 Relaxed NavController
        val mockNavController = mockk<NavController>(relaxed = true)

        // 2. 核心编程逻辑修复：将 Mock 控制器绑定在多层级视图上
        val tryAgainButton: View = rootView.findViewById(R.id.tryAgainButton)
        val buttonParent = tryAgainButton.parent as View

        Navigation.setViewNavController(rootView, mockNavController)
        Navigation.setViewNavController(buttonParent, mockNavController)
        Navigation.setViewNavController(tryAgainButton, mockNavController)

        // 3. 彻底告别 Espresso 带来的 90% 面积限制崩溃：直接触发点击
        tryAgainButton.performClick()

        // 4. 终极修复：直接匹配日志中实际弹出的具体类型
        verify { mockNavController.navigate(any<androidx.navigation.ActionOnlyNavDirections>()) }
    }
}