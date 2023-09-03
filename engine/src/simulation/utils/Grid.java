package simulation.utils;

import simulation.world.detail.ISimulationComponent;
import simulation.world.detail.entity.Entity;
import simulation.world.detail.entity.EntityInstance;

public class Grid implements ISimulationComponent {
    private final int rows;
    private final int cols;
    private Entity[][] grid;

    public Grid(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.grid = new Entity[rows][cols];
    }

    public Grid(Grid grid) {
        this.rows = grid.getRows();
        this.cols = grid.getCols();
        this.grid = new Entity[rows][cols];
    }

    public int getCols() {
        return cols;
    }

    public int getRows() {
        return rows;
    }

    public Entity[][] getEntityArray() {
        return grid;
    }

    public Entity getEntityInLoc(Location loc) {
        return grid[loc.getRow()][loc.getCol()];
    }

    public Location getEmptyRandLoc() {
        int randRow, randCol;
        Location loc;
        do {
            randRow = (int) (Math.random() * rows);
            randCol = (int) (Math.random() * cols);
            loc = new Location(randRow, randCol);

        } while (getEntityInLoc(loc) != null);
        return new Location(randRow, randCol);
    }

    public Location move(EntityInstance entityInstance) {
        Location loc = entityInstance.getLocation();
        loc = getLocToRight(loc);
        if(isLocEmpty(loc)) {
            putEntityInLoc(entityInstance, loc);
            return loc;
        }
        loc = getLocToLeft(loc);
        if(isLocEmpty(loc)) {
            putEntityInLoc(entityInstance, loc);
            return loc;
        }
        loc = getLocToUp(loc);
        if(isLocEmpty(loc)) {
            putEntityInLoc(entityInstance, loc);
            return loc;
        }
        loc = getLocToDown(loc);
        if(isLocEmpty(loc)) {
            putEntityInLoc(entityInstance, loc);
            return loc;
        }
        return entityInstance.getLocation();
    }

    public void putEntityInLoc(Entity entity, Location loc) {
        if (getEntityInLoc(loc) != null) {
            throw new RuntimeException("Location is already occupied");
        } else {
            grid[loc.getRow()][loc.getCol()] = entity;
        }
    }

    private Location getLocToRight(Location loc) {
        if (loc.getCol() == this.cols - 1) {
            return new Location(loc.getRow(), 0);
        } else {
            return new Location(loc.getRow(), loc.getCol() + 1);
        }
    }

    private Location getLocToLeft(Location loc) {
        if (loc.getCol() == 0) {
            return new Location(loc.getRow(), this.cols - 1);
        } else {
            return new Location(loc.getRow(), loc.getCol() - 1);
        }
    }

    private Location getLocToUp(Location loc) {
        if (loc.getRow() == 0) {
            return new Location(this.rows - 1, loc.getCol());
        } else {
            return new Location(loc.getRow() - 1, loc.getCol());
        }
    }

    private Location getLocToDown(Location loc) {
        if (loc.getRow() == this.rows - 1) {
            return new Location(0, loc.getCol());
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
