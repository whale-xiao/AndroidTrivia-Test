package com.example.android.navigation

import androidx.appcompat.app.AppCompatActivity
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [28])
class AboutFragmentTest {

    /**
     * [编号 3] 验证 AboutFragment 实例存活状态
     * 测试目标：
     * 1. 验证 AboutFragment 能否被成功实例化。
     * 2. 验证 Fragment 在被添加到 Activity 后处于正常存活状态。
     * 验证手段：使用 Robolectric 托管 Fragment，并通过 assertNotNull 验证实例。
     */
    @Test
    fun testFragmentNotNull() {
        val controller = Robolectric.buildActivity(AppCompatActivity::class.java).setup()
        // 关键点：必须设置主题，否则 Fragment 可能因样式缺失而崩溃
        controller.get().setTheme(R.style.AppTheme)

        val fragment = AboutFragment()
        controller.get().supportFragmentManager.beginTransaction()
                .add(android.R.id.content, fragment)
                .commitNow()

        assertNotNull("Fragment 实例不应为空", fragment)
    }

    /**
     * [编号 4] 验证关于页面的视图加载（不抛出异常即为通过）
     * 测试目标：
     * 1. 验证 AboutFragment 的 onCreateView 方法能正确加载布局。
     * 2. 确保 Fragment 的视图层次结构能被成功创建。
     * 验证手段：通过 commitNow 同步提交事务，并断言 fragment.view 不为空。
     */
    @Test
    fun testOnCreateView() {
        val controller = Robolectric.buildActivity(AppCompatActivity::class.java).setup()
        controller.get().setTheme(R.style.AppTheme)

        val fragment = AboutFragment()
        controller.get().supportFragmentManager.beginTransaction()
                .add(android.R.id.content, fragment)
                .commitNow()

        assertNotNull("Fragment 视图不应为空", fragment.view)
    }
}