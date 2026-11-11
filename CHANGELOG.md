# Changelog / 更新履歴

All notable changes to `Create is Lost Technology` will be documented in this file.

---

## [1.0.0-beta] - 2026-11-11

### English
#### Added
- **Initial Beta Release for Minecraft 1.20.1 (Minecraft Forge)**.
- **Zinc Ore Disabler**:
  - Automatically disables generation of Zinc Ore (`create:zinc_ore`) and Deepslate Zinc Ore (`create:deepslate_zinc_ore`).
  - Implemented both via Forge BiomeModifier and PlacedFeature mixin for maximum compatibility with vanilla, Create, and custom worldgen mods.
- **Core Recipe Disabler**:
  - Dynamically disables recipes producing Andesite Alloy (`create:andesite_alloy`) and Andesite Alloy Blocks.
  - Dynamically disables recipes producing Cogwheels (`create:cogwheel`) and Large Cogwheels (`create:large_cogwheel`).
  - Dynamically disables recipes producing Andesite Casing (`create:andesite_casing`) AND all recipes that use Andesite Casing as an ingredient (Mechanical Presses, Mixers, Drills, Saws, Harvesters, etc.).
- **Lost Technology Tooltip**:
  - In-game bilingual tooltip indicating uncraftable lost technology items.
- **Configurability**:
  - Fully customizable options in `config/create_lost_technology-common.toml` to toggle each disabler independently and specify custom items/ingredients to blacklist.

---

### 日本語
#### 追加
- **Minecraft 1.20.1 (Minecraft Forge) 向け初期ベータ版リリース**。
- **亜鉛鉱石の生成無効化**:
  - 亜鉛鉱石（`create:zinc_ore`）および深層亜鉛鉱石（`create:deepslate_zinc_ore`）のワールド生成を完全停止。
  - Forge BiomeModifier および PlacedFeature の Mixin による二重防御設計を採用し、バニラやカスタムワールド生成MOD導入時でも確実に機能。
- **基礎レシピの無効化**:
  - 安山岩合金（`create:andesite_alloy`）および安山岩合金ブロックを完成品とする全レシピを動的に無効化。
  - 歯車（`create:cogwheel`）および大きな歯車（`create:large_cogwheel`）を完成品とする全レシピを動的に無効化。
  - 安山岩ケーシング（`create:andesite_casing`）のクラフト、および安山岩ケーシングを材料（中間素材）として使用するすべての機械レシピ（メカニカルプレス、ミキサー、ドリル、ノコギリ、収穫機等）を動的に無効化。
- **失われた技術のツールチップ表示**:
  - クラフト不可能になった遺物アイテムに日英対応の解説ツールチップを表示。
- **柔軟なコンフィグ設定**:
  - `config/create_lost_technology-common.toml` より、各機能の個別ON/OFFや、任意のアイテム・素材の追加ブラックリスト指定が可能。
