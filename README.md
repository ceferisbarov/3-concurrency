# CSCI 6221 – Advancеd Softwarе Paradigms, Assignmеnt 3 – Concurrency, Fall 2023

## Set-up

All of the code has been developed and tested on Ubuntu 22.04. Java version information:
```
openjdk version "11.0.20.1" 2023-08-24
OpenJDK Runtime Environment (build 11.0.20.1+1-post-Ubuntu-0ubuntu122.04)
OpenJDK 64-Bit Server VM (build 11.0.20.1+1-post-Ubuntu-0ubuntu122.04, mixed mode, sharing)
```

## Problem statement:
Your application will take three arguments from the command line: file name, square size and the processing mode (Example: yourprogram somefile.jpg 5 S):

- file name: the name of the graphic file of jpg format (no size constraints)
- square size: the side of the square for the averaging
- processing mode: 'S' - single threaded and 'M' - multi threaded
  
Your task is to show the image, and start performing the following procedure:
* From left to right, top to bottom find the average color for the (square size) x (square size) boxes and set the color of the whole square to this average color. You need to show the result by progress, not at once.
* In the multi-processing mode, you need to perform the same procedure in parallel threads. The number of threads shall be selected according to the computer's CPU cores.
* There result shall be saved in a result.jpg file. The result of the processing shall look like the attached example.

## Solution
Solution consists of an object-oriented program that takes as input an image, square size, and processing mode. It provides as output visualisation of the average pooling process, and a saved version of the final result.
  
Average pooling takes place in a slightly different fashion. Instead of creating a window and sliding it to the right and down, we perform two separate acts:
* Horizontal pooling
* Vertical pooling

This results in the desired average pooling, albeit in a cleaner fashion. As a result, code is easier to write and maintain.

How to run the code:
```bash
# Clone the repository
git clone https://github.com/ceferisbarov/3-concurrency

# Walk into the source directory
cd 3-concurrency/src

# Compile the code
javac *.java

# Run it with necessary arguments
java Main <file path> <square size> <mode>

# Single threaded example 
java Main ../artifacts/lisa.jpg 20 S

# Multi threaded example
java Main ../artifacts/lisa.jpg 20 M
```
Larger files with too small square sizes will take same time, so please be patient.
