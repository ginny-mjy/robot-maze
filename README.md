## Robot escape from maze

A robot is trying to escape from a maze where it starts moving from the top-left corner 
(0,0) to the exit in the bottom-right corner (6,6). It moves according to the following 
rules:

- The robot has 60% chance to go forward along the same direction.

- The robot has 20% chance to turn left and 20% chance to turn right.

- The initial moving direction is indicated by the pointer. So, if the robot chooses 
to go forward, it will reach (1,0) in the next step.

- The new position must be within the boundaries of the maze.

- The new position must not have been visited previously.

- If the new position does not fulfil the criteria above, the robot will try another 
move again. Every decision is independent of the others.

- If the robot reaches a dead-end - all directions are not enterable, it fails to 
leave the maze.

- The robot continues to move until it reaches the exit.

![Question Image](images/question.png)

### Coding description

- Program using the Monte Carlo method to estimate the probability (P) of the robot
successfully escaping from the maze. The simulation should run one million times.

- Program print out the simulation results clearly and round the success rate P to
3 digits after the decimal point.

- The acceptable range of the success rate is 0.395 - 0.400 and the execution time 
is less than 5 seconds.

- Main method is located in the main class.

- All other classes is defined as the private static inner classes of the main class.

### Development

- JDK 1.8
