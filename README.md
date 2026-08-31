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

## 相较原作者原版 0.8 的 1.0.1 更新说明

- 移除本地长期记忆和本地对话摘要功能，不再保存、读取或向 API 注入记忆内容。
- 保留当前聊天上下文，继续按提供商策略发送最近对话。
- 推理摘要与最终回答分离：摘要不会混入正文，API 返回的推理字段仍可在 `Assistant Thoughts` 中显示。
- 放宽回答格式提示词，不再强制“先说结论”或固定表达顺序；仅保留数学公式的标准 LaTeX 排版建议。
- 流式回答完成后对单条消息执行一次最终 Markdown/公式重渲染，清理半截公式和临时布局状态。
- 对长公式和高密度公式设置渲染预算，降低 WebView 重排造成的卡顿与内存占用。
- Room 数据库升级到新 schema；按用户选择使用破坏性迁移，不保留旧聊天和记忆数据。



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


## 本次改进：拍摄后 Google 风格裁剪

相较原作者原版，本 Fork 增加了针对拍摄问题的相机和图片截取功能，方便只把需要识别的区域发送给模型。

- 在聊天界面直接调用相机拍照
- 拍照后显示完整预览，点击右上角裁剪图标进入裁剪模式
- 裁剪模式下选区外自动变暗，选区四角使用白色标记显示
- 支持拖动四个角调整选区大小，也支持拖动选区整体移动
- 点击“附加”后才确认图片并加入聊天附件，取消时自动清理临时文件
- 支持中文裁剪界面

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
