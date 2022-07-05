/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package s2122.hw1;

import java.util.Random;

/**
 *
 * @author Ma Jingyu
 * @date 12.06.2022
 */
public class Main {

    // next direction
    public static final int FORWARD = 0;
    public static final int LEFT = 1;
    public static final int RIGHT = 2;

    // status of location in maze
    public static final int NOT_VISIT = 0;
    public static final int CANNOT_VISIT = 1;

    //orientation of robot
    public static final int EAST = 0;
    public static final int SOURTH = 1;
    public static final int WEST = 2;
    public static final int NORTH = 3;

    // status of location in maze
    public static final boolean IS_DEAD = true;
    public static final boolean AVALIABLE = false;

    //start point
    public static final int START_POINT = 1;
    //exit point
    public static final int EXIT_POINT = 7; 
        
    public static void main(String[] args) {
        Robot robot = new Robot();
        Maze maze = new Maze();
        int success = robot.successPath(maze, 1000000);

        String prob = String.format("%.3f", success / 1000000f);

        System.out.println("The Monte Carlo simulation result of one million runs:");
        System.out.println("No. of successful escape: " + success);
        System.out.println("Success Rate P: " + prob);
    }
    
    // additional classes are defined as private static inner class
    // define the behaviours of the robot
    private static class Robot {

        private int orientation;
        private int X;
        private int Y;

        public int getX() {
            return X;
        }

        public void setX(int X) {
            this.X = X;
        }

        public int getY() {
            return Y;
        }

        public void setY(int Y) {
            this.Y = Y;
        }

        private int getOrientation() {
            return orientation;
        }

        private void setOrientation(int[] change) {
            if (change.length != 2) {
                System.err.println("orientation error");
            } else if (1 == change[0] && 0 == change[1]) {
                orientation = SOURTH;
            } else if (0 == change[0] && -1 == change[1]) {
                orientation = WEST;
            } else if (-1 == change[0] && 0 == change[1]) {
                orientation = NORTH;
            } else {
                orientation = EAST;
            }
        }       
        private void setOrientation(int orientation) {
            this.orientation = orientation;
        }

        private int nextDirection() {
            Random random = new Random();
            int percent = random.nextInt(100); // create [0,100) randomly, each integer represent 1%
            if (percent < 60) {
                return FORWARD;
            } else if (percent < 75) {
                return LEFT;
            } else {
                return RIGHT;
            }
        }
        
        //re-slelct direction
        private int nextDirection(int preDirec) {
            Random random = new Random();
            int percent = random.nextInt(100); // create [0,100) randomly, each integer represent 1%
            if (FORWARD == preDirec) {
                if (percent < 50) {
                    return LEFT;
                } else {
                    return RIGHT;
                }
            } else if (percent < 75) {
                return FORWARD;
            } else {
                return RIGHT;
            }
        }
        
        //check whether robot walked to the exit
        private static boolean checkFinished(int X, int Y) {
            return EXIT_POINT == X && EXIT_POINT == Y;
        }
    
        //Determine the coordinates of the next point by orientation and direction
        private static int[] nextChange(int orienation, int direction) {
            int[] next = new int[2];
            switch (direction) {
                case FORWARD:                
                    switch(orienation) {
                        case EAST: next[0] = 0; next[1] = 1; break;
                        case SOURTH: next[0] = 1;  next[1] = 0;  break;
                        case WEST: next[0] = 0; next[1] = -1; break;
                        case NORTH:  next[0] = -1; next[1] = 0; break;
                        default: System.err.println("orienation error");
                    }   break;
                case LEFT:
                    switch(orienation) {
                        case EAST: next[0] = -1; next[1] = 0; break;
                        case SOURTH: next[0] = 0;  next[1] = 1; break;
                        case WEST: next[0] = 1; next[1] = 0; break;
                        case NORTH:  next[0] = 0; next[1] = -1; break;
                        default: System.err.println("orienation error");
                    }   break;
                case RIGHT:
                    switch(orienation) {
                        case EAST: next[0] = 1; next[1] = 0; break;
                        case SOURTH: next[0] = 0;  next[1] = -1; break;
                        case WEST: next[0] = -1; next[1] = 0; break;
                        case NORTH:  next[0] = 0; next[1] = 1; break;
                        default: System.err.println("orienation error");
                    }   break;        
                default: System.err.println("direction error"); 
            }
            return next;
        }
    
        //Judge whether the next point is feasible
        private boolean pathStatus(Maze maze, int[] next_change, int nextDirec) {
            boolean pathStatus;
            int[] nextPoint = new int[2];
            int[] point = new int[2]; //current point

            nextPoint[0] = getX() + next_change[0];
            nextPoint[1] = getY() + next_change[1];
            point[0] = getX();
            point[1] = getY();

            if (maze.deadEnd(nextPoint)) {
                pathStatus = reselectDirec(maze, nextDirec);
            } else if (maze.canVisit(nextPoint)) {
                setX(nextPoint[0]);
                setY(nextPoint[1]);
                setOrientation(next_change);
                maze.setPointStatus(nextPoint, CANNOT_VISIT);
                pathStatus = AVALIABLE;
            } else if (maze.deadEnd(point)) {
                pathStatus = IS_DEAD;
            } else {
                pathStatus = reselectDirec(maze, nextDirec);
            }
            return pathStatus;
        }

        //reselect a direction and judge point status
        private boolean reselectDirec(Maze maze, int nextDirec) {
            boolean pathStatus = false;
            int[] nextPoint = new int[2];
            int[] nextChange;
            int[] point = new int[2];

            for (int i = 0; i < 3; i++) {
                nextChange = nextChange(getOrientation(), nextDirection(nextDirec));
                nextPoint[0] = getX() + nextChange[0];
                nextPoint[1] = getY() + nextChange[1];
                point[0] = getX();
                point[1] = getY();
                if (maze.canVisit(nextPoint)) {
                    setX(nextPoint[0]);
                    setY(nextPoint[1]);
                    setOrientation(nextChange);
                    maze.setPointStatus(nextPoint, CANNOT_VISIT);
                    pathStatus = AVALIABLE;
                    break;
                } else if (maze.deadEnd(point)) {
                    pathStatus = IS_DEAD;
                    break;
                }
            }

            return pathStatus;
        }

        // start find path and count the success time
        private int successPath(Maze maze, int numOfTries) {
            boolean isDead = false;
            int[] nextChange;
            int nextDirec;
            int success = 0;

            for (int i = 0; i < numOfTries; i++) {
                // set initial location (1,1)
                setX(START_POINT);
                setY(START_POINT);
                setOrientation(EAST);
                maze.initMaze(); // re-setting the maze
                while (!checkFinished(getX(), getY())) {
                    nextDirec = nextDirection();
                    nextChange = nextChange(getOrientation(), nextDirec);
                    isDead = pathStatus(maze, nextChange, nextDirec);
                    if (isDead) {//if this path is dead, finish this try
                        break;
                    }
                }

                if (isDead) {
                    isDead = false;
                    continue;
                }
                success++;
            }
            return success;
        }
    }

    /*
    define the behaviours of the maze
    init a [9][9] int array, add a wall to (7,7) maze
    initial maze format is [1, 1, 1, 1, 1, 1, 1, 1, 1]
                           [1, 0, 0, 0, 0, 0, 0, 0, 1]
                           [1, 0, 0, 0, 0, 0, 0, 0, 1]
                           [1, 0, 0, 0, 0, 0, 0, 0, 1]
                           [1, 0, 0, 0, 0, 0, 0, 0, 1]
                           [1, 0, 0, 0, 0, 0, 0, 0, 1]
                           [1, 0, 0, 0, 0, 0, 0, 0, 1]
                           [1, 0, 0, 0, 0, 0, 0, 0, 1]
                           [1, 1, 1, 1, 1, 1, 1, 1, 1]
     */
    private static class Maze {

        int[][] maze;

        private void initMaze() {
            maze = new int[9][9];
            // first line and last line
            for (int i = 0; i < maze[0].length; i++) {
                maze[0][i] = CANNOT_VISIT;
                maze[(maze.length - 1)][i] = CANNOT_VISIT;
            }
            //left column and right column
            for (int i = 0; i < maze.length; ++i) {
                maze[i][0] = CANNOT_VISIT;
                maze[i][(maze[0].length - 1)] = CANNOT_VISIT;
            }
        }

        //whether the point can be visited
        private boolean canVisit(int[] location) {
            int point = maze[location[0]][location[1]];
            if (2 != location.length) {
                System.err.println("location length errer");
                return false;
            } else {
                return CANNOT_VISIT != point;
            }
        }
        
        //the point surround the point is avaliable or not
        private boolean deadEnd(int[] location) {
            int poinTop = CANNOT_VISIT;
            int pointBottom = CANNOT_VISIT;
            int pointLeft = CANNOT_VISIT;
            int pointRight = CANNOT_VISIT;
            if (location[0] > 0) {
                poinTop = maze[location[0] - 1][location[1]];
            }
            if (location[0] < maze.length - 1) {
                pointBottom = maze[location[0] + 1][location[1]];
            }
            if (location[1] > 0) {
                pointLeft = maze[location[0]][location[1] - 1];
            }
            if (location[1] < maze[0].length - 1) {
                pointRight = maze[location[0]][location[1] + 1];
            }
            return CANNOT_VISIT == poinTop && CANNOT_VISIT == pointBottom && CANNOT_VISIT == pointLeft && CANNOT_VISIT == pointRight;
        }

        private void setPointStatus(int[] point, int status) {
            maze[point[0]][point[1]] = status;
        }
    }
}
