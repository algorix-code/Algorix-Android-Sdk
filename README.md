# Algorix Android SDK

**Welcome to the Algorix Android SDK**, your gateway to unlocking the full potential of in-app monetization.

This `Algorix-Android-SDK` repository contains:

1. Example source code for using Algorix
2. Open source mediation adapters

# Examples Demo App
The Java Demo App is sample projects demonstrating how to mediate ads using
Algorix SDK. To get started with the demo apps, follow the instructions below:

1.Open your desired project in Android Studio: Algorix Demo App - Java.

2.Verify that the dependency

    dependencies {
    
        // Add the Algorix Android SDK    
        implementation 'io.github.algorixco:ads-sdk:3.9.3'
    
    }

is included in your build.gradle (Module: app).

3.Change the package with your own unique identifier in your build.gradle (Module: app).
Base your unique identifier on the name of the application you will create or that you
have already created in the Algorix dashboard.

4.Update the unique Algorix ad unit ID value within the activity code for each ad format.
Each ad format corresponds to a unique Algorix ad unit ID you create in the Algorix
dashboard for the package used before.