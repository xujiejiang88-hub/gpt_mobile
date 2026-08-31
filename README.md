<div align="center">

<img width="200" height="200" style="display: block;" src="./images/logo.png">

# GPT Mobile

## Multi-provider AI chat and optional on-device agents for Android.

<p>
  <a href="https://mailchi.mp/kotlinweekly/kotlin-weekly-431"><img alt="Kotlin Weekly" src="https://img.shields.io/badge/Kotlin%20Weekly-%23431-blue"/></a>
  <img alt="Android" src="https://img.shields.io/badge/Platform-Android-green.svg"/>
  <img alt="GitHub Actions Workflow Status" src="https://img.shields.io/github/actions/workflow/status/Taewan-P/gpt_mobile/release-build.yml">
  <a href="https://hosted.weblate.org/engage/gptmobile/"><img src="https://hosted.weblate.org/widget/gptmobile/gptmobile/svg-badge.svg" alt="Translation status" /></a>
  <a href="https://github.com/Taewan-P/gpt_mobile/releases/"><img alt="GitHub Releases Total Downloads" src="https://img.shields.io/github/downloads/Taewan-P/gpt_mobile/total?label=Downloads&logo=github"/></a>
  <a href="https://github.com/Taewan-P/gpt_mobile/releases/latest/"><img alt="GitHub Releases (latest by date)" src="https://img.shields.io/github/v/release/Taewan-P/gpt_mobile?color=black&label=Stable&logo=github"/></a>
</p>


</div>

## Screenshots

<div align="center">

<img style="display: block;" src="./images/screenshots.webp">

</div>

## Demos


| <video src="https://github.com/Taewan-P/gpt_mobile/assets/27392567/96229e6d-6795-48b4-a915-aca915bd2527"/> | <video src="https://github.com/Taewan-P/gpt_mobile/assets/27392567/1cc13413-7320-4f6f-ace9-de76de58adcc"/> | <video src="https://github.com/Taewan-P/gpt_mobile/assets/27392567/546e2694-953d-4d67-937f-a29fba81046f"/> |
|------------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------|


## Features

- **Chat with multiple models at once**
  - Uses official APIs for each platforms
  - Supported platforms:
    - OpenAI GPT
    - Anthropic Claude
    - Google Gemini
    - Groq
    - Ollama
  - Can customize temperature, top p (Nucleus sampling), and system prompt
  - Custom API URLs, Custom Models are also supported
- **Optional agent tools per provider profile**
  - Native tool calling with OpenAI, OpenAI-compatible/Groq, Anthropic, and Gemini
  - Firecrawl, Perplexity, or Exa web search and hardened URL reading
  - MCP Streamable HTTP servers with public, bearer, or OAuth authentication
  - Parallel runs, persistent traces, cancellation, and foreground progress
  - Existing and newly migrated profiles remain chat-only until tools are assigned
- Local chat history
  - Chat history is **only saved locally**
  - Credentials are encrypted with Android Keystore and excluded from backup/export
  - During chats, requests go only to selected model providers and assigned tools. Opening a profile's Tools dialog may contact every saved MCP server for discovery and may include its bearer or OAuth credential.
- [Material You](https://m3.material.io/) style UI, Icons
  - Supports dark mode, system dynamic theming **without Activity restart**
- Per app language setting for Android 13+
- 100% Kotlin, Jetpack Compose, Single Activity, [Modern App Architecture](https://developer.android.com/topic/architecture#modern-app-architecture) in Android developers documentation


## 本 Fork 更新说明

感谢原作者 [Taewan-P](https://github.com/Taewan-P) 开源 GPT Mobile。本项目在原作者项目的基础上独立维护，以下内容仅记录本 Fork 自身的更新，不代表原作者项目的版本变更。

### 本次改进：拍摄后 Google 风格裁剪

新增针对拍摄问题的相机和图片截取功能，方便只把需要识别的区域发送给模型。

- 在聊天界面直接调用相机拍照
- 拍照后显示完整预览，点击右上角裁剪图标进入裁剪模式
- 裁剪模式下选区外自动变暗，选区四角使用白色标记显示
- 支持拖动四个角调整选区大小，也支持拖动选区整体移动
- 点击“附加”后才确认图片并加入聊天附件，取消时自动清理临时文件
- 支持中文裁剪界面

### 本 Fork 1.0.1 版本更新声明

本声明只描述本 Fork 当前代码中的实际改动，不对原作者项目的功能、设计或版本路线作比较或归因。

- **界面与主题**：设置项改为可随内容展开的卡片布局，避免第二行文字、数值或开关被固定高度裁切；磨砂表面采用单层绘制；主要按钮、发送按钮、开关和选择控件统一使用橙色强调色。
- **聊天附件**：在聊天输入区通过“+”集中进入文件选择或相机拍摄；拍照后支持完整预览、四角裁剪、整体移动、确认附加和取消清理临时文件。
- **流式输出**：保留实时输出，同时对 Markdown、代码和 LaTeX 公式使用分块解析、渲染预算和缓存；回答完成后对当前消息进行一次最终重渲染，减少半截公式、符号混排和布局重排造成的卡顿。极长或高密度公式仍可能受到设备 WebView 性能影响。
- **推理控制**：支持在聊天界面调整 Low、Medium、High 推理级别，并在设置中选择推理状态/摘要显示方式及中文或英文显示。实际请求参数按提供商适配器发送；自定义接口只有在自身支持对应字段时才会改变服务端推理强度。
- **推理内容边界**：推理摘要与最终回答分开保存和显示，最终正文不会被强制加入“先说结论”等固定结构；只有 API 返回可展示的推理字段时，`Assistant Thoughts` 才会显示内容，不承诺暴露模型的隐藏思维链。
- **本地记忆**：移除本地长期记忆、用户画像和本地对话摘要模块，不再把这些内容注入 API。当前对话上下文和原有聊天记录机制仍按应用现有流程工作。
- **数据与兼容**：Room `ChatDatabaseV2` schema 更新到 14；当前数据库配置包含破坏性迁移回退，因此缺少可用迁移路径时不会保留旧数据库内容。
- **运行方式**：未新增自托管服务器；远程对话仍通过用户配置的 API 提供商完成，本地模型能力保持为原项目已有的可选功能。


### 中转站配置说明

部分中转站只提供 OpenAI Chat Completions 接口，不提供 Responses 接口。此类服务请在应用中选择“自定义提供商”或“OpenAI Compatible”，并填写该服务提供的 API 地址：

```text
https://你的中转站地址/v1/
```

不要在公开仓库、截图或 issue 中提交 API Key。


## Agent documentation

See [Agent tools, privacy, and security](docs/agent-tools.md) and the [0.8.0 release notes](docs/release-notes-v0.8.0.md).

If you have any feature requests, please open an issue.


## Downloads

You can download the app from the following sites:

[<img height="80" alt="Get it on F-Droid" src="https://fdroid.gitlab.io/artwork/badge/get-it-on.png"/>](https://f-droid.org/packages/dev.chungjungsoo.gptmobile)
[<img height="80" alt='Get it on Google Play' src='https://play.google.com/intl/en_us/badges/static/images/badges/en_badge_web_generic.png'/>](https://play.google.com/store/apps/details?id=dev.chungjungsoo.gptmobile&utm_source=github&utm_campaign=gh-readme)
[<img height="80" alt='Get it on GitHub' src='https://raw.githubusercontent.com/Kunzisoft/Github-badge/main/get-it-on-github.png'/>](https://github.com/Taewan-P/gpt_mobile/releases)

Cross platform updates are supported. However, GitHub Releases will be the fastest track among the platforms since there is no verification/auditing process. (Probably 1 week difference?)



## Contributions

Contributions are welcome! The contribution guideline is not yet available, but I will be happy to review it! 💯

For translations, we are using [Hosted Weblate](https://hosted.weblate.org/engage/gptmobile/). If you want your language supported, help us translate the app!

<a href="https://hosted.weblate.org/engage/gptmobile/">
  <img src="https://hosted.weblate.org/widget/gptmobile/gptmobile/multi-auto.svg" alt="Translation status" />
</a>


## Star History

[![Star History Chart](https://api.star-history.com/svg?repos=Taewan-P/gpt_mobile&type=Timeline)](https://star-history.com/#Taewan-P/gpt_mobile&Timeline)


## License

See [LICENSE](./LICENSE) for details.

[F-Droid Icon License](https://gitlab.com/fdroid/artwork/-/blob/master/fdroid-logo-2015/README.md)
