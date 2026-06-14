package com.example.android.navigation

import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDirections // 导入 NavDirections 用于 Safe Args 匹配
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
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [28])
class TitleFragmentTest {

    private fun launchFragment(): TitleFragment {
        val controller = Robolectric.buildActivity(AppCompatActivity::class.java).setup()
        controller.get().setTheme(R.style.AppTheme)
        val fragment = TitleFragment()

        controller.get().supportFragmentManager.beginTransaction()
                .add(android.R.id.content, fragment, "TAG")
                .commitNow()
        return fragment
    }

    /**
     * [编号 16] 验证起始页 Fragment 实例完整性
     * 测试目标：确保 TitleFragment 能被成功实例化和挂载
     */
    @Test
    fun testFragmentNotNull() = assertNotNull(launchFragment())

    /**
     * [编号 17] 验证起始页视图加载
     * 测试目标：确保 TitleFragment 的视图能被正确创建和加载
     */
    @Test
    fun testOnCreateView() { assertNotNull(launchFragment().view) }

    /**
     * [编号 18] 交互测试：验证“开始游戏”按钮 (playButton) 是否可见
     * 测试目标：确保 UI 中的开始游戏按钮正常显示
     */
    @Test
    fun testPlayButtonExists() {
        launchFragment()
        onView(withId(R.id.playButton)).check(matches(isDisplayed()))
    }

    /**
     * [编号 19] 导航测试：验证按钮点击后是否调用了 NavController.navigate()
     * 测试目标：确保点击开始游戏按钮后触发正确的导航动作
     */
    @Test
    fun testPlayButtonNavigatesToGame() {
        val fragment = launchFragment()
        val mockNavController = mockk<NavController>(relaxed = true)

        fragment.view?.let {
            Navigation.setViewNavController(it, mockNavController)
        }

        onView(withId(R.id.playButton)).perform(click())

        // 使用 match 块捕获 Safe Args 生成的 ActionOnlyNavDirections 并验证 actionId
        verify {
            mockNavController.navigate(
                    match<NavDirections> {
                        it.actionId == R.id.action_titleFragment_to_gameFragment
                    }
            )
        }
    }

    /**
     * [编号 20] 功能测试：验证首页的 OptionsMenu 是否被正常挂载
     * 测试目标：确保 TitleFragment 正确声明并显示了选项菜单
     */
    @Test
    fun testOptionsMenuExists() {
        val fragment = launchFragment()
        assertTrue("TitleFragment 应该声明持有 OptionMenu", fragment.hasOptionsMenu())
    }

    /**
     * [编号 21] 导航测试：验证通过菜单栏（About 选项）进行跳转的逻辑
     * 测试目标：确保点击 About 菜单项后触发正确的导航动作
     */
    @Test
    fun testOptionsMenuItemNavigates() {
        val fragment = launchFragment()

        val mockNavController = mockk<NavController>(relaxed = true)
        Navigation.setViewNavController(fragment.requireView(), mockNavController)

        val mockMenuItem = mockk<MenuItem>(relaxed = true)
        every { mockMenuItem.itemId } returns R.id.aboutFragment

        // 手动调用生命周期方法
        fragment.onOptionsItemSelected(mockMenuItem)

        // 匹配 NavigationUI.onNavDestinationSelected 底层调用的 3 参数 navigate 重载方法
        verify {
            mockNavController.navigate(
                    R.id.aboutFragment,
                    null,
                    any() // 匹配自动生成的 NavOptions
            )
        }
    }
}