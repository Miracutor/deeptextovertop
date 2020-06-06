# Deep Text Overtop
A Java program that will translate Japanese text automatically from the clipboard using DeepL and 
displaying translated on the black translucent overlay.

## Why the name 'Deep Text Overtop'?
I just combine the random words together as a tentative title but... it ends up became the real title. 

Dammit! I should have followed Goto-sensei's advice! (Kakushigoto reference)

## For who this program intended for?
For the one who want to experience untranslated JP main story in games with at least better machine translation (DeepL). 

I used this program to read Fate/Grand Order story (like event stories and main chapters). Of course,  
it will never reach the quality of normal translations but at least, you can get the gist of the story. 

Theoretically, you can use this program to read many other games; not limited to FGO.

## Requirements
- Windows
- Chrome Browser
- Java 11+

## Setup Instructions
1. Make sure you fulfill the requirements before start.
   - I am sure the first and second is easy enough, so I will focus only at the third requirements.
   - To check if you have Java 11+ in your PC, please open the command prompt and type ```java -version```.
   - If it shows that you the Java version 11 or larger, you pass.
   - If it said not found, probably you don't have Java 11+ in your system, or your have not configure PATH 
   correctly.
     - Get Java 11 from here: `https://adoptopenjdk.net/`.
     - Pick JDK 11 and Hotspot.
     - Download the installer.
     - Run the installer and setup Java 11 with Add to PATH, Associate .jar and Set JAVA_HOME variables.
     - Check your installation with the above method.
2. Download ChromeDriver.
   - Check out your Chrome version by going to Help -> About
   - Download ChromeDriver here with the same version with your Chrome installation: 
   `https://chromedriver.chromium.org/downloads`
   - Extract the `chromedriver.exe` inside and keep the file.
3. Download the latest Deep Text Overtop jar from releases.
   - Put the `chromedriver.exe` that you extracted earlier at the same folder with the jar.
   - Double-click the jar and if it shows `Program initializing... OK`, then the program is successfully start!

## Usage Guide
