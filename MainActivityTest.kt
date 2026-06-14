package com.example.android.navigation

import android.os.Looper
import android.view.Gravity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.Shadows.shadowOf
import org.robolectric.android.controller.ActivityController
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowLooper

@RunWith(AndroidJUnit4::class)
@Config(sdk = [28])
class MainActivityTest {

    // 启动 Activity，只用 create + start，不用 resume 避免 RecyclerView layout bug
    private fun launch(): MainActivity {
        val a = Robolectric.buildActivity(MainActivity::class.java).create().start().get()
        ShadowLooper.runUiThreadTasks()
        shadowOf(Looper.getMainLooper()).idle()
        return a
    }

    // ============================================================
    // 1. Activity 不为 null
    // ============================================================
    @Test
    fun testActivityNotNull() {
        assertNotNull(launch())
    }

    // ============================================================
    // 2. NavController 存在
    // ============================================================
    @Test
    fun testNavControllerExists() {
        val a = launch()
        assertNotNull(a.findNavController(R.id.myNavHostFragment))
    }

    // ============================================================
    // 3. DrawerLayout 存在
    // ============================================================
    @Test
    fun testDrawerLayoutExists() {
        val a = launch()
        val f = MainActivity::class.java.getDeclaredField("drawerLayout")
        f.isAccessible = true
        assertNotNull(f.get(a) as? DrawerLayout)
    }

    // ============================================================
    // 4. 非起始目的地时 Drawer 锁定
    // ============================================================
    @Test
    fun testDrawerLockedWhenNotOnStart() {
        val a = launch()
        val nav = a.findNavController(R.id.myNavHostFragment)
        nav.navigate(R.id.gameFragment)
        ShadowLooper.runUiThreadTasks()
        shadowOf(Looper.getMainLooper()).idle()

        val f = MainActivity::class.java.getDeclaredField("drawerLayout")
        f.isAccessible = true
        val dl = f.get(a) as DrawerLayout
        assertEquals(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, dl.getDrawerLockMode(Gravity.LEFT))
    }

    // ============================================================
    // 5. 起始目的地时 Drawer 解锁
    // ============================================================
    @Test
    fun testDrawerUnlockedOnStart() {
        val a = launch()
        val f = MainActivity::class.java.getDeclaredField("drawerLayout")
        f.isAccessible = true
        val dl = f.get(a) as DrawerLayout
        assertEquals(DrawerLayout.LOCK_MODE_UNLOCKED, dl.getDrawerLockMode(Gravity.LEFT))
    }

    // ============================================================
    // 6. onSupportNavigateUp 返回 true
    // ============================================================
    @Test
    fun testOnSupportNavigateUp() {
        val a = launch()
        val nav = a.findNavController(R.id.myNavHostFragment)
        nav.navigate(R.id.gameFragment)
        ShadowLooper.runUiThreadTasks()
        shadowOf(Looper.getMainLooper()).idle()
        assertTrue(a.onSupportNavigateUp())
    }
}
