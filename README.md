# 🐾 Desktop Pet Simulator

A cute, interactive virtual pet that lives on your desktop! Feed it, play games with it, let it sleep, and watch it react to your care with adorable animations.

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Swing](https://img.shields.io/badge/Swing-GUI-blue?style=for-the-badge)

## ✨ Features

### 🎮 Interactive Pet Care
- **Feed** your pet to reduce hunger and boost happiness
- **Play** a built-in Flappy Bird mini-game
- **Sleep** to restore your pet's energy
- **Pet** your companion for instant affection

### 😊 Dynamic Emotions
Your pet has 5 different mood states:
- **Happy** 😊 - When well-fed and content (yellow)
- **Sad** 😢 - When neglected (blue)
- **Hungry** 🍕 - Needs food! (pink/orange)
- **Sleepy** 😴 - Low on energy (purple)
- **Neutral** 😐 - Just chilling (teal)

### 🎪 Adorable Animations
- **Bouncing** - Constant gentle bounce animation
- **Eye Blinking** - Realistic automatic blinking
- **Giggle** - Pet wobbles when clicked or petted
- **Jump** - Excited jumping when interacted with
- **Excited Jump** - Big celebration after a good sleep!
- **Eating** - Open mouth with tongue animation
- **Sleeping** - Closed eyes with floating Z's

### 🎨 Visual Effects
- Gradient body colors that change with mood
- Sparkly eyes with shine effects
- Cute pink bow accessory (when happy/neutral)
- Floating hearts when super happy (>90% happiness)
- Particle effects (pizza slices and stars)
- Smooth color transitions for mood changes

### 📊 Real-Time Stats
Track your pet's well-being with three status bars:
- ❤️ **Happiness** - Keep it high by interacting!
- 🍔 **Fullness** - Feed regularly to avoid hunger
- ⚡ **Energy** - Let your pet sleep when tired

### 🎮 Mini-Game: Flappy Pet
- Play an integrated Flappy Bird-style game
- Control your pet with SPACE or mouse clicks
- Avoid green obstacles and rack up points
- Score increases happiness, but costs energy!

### 💡 Smart Features
- **Auto-degrading stats** - Pet gets hungry/tired over time (realistic care simulation)
- **Validation checks** - Can't feed when full, can't sleep when energized
- **Click interactions** - Click directly on your pet for random reactions
- **Helpful notifications** - Speech bubbles show your pet's responses
- **30-second sleep cycle** - Realistic rest duration

## 🚀 Getting Started

### Prerequisites
- Java 11 or higher installed
- That's it! No external dependencies needed

### Installation

#### Option 1: Run the JAR (Easiest)
1. Download `DesktopPetSimulator.jar` from the [Releases](../../releases) page
2. Double-click the JAR file, or run:
```bash
   java -jar DesktopPetSimulator.jar
```

#### Option 2: Compile from Source
1. Clone this repository:
```bash
   git clone https://github.com/yourusername/desktop-pet-simulator.git
   cd desktop-pet-simulator
```

2. Compile the code:
```bash
   javac DesktopPetSimulator.java
```

3. Run the application:
```bash
   java DesktopPetSimulator
```

## 🎯 How to Play

1. **Keep Your Pet Happy!**
   - Monitor the three status bars at the top
   - Feed when hunger (fullness) gets low
   - Let it sleep when energy drops
   - Pet it regularly for bonus happiness

2. **Interact with Your Pet**
   - Click the **Feed** button to give food 🍕
   - Click **Play Game** to launch Flappy Pet 🎮
   - Click **Sleep** for a 30-second nap 😴
   - Click **Pet** to show affection 💕
   - Or **click directly on your pet** for surprises!

3. **Watch the Moods**
   - Your pet's color and expression change based on its needs
   - Keep all stats balanced for the happiest pet!
   - When happiness >90%, hearts appear! ❤️

4. **Play the Mini-Game**
   - Press **Play Game** when energy >20%
   - Use SPACE or click to flap
   - Avoid the green pipes
   - High scores = more happiness!

## 🛠️ Built With

- **Java Swing** - GUI framework
- **AWT** - Graphics rendering and animations
- **Java Timers** - Animation loops and state updates
- Pure Java - No external libraries!

## 📸 Screenshots

<img width="487" height="738" alt="image" src="https://github.com/user-attachments/assets/0ac777c3-a3fb-44f5-96ce-1726dc251b88" />
<img width="477" height="678" alt="image" src="https://github.com/user-attachments/assets/3f917d17-386b-487d-9e8f-53f4d45daad4" />
<img width="482" height="740" alt="image" src="https://github.com/user-attachments/assets/f622abc0-4153-4257-b59d-c24ee15c2135" />
<img width="481" height="738" alt="image" src="https://github.com/user-attachments/assets/f7e4e474-63e4-4dbe-9d43-8535916106c5" />
<img width="476" height="736" alt="image" src="https://github.com/user-attachments/assets/fc2f52c9-0b2b-441a-9bbe-5302ca852f30" />
<img width="482" height="750" alt="image" src="https://github.com/user-attachments/assets/eac63f18-1ff0-4c96-913d-8b0b195d582c" />
<img width="475" height="730" alt="image" src="https://github.com/user-attachments/assets/7da5c855-1b52-4b6d-a896-032744d567c5" />
## 🎨 Customization Ideas

Want to modify your pet? Here are some ideas:
- Change the bow color (line ~370)
- Adjust mood thresholds (lines ~242-246)
- Modify stat degradation rates (lines ~266-268)
- Change particle types and colors
- Add new animations or expressions

## 🐛 Known Issues

- None currently! Report issues [here](../../issues)

## 📝 Version History

- **v1.0.0** (2024) - Initial release
  - Basic pet care mechanics
  - 5 mood states with animations
  - Flappy Bird mini-game
  - Click interactions
  - 30-second sleep cycle
  - Particle effects

## 🤝 Contributing

Contributions are welcome! Feel free to:
1. Fork the project
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 💡 Future Ideas

- [ ] Multiple pet types (cat, dog, bunny)
- [ ] Save/load pet state
- [ ] Pet leveling system
- [ ] More mini-games
- [ ] Customizable accessories
- [ ] Sound effects
- [ ] Pet aging system
- [ ] Achievement badges

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- Inspired by classic virtual pet games (Tamagotchi, Neopets)
- Built as a creative Java GUI project
- Special thanks to the Swing/AWT documentation

## 📧 Contact

Your Name - Vijaya Y

Project Link: (https://github.com/VijayaY836/Pet-Simulator)

---

⭐ **Enjoyed this project? Give it a star!** ⭐

Made with ❤️
Vij
