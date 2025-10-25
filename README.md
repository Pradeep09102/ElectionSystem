# Election System

## Overview

The Election System is a command-line Java application designed to simulate and manage regional elections. It processes voter preferences, validates votes, and outputs the results directly to the console. The system reads input data from a file located in the `src/main/resources` directory and displays the election outcomes on the command line interface.

## Prerequisites

* Java 17 or higher
* Maven (for dependency management and building the project)
* Git (for version control)

## Installation

1. Clone the repository:

   ```bash
   git clone https://github.com/Pradeep09102/ElectionSystem.git
   cd ElectionSystem
   ```

2. Build the project using Maven:

   ```bash
   mvn clean install
   ```

3. Ensure the `voting.dat` file is located in the `src/main/resources` directory. This file contains the election data in the following format:

   ```
   Hyderabad/ABCFGHKLM
   Warangal/BCDHIJNOP
   Nizamabad/AEFGQRSUV
   Karimnagar/BCGHJKLWX
   Khammam/ABDEFTUVW
   ```

4. Run the application:

   ```bash
   mvn exec:java -Dexec.mainClass="com.electionsystem.ElectionSystemMain"
   ```

   The system will process the votes and display the results in the console.

## Data Format

The `voting.dat` file should contain:

* **Region Names**: Each line starts with a region name followed by a slash (`/`).
* **Candidate Codes**: After the slash, a string of uppercase letters representing the candidates in that region.

Example:

```
Hyderabad/ABCFGHKLM
Warangal/BCDHIJNOP
Nizamabad/AEFGQRSUV
Karimnagar/BCGHJKLWX
Khammam/ABDEFTUVW
```

 ELECTION SYSTEM - RESULTS
========================================

Input File: voting.dat

CHIEF OFFICER
-------------
B  (Total Points: 41)

REGIONAL RESULTS
----------------
Region: Hyderabad
  Contestants: A, B, C, F, G, H, K, L, M
  Invalid Votes: 3
  Regional Head: A  (Points: 15)
  
  Candidate Points (region):
  
    A : 15
    B : 10
    C : 8
    G : 4
    F : 2
    H : 2
    K : 1
    L : 0
    M : 0

Region: Warangal
  Contestants: B, C, D, H, I, J, N, O, P
  Invalid Votes: 2
  Regional Head: C  (Points: 7)
  
  Candidate Points (region):
   
    B : 13
    C : 7
    D : 6
    H : 4
    I : 4
    N : 4
    O : 4
    J : 3
    P : 2

Region: Nizamabad
  Contestants: A, E, F, G, Q, R, S, U, V
  Invalid Votes: 1
  Regional Head: A  (Points: 10)
 
  Candidate Points (region):
   
    A : 10
    E : 10
    S : 9
    F : 5
    Q : 5
    U : 5
    R : 4
    G : 3
    V : 3

Region: Karimnagar
  Contestants: B, C, G, H, J, K, L, W, X
  Invalid Votes: 1
  Regional Head: K  (Points: 11)
 
  Candidate Points (region):
   
    K : 11
    B : 9
    G : 9
    J : 8
    L : 5
    C : 4
    H : 4
    W : 3
    X : 0

Region: Khammam
  Contestants: A, B, D, E, F, T, U, V, W
  Invalid Votes: 1
  Regional Head: A  (Points: 13)
 
  Candidate Points (region):
   
    A : 13
    U : 10
    B : 9
    D : 9
    T : 7
    E : 4
    V : 4
    F : 3
    W : 1

GLOBAL CANDIDATE POINTS (ALL REGIONS)
-------------------------------------
 
  B : 41
  A : 38
  C : 19
  G : 16
  D : 15
  U : 15
  E : 14
  K : 12
  J : 11
  F : 10
  H : 10
  S : 9
  T : 7
  V : 7
  L : 5
  Q : 5
  I : 4
  N : 4
  O : 4
  R : 4
  W : 4
  P : 2

Process finished with exit code 0
