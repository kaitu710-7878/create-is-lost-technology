# Create is Lost Technology

[English](#english) | [日本語](#japanese)

---

<a name="english"></a>
## English

**Create is Lost Technology** is an immersive Minecraft Forge add-on for Minecraft 1.20.1 that transforms the Create mod into an era of ancient, forgotten relics.

In this world, the civilization that built rotational machinery has long perished. Players cannot synthesize basic kinetic materials—Zinc Ore is depleted from the underground, Andesite Alloy cannot be forged, Cogwheels cannot be crafted, and Andesite Casing cannot be constructed or used as machine housings. 

Create components and machines can no longer be mass-produced in a workshop; they must be scavenged from ancient ruins, sunken ships, village armories, dungeon vaults, and forgotten strongholds!

### Key Features

1. **Zinc Ore Generation Disabled**:
   - Zinc Ore and Deepslate Zinc Ore will never generate naturally in any dimension.
   - Dual-layered protection via Forge BiomeModifier and PlacedFeature Mixin ensures compatibility with custom world-generation mods (Terralith, Biomes O' Plenty, etc.).
2. **Andesite Alloy Uncraftable**:
   - Disables all crafting, mixing, and processing recipes that yield Andesite Alloy or Andesite Alloy Blocks.
3. **Cogwheels Uncraftable**:
   - Disables recipes that yield Cogwheels and Large Cogwheels.
4. **Andesite Casing & Casing-Based Machinery Disabled**:
   - Disables Andesite Casing fabrication recipes (applying alloy onto stripped logs).
   - Disables any recipe that requires Andesite Casing as an ingredient (Mechanical Presses, Mechanical Mixers, Mechanical Drills, Mechanical Saws, Harvesters, Encased Shafts, etc.).
5. **Relic Tooltips**:
   - Items rendered uncraftable by the mod display a helpful, immersive tooltip informing players that the item is a lost technology relic.
6. **Fully Configurable**:
   - All modules can be selectively toggled in `config/create_lost_technology-common.toml`.
   - Supports custom blacklist lists for both produced items and ingredient items.

### Installation & Requirements

- **Minecraft**: 1.20.1
- **Mod Loader**: Minecraft Forge (>= 47.1.0)
- **Dependency**: [Create](https://www.curseforge.com/minecraft/mc-mods/create) (recommended >= 0.5.1)
- Drop `create_lost_technology-1.0.4-beta.jar` into your `.minecraft/mods` folder.

### License

Distributed under the **GNU General Public License v3.0 (GPL-3.0-or-later)**. See `LICENSE` for details.

---

<a name="japanese"></a>
## 日本語

**Create is Lost Technology** は、Minecraft 1.20.1 (Forge) 向けのCreate拡張アドオンMODです。

かつて栄華を極めた回転動力の機械文明は滅び去り、その技術は「失われた超古代文明の遺物（ロストテクノロジー）」と化しました。
プレイヤー自らの手で基礎素材を新たにクラフトすることはできず、自然界の亜鉛鉱石も枯渇しています。Createの機械や部品は、ダンジョン、沈没船、廃坑、村のチェストや古代の遺構から発掘することでのみ手に入る貴重なアーティファクトとなります。

### 主な特徴

1. **亜鉛鉱石（Zinc Ore）の生成停止**:
   - 通常世界および全ディメンションにおいて、亜鉛鉱石・深層亜鉛鉱石の自然生成を完全に無効化します。
   - Forge BiomeModifier と PlacedFeature Mixin による強固な二重設計で、TerralithやBiomes O' Plenty等のワールド生成MODとも高い互換性を保持します。
2. **安山岩合金（Andesite Alloy）のレシピ無効化**:
   - 作業台やミキサーなど、安山岩合金および合金ブロックを完成品とするあらゆるレシピを無効化します。
3. **歯車（Cogwheel）のレシピ無効化**:
   - 歯車および大きな歯車を完成品とするすべてのレシピを無効化します。
4. **安山岩ケーシング（Andesite Casing）およびそれを使用するレシピの無効化**:
   - 原木への安山岩合金塗布による安山岩ケーシングの作成レシピを無効化。
   - 安山岩ケーシングを中間素材（原材料）として要求するすべての機械レシピ（メカニカルプレス、メカニカルミキサー、ドリル、ノコギリ、収穫機、ケーシング付きシャフト等）をまとめて無効化します。
5. **失われた技術のツールチップ表示**:
   - クラフト不能となった遺物アイテムに、「失われた技術」である旨を伝える没入感あふれるツールチップを表示します。
6. **柔軟なコンフィグレーション**:
   - `config/create_lost_technology-common.toml` より、各機能の個別切り替えや、任意のアイテム・素材の追加ブラックリスト設定が可能です。

### 導入要件

- **Minecraft**: 1.20.1
- **MODローダー**: Minecraft Forge (47.1.0 以上推奨)
- **前提MOD**: [Create](https://www.curseforge.com/minecraft/mc-mods/create) (0.5.1 以上推奨)
- ビルドされた `create_lost_technology-1.0.4-beta.jar` を `.minecraft/mods` フォルダに配置してください。

### ライセンス

本プロジェクトは **GNU General Public License v3.0 (GPL-3.0-or-later)** のもとで公開されています。詳細は `LICENSE` ファイルをご確認ください。
