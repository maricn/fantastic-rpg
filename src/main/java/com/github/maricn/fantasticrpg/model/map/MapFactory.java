package com.github.maricn.fantasticrpg.model.map;

import com.github.maricn.fantasticrpg.model.character.MonsterFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Factory used to generate levels. Level number is a seed used for {@link java.util.Random},
 * so using same seed will result in same level.
 *
 * @author nikola
 */
public class MapFactory {
    private static final int MAX_ROOM_WIDTH = 22;
    private static final int MAX_ROOM_HEIGHT = 6;

    private static final int MIN_ROOM_WIDTH = 6;
    private static final int MIN_ROOM_HEIGHT = 3;

    private static final double MIN_ROOM_RATIO = 0.5;
    private static final double MAX_ROOM_RATIO = 0.94;

    private static final int LEVELS_PER_DIFFICULTY = 10;

    private Random random;
    private MonsterFactory monsterFactory;

    public MapFactory(MonsterFactory monsterFactory) {
        this.monsterFactory = monsterFactory;
    }

    public Map generateMap(int seed) {
        return generateMap(80, 24, seed);
    }

    public Map generateMap(int mapWidth, int mapHeight, int seed) {
        random = new Random(seed);

        // Create blank map fields
        Field[][] fields = new Field[mapHeight][mapWidth];
        for (int i = 0; i < mapHeight; i++) {
            for (int j = 0; j < mapWidth; j++) {
                fields[i][j] = new Field(FieldType.WALL);
            }
        }

        // Simulate a table of rooms
        int roomsAcross = mapWidth / MAX_ROOM_WIDTH;
        int maxRooms = roomsAcross * (mapHeight / MAX_ROOM_HEIGHT);
        boolean[] usedRoomId = new boolean[maxRooms];
        for (int i = 0; i < maxRooms; i++) {
            usedRoomId[i] = false;
        }

        // Generate rooms
        int totalRooms = random.nextInt(
                (int) (maxRooms * MAX_ROOM_RATIO - maxRooms * MIN_ROOM_RATIO)) + (int) (maxRooms * MIN_ROOM_RATIO
        );
        List<Room> rooms = new ArrayList<>(totalRooms);
        int numOfMonsters = 0;
        for (int i = 0; i < totalRooms; i++) {

            // Choose a table cell for a room
            int roomCell;
            do {
                roomCell = random.nextInt(maxRooms);
            } while (usedRoomId[roomCell]);

            // Randomize room size
            int width = random.nextInt(MAX_ROOM_WIDTH - MIN_ROOM_WIDTH) + MIN_ROOM_WIDTH;
            int height = random.nextInt(MAX_ROOM_HEIGHT - MIN_ROOM_HEIGHT) + MIN_ROOM_HEIGHT;

            // Randomize room position
            int x = (roomCell % roomsAcross) * MAX_ROOM_WIDTH;
            x += random.nextInt(MAX_ROOM_WIDTH - width);
            int y = (roomCell / roomsAcross) * MAX_ROOM_HEIGHT;
            y += random.nextInt(MAX_ROOM_HEIGHT - height);

            // Place room at map
            Room room = new Room(x, y, width, height);
            placeRoom(fields, room, seed);
            numOfMonsters += placeMonsters(fields, room, seed);
            rooms.add(room);
        }

        // Connect rooms with tunnels
        for (int i = 0; i < totalRooms + 2; i++) {
            Room currentRoom = rooms.get(i % totalRooms);
            Room goalRoom = rooms.get((i + 1) % totalRooms);

            int currentX = currentRoom.x + (currentRoom.width / 2);
            int currentY = currentRoom.y + (currentRoom.height / 2);
            int deltaX = (goalRoom.x + (goalRoom.width / 2)) - currentX;
            int deltaY = (goalRoom.y + (goalRoom.height / 2)) - currentY;

            int deltaXSign = deltaX >= 0 ? 1 : -1;
            int deltaYSign = deltaY >= 0 ? 1 : -1;

            while (!(deltaX == 0 && deltaY == 0)) {
                boolean movingInX = random.nextBoolean();

                if (movingInX && deltaX == 0) {
                    movingInX = false;
                }
                if (!movingInX && deltaY == 0) {
                    movingInX = true;
                }

                int carveLength = random.nextInt(Math.abs(movingInX ? deltaX : deltaY)) + 1;
                for (int carver = 0; carver < carveLength; carver++) {
                    if (movingInX) {
                        currentX += deltaXSign;
                    } else {
                        currentY += deltaYSign;
                    }

                    if (FieldType.WALL.equals(fields[currentY][currentX].getType())) {
                        fields[currentY][currentX].setType(FieldType.EMPTY);
                    }
                }

                if (movingInX) {
                    deltaX -= deltaXSign * carveLength;
                } else {
                    deltaY -= deltaYSign * carveLength;
                }
            }
        }

        // Generate player's starting position
        int startY, startX;
        do {
            startY = random.nextInt(mapHeight);
            startX = random.nextInt(mapWidth);
        } while (!FieldType.EMPTY.equals(fields[startY][startX].getType()) ||
                fields[startY][startX].getOccupying() != null);

        return new Map(fields, startY, startX, numOfMonsters);
    }

    private int placeMonsters(Field[][] fields, Room room, int level) {
        int numOfMonsters = random.nextInt(
                1 + (room.width * room.height) /
                        (((level - 1) / LEVELS_PER_DIFFICULTY + 1) * 6)
        );
        for (int i = 0; i < numOfMonsters; i++) {
            int monsterX, monsterY;
            do {
                monsterX = room.x + random.nextInt(room.width);
                monsterY = room.y + random.nextInt(room.height);
            } while (fields[monsterY][monsterX].getOccupying() != null);

            int monsterDifficulty = random.nextInt(level);
            MonsterFactory.MonsterDifficulty difficulty;
            switch (monsterDifficulty / LEVELS_PER_DIFFICULTY) {
                case 0:
                    difficulty = MonsterFactory.MonsterDifficulty.EASY;
                    break;
                case 1:
                    difficulty = MonsterFactory.MonsterDifficulty.MODERATE;
                    break;
                default:
                    difficulty = MonsterFactory.MonsterDifficulty.HARD;
            }

            fields[monsterY][monsterX].setOccupying(
                    monsterFactory.createMonster(random, difficulty, fields[monsterY][monsterX].getType())
            );
        }

        return numOfMonsters;
    }

    private Field[][] placeRoom(Field[][] fields, Room room, int seed) {
        FieldType fieldType = getRandomFieldType(seed);
        for (int i = room.y; i < room.y + room.height; i++) {
            for (int j = room.x; j < room.x + room.width; j++) {
                fields[i][j].setType(fieldType);
            }
        }

        return fields;
    }

    private FieldType getRandomFieldType(int seed) {
        FieldType fieldType = FieldType.EMPTY;
        if (seed >= 11) {
            if (seed >= 21) {
                int rnd = random.nextInt(42);
                if (rnd > seed) {
                    fieldType = FieldType.WALL;
                } else {
                    if (rnd > 21) {
                        fieldType = FieldType.WATER;
                    }
                }
            } else {
                // Actually a very magic number in this case!
                fieldType = random.nextInt(42) > seed ? FieldType.WATER : FieldType.EMPTY;
            }
        }

        return fieldType;
    }

    class Room {
        int x, y, width, height;

        public Room(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
    }
}
