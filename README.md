# Vacuum Cleaning Agent

## Description 
In this project, we created a vaccuum cleaning agent that finds its path in a room and tries to clean the room in the **least number of steps** while **minimizing energy consumption** and **maximizing performance**. Three classes of agents with variable visibility were developed and their performance was evaluated regarding the number of steps needed, energy consumption and performance.

We also worked on implementing a **Multi-Agent** Adversarial Vacuum Cleaner Agent, where the environment includes one ore many vacuum cleaners, as well as one or many “dirt producer” agents which dump dirt on the floor. The goal is for both types of agents to **operate in the most efficient way**, i.e.  perform **the least number of steps** while **minimizing energy consumption** and **maximizing performance**. Also, “dirt producers” must **maximize the number of dirt in the room while vacuum cleaner will try to minimize it**.

### Environments:
 1. Fully Observable 
 2. Partially Observable
 3. Non Observable

### Algorithms Used: 

 1. Breadth First Search (BFS)
 2. Simulated Annealing for solving the Travelling Salesman Problem
 3. Weight Driven BFS
 4. Depth First Search for room discovery.
 5. Adversarial BFS
 6. and other variations depending on the environment.

This project opens the path for further regarding efficient algorithms for path finding that can be applied to many real life scenarios not only vaccum cleaning agents, but also for data routing in communication systems, VLSI, scheduling...

## Requirements
- Java 8
- Processing 3
- G4P

## Installation
Runnable jars with exe and bat files are available in the runnable folder. 

## Authors and acknowledgment
This Application is built by **Georgio Yammine**, **Anis Ismail**, and **Rami Naffah**.

## Usage
![launcher](/images/launcher.png)
![Fully Observable BFS](/images/BFS.PNG)
![partially Observable weighted BFS](/images/BFSvis.png)
![Unobservable Discover the room DFS](/images/discover.PNG)
![Multi agent](/images/multi.png)


