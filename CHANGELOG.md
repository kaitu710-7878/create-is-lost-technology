# Changelog / 更新履歴

All notable changes to `Create is Lost Technology` will be documented in this file.

---

## [1.0.4-beta] - 2026-11-11

### English
#### Added
- **Fundamental Root-Cause Logic for "Missing Tag" Prevention**:
  - Implemented dynamic ingredient evaluation `hasEmptyOrMissingIngredient(recipe)` that scans every ingredient across all recipe types. If any ingredient specifies an empty tag, unresolvable tag, or contains zero valid non-air items, the recipe is immediately purged as an invalid/broken recipe.
  - Eliminated the need for hardcoded per-item blacklists for broken tag recipes (such as Pulse Repeaters, Pressing recipes for absent metals, etc.).
  - Added `purgeBrokenRecipes` configuration toggle (default: `true`).
  - Added client-side listener for `RecipesUpdatedEvent` with `RecipeManagerAccessor` to re-purge recipes once all game tags are 100% bound, preventing any desynchronized tags from ever reaching JEI.

---

### 日本語
#### 追加・根本改修
- **ミッシングタグ不具合を恒久防止する「根本ロジック」の導入**:
  - 個別アイテムのリスト追加（対処療法）を廃止し、あらゆるレシピタイプの全素材を動的に走査する `hasEmptyOrMissingIngredient(recipe)` を実装。空のタグ、未解決のタグ、または有効なアイテムが0件のIngredientを要求するレシピを「破損・不完全レシピ」として100%自動パージする根本ロジックを確立。
  - パルスリピーターや、環境に存在しない金属インゴットのプレート作成レシピ（Pressing）など、空タグを含むレシピがJEIにミッシングタグとして表示される問題を完全に一掃。
  - `purgeBrokenRecipes` コンフィグオプション（デフォルト: `true`）を追加。
  - クライアント側でタグが100%確定した瞬間に発火する `RecipesUpdatedEvent` および `RecipeManagerAccessor` を導入し、JEIがレシピを構築する直前に完璧なクリーンアップを実行。

---

## [1.0.3-beta] - 2026-11-11

### English
#### Fixed
- **Fixed Missing Tags on Chassis and Gantry Shafts**:
  - Radial Chassis, Linear Chassis, Gantry Shafts, and Gantry Carriages require Andesite Alloy as an ingredient.
  - Added comprehensive ingredient scanning for Andesite Alloy and Cogwheels in addition to Andesite Casing.
  - Added Radial Chassis, Linear Chassis, Gantry Shafts, and Gantry Carriages to the `KINETIC_MACHINES` blacklist set and recipe ID filter, ensuring they are cleanly removed from RecipeManager and JEI.

---

### 日本語
#### 修正
- **ラジアルシャーシおよびガントリーシャフト等のミッシングタグ問題を解消**:
  - ラジアルシャーシ、リニアシャーシ、ガントリーシャフト、ガントリーキャリッジ等は素材に「安山岩合金」を使用しているため、素材検査処理に安山岩合金（および歯車）の検出処理を完全追加。
  - シャーシおよびガントリー系アイテムを動力機械・伝達部品セット（`KINETIC_MACHINES`）およびIDフィルターに追加し、RecipeManagerおよびJEIから確実に完全消去されるよう修正。

---

## [1.0.2-beta] - 2026-11-11

### English
#### Added
- **Comprehensive Machine Crafting Disabler**:
  - Completely purged recipes for all Create kinetic machines (Mechanical Press, Mechanical Mixer, Mechanical Saw, Mechanical Drill, Mechanical Harvester, Mechanical Plough, Mechanical Roller, Deployer, Encased Fan, Millstone, Crushing Wheel, Gearbox, Clutch, Gearshift, Rope Pulley, Windmill Bearing, Mechanical Bearing, Speedometer, Stressometer, Encased Chain Drive, Water Wheels, etc.).
  - Added new configuration toggle `disableMachineRecipes` (defaults to `true`).
  - Added machine items to the "Lost Technology" tooltip registry so JEI and inventory hover clearly displays their uncraftable relic status.
#### Fixed
- **Fixed Missing Tags in JEI & Recipe Inspection**:
  - Tag resolution during early recipe loading could fail on unbound forge tags (e.g. `forge:plates/iron`, `forge:storage_blocks/iron`), interrupting recipe filtering and leaving broken machine recipes visible in JEI with "Missing Tag" warnings.
  - Implemented multi-layered fast identification using direct recipe resource IDs, item registry IDs, and per-ingredient isolated error handling to guarantee all machine recipes are removed without relying on tag binding order.

---

## [1.0.1-beta] - 2026-11-11

### English
#### Fixed
- **Fixed Crash on World Loading/Creation**:
  - Removed invalid reference to non-existent placed feature `create:deepslate_zinc_ore` in `remove_zinc_ores.json`, which caused an `IllegalStateException: Unbound values in registry [create:deepslate_zinc_ore]`.
  - Added direct override for `data/create/forge/biome_modifier/zinc_ore.json` with `forge:none` to safely suppress Create's zinc ore injection at the source.

---

### 日本語
#### 修正
- **ワールド読み込み／作成時のクラッシュを修正**:
  - `remove_zinc_ores.json` 内に存在しない placed feature である `create:deepslate_zinc_ore` が記述されていたため、レジストリ読み込み時に `IllegalStateException: Unbound values in registry [create:deepslate_zinc_ore]` が発生していた問題を修正。
  - Create 本体の `data/create/forge/biome_modifier/zinc_ore.json` を `forge:none` で安全にオーバーライドし、Createによる亜鉛鉱石のバイオーム追加処理を元から完全に無効化。

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
