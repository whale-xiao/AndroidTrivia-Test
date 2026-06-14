

## 🔧 AndroidTrivia 单元测试项目

一个为问答游戏应用 AndroidTrivia 添加完整单元测试的项目。

### ✨ 功能特点

#### 📊 测试覆盖
- **全面的单元测试**：覆盖应用中所有关键的Fragment和Activity
- **环境模拟测试**：使用Robolectric模拟Android运行环境
- **UI交互测试**：通过Espresso框架测试用户界面交互

#### 🧪 测试框架集成
- **JUnit测试**：使用JUnit 4编写和管理测试用例
- **Mock对象**：利用MockK框架模拟依赖组件
- **导航测试**：测试Android Navigation组件的跳转逻辑

#### 📈 代码覆盖率
- **多工具支持**：支持JaCoCo和IntelliJ Coverage两种覆盖率工具
- **可视化报告**：生成详细的测试覆盖率报告
- **质量保障**：确保核心逻辑被充分测试

## 📦 环境配置

### 开发环境要求

| 项目 | 版本/配置 |
| --- | --- |
| **Android Studio** | 4.0.1 |
| **Gradle** | 6.1.1 |
| **JDK** | JDK 8 (AS 内置) |
| **Android SDK** | API 30 |
| **Kotlin** | 1.3.72 |
| **Navigation** | 2.3.5 (AndroidX) |

### 测试依赖库

| 依赖 | 版本 | 用途 |
| --- | --- | --- |
| JUnit | 4.13.2 | 单元测试框架 |
| Robolectric | 4.3.1 | Android 环境模拟 |
| MockK | 1.10.6 | Mock 框架 |
| Espresso | 3.4.0 | UI 测试 |
| Navigation Testing | 2.3.5 | 导航测试 |
| Fragment Testing | 1.3.6 | Fragment 测试 |
| JaCoCo | 0.8.7 | 覆盖率工具（备选） |
| IntelliJ Coverage | 自带 | 覆盖率工具（Android Studio内置） |

## 📁 项目结构

```
AndroidTrivia/
├── src/
│   ├── main/                    # 源代码
│   │   ├── java/.../            # 主要逻辑和UI代码
│   │   └── res/                 # 资源文件
│   └── test/                    # 单元测试代码
│       └── java/.../            # 测试类
│           ├── RulesFragmentTest.kt
│           ├── AboutFragmentTest.kt
│           ├── GameOverFragmentTest.kt
│           ├── GameWonFragmentTest.kt
│           ├── TitleFragmentTest.kt
│           ├── GameFragmentTest.kt
│           └── MainActivityTest.kt

```

## 📊 测试用例统计

| 测试类 | 测试方法数 |
| --- | :---: |
| RulesFragmentTest | 2 |
| AboutFragmentTest | 2 |
| GameOverFragmentTest | 4 |
| GameWonFragmentTest | 7 |
| TitleFragmentTest | 6 |
| GameFragmentTest | 10 |
| MainActivityTest | 6 |
| **合计** | **37** |

## 🔧 测试指标

### 测试类型
- **逻辑单元测试**：测试ViewModel和工具类中的业务逻辑
- **UI组件测试**：测试Fragment和Activity的生命周期和交互
- **导航测试**：验证Navigation组件在不同Fragment间的跳转
- **状态验证测试**：测试游戏状态（进行中、胜利、结束）的正确转换

### 覆盖范围
- **Fragment测试**：覆盖所有7个Fragment的主要功能
- **Activity测试**：测试MainActivity的导航和容器行为
- **边界条件**：测试异常输入和边界情况
- **集成点**：测试Fragment与ViewModel的交互

## 🙏 致谢

- 感谢AndroidTrivia原始项目[https://github.com/Srihitha18798/AndroidTrivia]
- 基于以下优秀的开源测试框架构建：
  - JUnit - 单元测试标准
  - Robolectric - Android测试模拟器
  - MockK - Kotlin原生Mock库
  - Espresso - UI测试框架


