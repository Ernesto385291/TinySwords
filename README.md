# Tiny Swords

<img width="958" height="336" alt="Screen_Shot_2024-12-02_at_23 38 13" src="https://github.com/user-attachments/assets/3cc975b2-892a-48dc-8701-d65015698868" /> <!--![Uploading Screen_Shot_2024-12-02_at_23.38.13.webp…]()
 Replace with actual logo or screenshot if available -->

Tiny Swords is a 2D adventure game developed in pure Java (no external libraries), where you play as a knight exploring a world filled with enemies and resources. The focus is on simple combat, exploration, and strategy to complete objectives like defeating goblins, collecting meat and gold, and surviving challenges.

This project was created as a personal learning exercise in object-oriented programming (OOP) with Java, handling game loops, animations, collisions, and more.

## Features

- **Player Character**: A knight with 3 lives, attack capabilities in 4 directions, walking/idle/attack animations, and brief invincibility after taking damage.
- **Enemies**:
  - Goblins: Patrol areas, chase the player, attack with cooldown, 2 HP, drop gold when defeated.
  - Sheep: Move randomly, bounce animations, require 2 hits to defeat, drop meat for health restoration.
- **Resources**:
  - Meat: Restores player health.
  - Gold: Required to meet objectives.
- **Objectives** (Complete all to win):
  1. Eliminate 4 goblins.
  2. Collect 5 meats.
  3. Defeat 3 sheep.
  4. Collect 8 sacks of gold.
- **Game Mechanics**: Simple collision detection, resource drops with animations, level progression (though basic), and UI for life and objectives.
- **Audio**: Basic sound effects and music (converted to WAV due to Java limitations).
- **Title Screen**: Animated background with clouds and centered text.

## Requirements

- Java Development Kit (JDK) 8 or higher (tested with Java's built-in features only).

## Controls

- **W**: Move up
- **S**: Move down
- **A**: Move left
- **D**: Move right
- **SPACE**: Attack
- **ENTER**: Start game (from title screen)

## Development Challenges

During development, several issues arose:

- **Concept Design**: Creating a top-down adventure required balancing simple mechanics with engaging gameplay, including precise collision detection and tile-based level design.
- **Audio Handling**: Java lacks native MP3 support, so all audio was converted to WAV.
- **Title Screen**: Issues with animating clouds (resolved by increasing spawn distances) and centering text (fixed by adjusting font sizes and screen dimensions).
- **Level Management**: Challenges in auto-loading new levels, placing elements (enemies, chests, traps), transferring player data (health, inventory), and syncing music/effects.

## UML Diagram

Here's a textual representation of the project's UML structure:
<img width="3140" height="2130" alt="Untitled-2023-08-29-2053_22 44 01" src="https://github.com/user-attachments/assets/5849fa09-7a94-4686-9253-9ed61083cbae" />

## Conclusions

Developing Tiny Swords was a valuable experience in tackling real-world programming challenges. It strengthened skills in OOP concepts like encapsulation, inheritance, and polymorphism through class designs for entities, enemies, and resources. Working within Java's limitations also highlighted its strengths for game development.

## Contributing

This is a personal project, but feel free to fork and experiment! Pull requests are welcome for bug fixes or improvements.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
