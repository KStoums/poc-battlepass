# Battlepass

**Battlepass** is a flexible and lightweight Minecraft plugin (Java) that adds a Battlepass system to your server. Players can unlock rewards by leveling up through player or mob kills.

---

## 🔧 Features

- 📜 **Battlepass menu**: `/battlepass` to open the UI.
- 🧑‍💻 **Admin commands**:
  - `/battlepass-admin resetplayer <player>` – Reset a player's profile.
  - `/battlepass-admin setlevel <level> <player>` – Set a player's level.
  - `/battlepass-admin addlevel <level> <player>` – Add levels to a player.
  - `/battlepass-admin reloadrewards` – Reload rewards data
- 🧩 **Configurable rewards**: Define Battlepass items in a JSON file. Each item can include:
  - `material`: The Minecraft item type (e.g. DIAMOND_SWORD)
  - `name`: Custom item name
  - `description`: A list of lore lines
  - `level`: Required level to unlock
  - `command`: Command executed when reward is unlocked
  - `premium`: Boolean indicating if the item is for premium users
- 🗃️ **Custom storage system**: Players and items are stored in JSON by default, but you can implement your own storage by creating a new `PlayerRepository`, `RewardRepository`, or item provider.
- 🧠 **Optimized performance**:
  - Player data is cached on plugin load.
  - Data is saved every 5 minutes and on shutdown to reduce file I/O.
  - Codebase is clean and flexible, making future additions or changes easy to implement.
- ⚔️ **Progression system**: Players gain experience and level up by killing mobs or other players.

---

## 🧪 Design & Extensibility

- Clean and intuitive interfaces to extend the plugin.
- Battlepass items navigation is currently hardcoded in the menu for simplicity.
- Designed as a proof-of-concept for a private project but **open for reuse**.
  - **This plugin is shared publicly and freely for the enjoyment of the community — please do not claim it or its code as your own.**
