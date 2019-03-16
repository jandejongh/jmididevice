# jmididevice
A Java library holding a few MIDI device (incl. Boss ME-80) implementations, some with Swing components.

## Introduction

The jservice library introduces (among others) the Service, MidiService, and MidiDevice interfaces.
The present library, jmididevice, features implementations of MidiDevice for devices I own.
In addition, it features Java Swing components for monitoring and/or controlling (or even playing)
these devices.

Currently (v0.1.0), the library features only a single implementation for the Boss ME-80.
However, work on the Alesis Quadraverb (GT) and some AKAI MIDI controllers is underway.

## Organization and Dependencies

The library is organized as a Maven project, but not (yet) published on Maven Central.
It has a sole dependency, jservice, which is also available from my github page.
If you want to play with the source, you therefore have to clone both repositories.

## Installation

Several options here, but easiest: Download the executable jar that is marked as an 'Asset'
under the release of your choice on the github repo.
You will need the ...-with-dependencies jar file.
Then do the usual 'java -jar <jmididevice...with-dependencies...jar>'.

The remainder of this README describes the Boss ME-80 Patch Editor (the library's Main class at the present time).

## Boss ME-80 Patch Editor

### Preparing the Boss ME-80

Currently, the library only supports connecting to the MIDI device itself through a UDP multicast socket with
MIDI over UDP.
This, in fact, is supported by the marvellous QmidiNet program by Rui Nuno Capela.
Most recent Linux distributions ship with QmidiNet or have it as an optional package in their repos.
While your at it, you'll need QjackCtl as well (from the same author).

Connect the Boss ME-80 through USB to a Linux machine (like a RPi).
Make sure the ME-80 is recognized on that machine (at ALSA level MIDI level; never mind the audio port),
and start QjackCtl and QmidiNet. Note that we do not need to run jack or jack2.

Use the connection editor in QjackCtl to connect the ME-80 with QmidiNet.
Switch between patches on the ME-80; QmidiNet should send MIDI Program Change messages over UDP;
you can check that by looking at the neat QmidiNet applet in your task bar.

You're all prepared now.

### Screenshot
Upon starting the main method in jmididevice, the following screen (well, more or less) will pop up.

![](resources/images/Screenshot_JMe80_v0.1.0_Full.png)

As you can see, the interface is Spartan at best, and still work in progress.
ME-80 owners likely recognize the various effect groups of the device, and their parameters.
The next sections describe the different panels on the application main screen, from top-left to bottom-right.

### MIDI

The top-left panel controls the interface to the MIDI. Clicking the small rectangle will toggle the status of
the interface: If the rectangle is filled red, it is active.
Note that the application does not activate MIDI upon startup.

The panel also has Tx and Rx indicators (at the MIDI level).
If you switch patches on the ME-80 (or have other MIDI traffic around), the Rx indicator should lite up.

### ME-80 \[Device\]

The second panel controls the MidiDevice service in the software.
As with the MIDI panel, this panel features a rectangular activation switch that you need to click
in order to activate the device.
Rest assured, these are the only two buttons you have to press in order to get started.

The watchdog rectange should turn green, and the device's identification data should become visible
(as in the screenshot).

If both MIDI and ME-80 \[Device\] are activated, the appearance of the screen should
change dramatically: All panels are updated to reflect the state of the ME-80.
The application continuously monitors the ME-80 by sending requests for patch and system data.
As a result, there will be some heavy traffic (well, heavy in MIDI context).
