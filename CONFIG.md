# Configuration Guide / コンフィグ設定ガイド

[English](#english) | [日本語](#japanese)

---

<a name="english"></a>
## English

Configuration file location: `config/create_lost_technology-common.toml`

### Configuration Options

| Key | Type | Default | Description |
| :--- | :--- | :--- | :--- |
| `disableZincOre` | Boolean | `true` | Prevents Zinc Ore and Deepslate Zinc Ore from generating in the world. |
| `disableAndesiteAlloyRecipes` | Boolean | `true` | Disables all recipes that produce Andesite Alloy or Andesite Alloy Blocks (Crafting, Mixing, etc.). |
| `disableCogwheelRecipes` | Boolean | `true` | Disables all recipes that produce Cogwheels and Large Cogwheels. |
| `disableAndesiteCasingRecipes` | Boolean | `true` | Disables recipes that produce Andesite Casing OR require Andesite Casing as an ingredient (Presses, Mixers, Drills, Harvesters, etc.). |
| `showLostTechTooltip` | Boolean | `true` | Displays a "Lost Technology" tooltip on uncraftable Create items. |
| `customDisabledOutputItems` | List of Strings | `[]` | Additional item registry IDs whose recipes should be disabled when produced as a result (e.g. `["create:brass_casing"]`). |
| `customDisabledIngredients` | List of Strings | `[]` | Additional item registry IDs whose presence in any recipe ingredient will invalidate and remove that recipe. |

---

<a name="japanese"></a>
## 日本語

設定ファイルの場所: `config/create_lost_technology-common.toml`

### 設定項目一覧

| キー名 | データ型 | デフォルト値 | 説明 |
| :--- | :--- | :--- | :--- |
| `disableZincOre` | 真偽値 (Boolean) | `true` | 亜鉛鉱石および深層亜鉛鉱石のワールド生成を無効化します。 |
| `disableAndesiteAlloyRecipes` | 真偽値 (Boolean) | `true` | 安山岩合金および安山岩合金ブロックを成果物とするすべてのレシピ（作業台、ミキサー等）を無効化します。 |
| `disableCogwheelRecipes` | 真偽値 (Boolean) | `true` | 歯車および大きな歯車を成果物とするすべてのレシピを無効化します。 |
| `disableAndesiteCasingRecipes` | 真偽値 (Boolean) | `true` | 安山岩ケーシング自体のクラフト、および安山岩ケーシングを素材（材料）とするすべてのレシピ（メカニカルプレス、ミキサー、ドリル、収穫機等）を無効化します。 |
| `showLostTechTooltip` | 真偽値 (Boolean) | `true` | クラフト不能となったCreateアイテムに「失われた技術」ツールチップを表示します。 |
| `customDisabledOutputItems` | 文字列リスト | `[]` | 完成品としてレシピを無効化したい追加のアイテムIDリスト（例: `["create:brass_casing"]`）。 |
| `customDisabledIngredients` | 文字列リスト | `[]` | 材料に含まれていた場合にそのレシピを無効化する追加のアイテムIDリスト。 |
