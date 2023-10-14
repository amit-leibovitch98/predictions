package simulation.utils;

import simulation.world.detail.ISimulationComponent;
import simulation.world.detail.entity.Entity;
import simulation.world.detail.entity.EntityInstance;

import java.util.Random;

public class Grid implements ISimulationComponent {
    private final int rows;
    private final int cols;
    private Entity[][] grid;

    public Grid(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.grid = new EntityInstance[rows + 1][cols + 1];
    }

    public int getCols() {
        return cols;
    }

    public int getRows() {
        return rows;
    }

    public Entity getEntityInLoc(Location loc) {
        try {
            return grid[loc.getRow()][loc.getCol()];
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Location " + loc + " is out of bounds");
            System.exit(1);
            return null;
        }
    }

    public Location getEmptyRandLoc() {
        int randRow, randCol;
        Location loc;
        do {
            Random random = new Random();
            randRow = random.nextInt(this.rows) + 1;
            randCol = random.nextInt(this.cols) + 1;
            loc = new Location(randRow, randCol);

        } while (getEntityInLoc(loc) != null);
        return new Location(randRow, randCol);
    }

    private void doMove(EntityInstance entityInstance, Location oldLoc, Location newLoc) {
        setLocAsEmpty(oldLoc);
        putEntityInLoc(entityInstance, newLoc);
    }

    public Location move(EntityInstance entityInstance) {
        Location currLoc = entityInstance.getLocation();
        Location newLoc = getLocToRight(currLoc);
        if (currLoc.getRow() == 0) {
            System.out.println("currLoc: " + currLoc);
        }
        if (isLocEmpty(newLoc)) {
            doMove(entityInstance, currLoc, newLoc);
            return newLoc;
        }
        newLoc = getLocToLeft(currLoc);
        if (isLocEmpty(newLoc)) {
            doMove(entityInstance, currLoc, newLoc);
            return newLoc;
        }
        newLoc = getLocToUp(currLoc);
        if (isLocEmpty(newLoc)) {
            doMove(entityInstance, currLoc, newLoc);
            return newLoc;
        }
        newLoc = getLocToDown(currLoc);
        if (isLocEmpty(newLoc)) {
            doMove(entityInstance, currLoc, newLoc);
            return newLoc;
        }
        return entityInstance.getLocation();
    }

    public void setLocAsEmpty(Location loc) {
        grid[loc.getRow()][loc.getCol()] = null;
    }

    public void putEntityInLoc(EntityInstance entityInstance, Location loc) {
        if (getEntityInLoc(loc) != null) {
            throw new RuntimeException("Location is already occupied");
        } else if (loc.getRow() < 1 || loc.getCol() < 1 || loc.getRow() > this.rows || loc.getCol() > this.cols) {
            throw new RuntimeException("Location is out of bounds");
        } else {
            grid[loc.getRow()][loc.getCol()] = entityInstance;
        }
    }

    private Location getLocToRight(Location loc) {
        if (loc.getCol() == this.cols) {
            return new Location(loc.getRow(), 1);
        } else {
            return new Location(loc.getRow(), loc.getCol() + 1);
        }
    }

    private Location getLocToLeft(Location loc) {
        if (loc.getCol() == 1) {
            return new Location(loc.getRow(), this.cols);
        } else {
            return new Location(loc.getRow(), loc.getCol() - 1);
        }
    }

    private Location getLocToUp(Location loc) {
        if (loc.getRow() == 1) {
            return new Location(this.rows, loc.getCol());
        } else {
            return new Location(loc.getRow() - 1, loc.getCol());
        }
    }

    private Location getLocToDown(Location loc) {
        if (loc.getRow() == this.rows) {
            return new Location(1, loc.getCol());
        } else {
            return new Location(loc.getRow() + 1, loc.getCol());
        }
    }

    private boolean isLocEmpty(Location loc) {
        return getEntityInLoc(loc) == null;
    }

    @Override
    public String getInfo() {
        return "Grid size is: " + rows + "x" + cols + ".";
    }

    @Override
    public String getName() {
        return "Grid";
    }
}
