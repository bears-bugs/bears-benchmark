[![Travis CI Build Status](https://travis-ci.org/SzFMV2018-Tavasz/AutomatedCar.svg?branch=master)](https://travis-ci.org/SzFMV2018-Tavasz/AutomatedCar)
[![CodeFactor](https://www.codefactor.io/repository/github/szfmv2018-tavasz/automatedcar/badge)](https://www.codefactor.io/repository/github/szfmv2018-tavasz/automatedcar)
[![Coverage Status](https://coveralls.io/repos/github/SzFMV2018-Tavasz/AutomatedCar/badge.svg?branch=master)](https://coveralls.io/github/SzFMV2018-Tavasz/AutomatedCar?branch=master)
[![API Documentation](https://img.shields.io/badge/docs-stable-blue.svg)](https://szfmv2018-tavasz.github.io/AutomatedCar/javadoc/)
[![forum](https://img.shields.io/badge/forum-Everyone-D07A79.svg)](https://github.com/orgs/SzFMV2018-Tavasz/teams/everyone)
[![Gitter](https://img.shields.io/badge/chat-on%20gitter-ff69b4.svg)](https://gitter.im/szfmv2018-tavasz/Lobby?utm_source=share-link&utm_medium=link&utm_campaign=share-link)
[![Handout](https://img.shields.io/badge/handout-wiki-yellow.svg)](https://github.com/SzFMV2018-Tavasz/AutomatedCar/wiki)


# AutomatedCar

The program simulates the behavior of a passenger vehicle on the public road, equipped with driver assistance functions.

Equipped driver assistance functions are Parking Assist, Adaptive Cruise Control with Automated Emergency Braking and Lane Keeping Assistance with Traffic Sign Recognition, based on Ultra Sonic, Radar and Video sensors, respectively.

The inputs are categorized into two separate groups: user input, and configuration.

User input consists of the following elements:
 - Throttle button - gradually increasing while pressed, on a 1s duration from 0->100%, if not pressed, returns to idle
 - Brake  button - gradually increasing while pressed, on a 0.5s duration from 0->100%, if not pressed, returns to idle
 - Gear Shift - Automated, P/R/N/D available for choosing with a rotary button
 - Steering Buttons - left, right, gradually increasing while pressed on a 1s duration from 0->100%, in idle returns to straight
 - Driver Assistance Function Main Switches - On/Off, flip switch, changes state on press
 - ACC: Set/Resume/Plus/Minus/Time Gap switches, activating the function, changing the chosen reference speed, changing the chosen distance to the target vehicle, respectively
 - PA: Indicators to activate parking spot search, confirmation button to trigger automated parking maneuver

Configuration input consist of the following elements:
 - Description of world in an XML file, in a predefined language
 - "Tiles" - building blocks of the simulated world, such as roads, trees, people, vehicles, buildings, etc.
 - Position and behavior of non-player vehicles and characters
 - Position of the player vehicle

The system processes the input configuration, builds a world from the provided elements after processing the content of the XML file, creating a coordinate system and defining the distances, sizes and positions of the objects in that, places the non-player vehicles and the player in the world, and cyclically updates their position, interrupting or replacing the driver input with the calculated actuation requests from the Driver Assistance systems, whenever applicable. The output consists of two parts: first, the world with all its objects, displayed through a camera view, which centers on the player, and follows its movements, second the feedback about the current state of the vehicle, including the following data:
 - Current speed
 - Current gas, brake pedal position
 - Current steering wheel angle
 - Current gear
 - Indicator status
 - ACC: Set Speed, Time Gap level, Object detected (boolean), main status(on/off/controlling)
 - AEB: on/off status, visual warning signal (Yellow: probable collision, Red: automated braking active)
 - Parking assist: available(yellow),searching for place(blinking yellow), spot found(green), parking (blinking green), off (no signal)
 - LKA/TSR: On/Off, detected speed limit, detected warning sign, controlling steering(green), lane marking not available(yellow)

The driver assistance systems apply actuation requests to the steering, drivetrain(engine, transmission) or both, of the ego vehicle. Said subsystems decide if the actuation request of the player or the driver assistance system shall be executed. The actuation request is calculated from the information detectable by the sensor the DA systems depend on. The Video, Radar and Ultrasonic sensors have specified Fields of View, and view distance, detect a different subset of objects. From the provided object list - simulating the real world hardware detection of radar wave reflection, image processing, ultrasonic reflection - the "software" components evaluate the control relevant subset, and if the conditions are fulfilled (i.e. function activated and driving in lane, object in ego lane in front of vehicle, relative speed negative, distance reducing as time passes by) the function requests actuation (in the above example, requests speed reduction from the drivetrain).

The system decides between the concurrent drivetrain requests based on comfort and safety aspects. The system shall handle and detect collisions between the objects of the world. The simulation is approximating the real world physics, to provide a satisfying driving experience for the player, and an appropriate demonstration of the work of the driver assistance systems. On demand, the field of view of the sensors, and the detected control objects may be marked on the display for debug or demonstration.


# Roster

- Varga Robert
- Skornyak Andras
- Bencs Zoltan
- Bakonyi Gergő
- Módi Zoltán
- Hunyady László
- Kurucz Márk
- Lőrincz Attila
- Petrovics Gábor
- Hajdu Albert
- Kevevari Kristof
- Székács Márk
- Ács Bálint
- Kulcsár Kristóf
- Varga Dániel
- Sipos Gergo
- Tóth Ádám
- Papp Zsolt
- Uhrin Adam Attila
- Nyerges Dávid
- Marics Mark
- Hegedus Andras
- Ángyán Tamás
