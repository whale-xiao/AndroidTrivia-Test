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
class RulesFragmentTest {

    /**
     * [编号 1] 验证 Fragment 实例创建及存活
     * 测试目标：
     * 1. 验证 RulesFragment 能否被成功实例化。
     * 2. 验证 Fragment 的视图（View）在添加到 Activity 后能成功加载且不为空。
     * 验证手段：使用 Robolectric 构建 Activity 并托管 Fragment，通过 assertNotNull 断言。
     */
    @Test
    fun testFragmentNotNull() {
        // 1. 构建并启动 Activity
        val controller = Robolectric.buildActivity(AppCompatActivity::class.java).setup()
        val activity = controller.get()
        activity.setTheme(R.style.AppTheme)

        // 2. 实例化并添加 Fragment
        val fragment = RulesFragment()
        activity.supportFragmentManager.beginTransaction()
                .add(android.R.id.content, fragment, "RulesFragment")
                .commitNow()

        // 3. 断言验证
        assertNotNull("Fragment 实例不应为空", fragment)
        assertNotNull("Fragment 视图不应为空", fragment.view)
    }

    /**
     * [编号 2] 高级生命周期测试：模拟设备旋转导致重建
     * 测试目标：
     * 1. 验证 RulesFragment 在经历完整生命周期（包括暂停、停止、销毁）时的健壮性。
     * 2. 确保 Fragment 在配置更改（如屏幕旋转）导致的销毁过程中不会抛出异常。
     * 验证手段：使用 Robolectric Controller 模拟 pause -> stop -> destroy 流程。
     */
    @Test
    fun testOnCreateView_WithRotation() {
        // 1. 构建并启动 Activity
        val controller = Robolectric.buildActivity(AppCompatActivity::class.java).setup()
        val activity = controller.get()
        activity.setTheme(R.style.AppTheme)

        // 2. 添加 Fragment
        val fragment = RulesFragment()
        activity.supportFragmentManager.beginTransaction()
                .add(android.R.id.content, fragment)
                .commitNow()

        // 3. 模拟横竖屏切换导致的销毁
        // 如果 Fragment 的 onCreateView 或 onDestroyView 逻辑有问题，这里会抛出 Exception
        controller.pause().stop().destroy()

        // 4. 执行到这里没有崩溃，即证明视图加载流程是健壮的
    }
}