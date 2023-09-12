package simulation.world.detail.entity;

public class EntityPropertyTicks {
    private int chagesCount;
    private int ticksSum;

    private int lastUpdateTick;
    private EntityProperty entityProperty;

    public EntityPropertyTicks(EntityProperty property) {
        this.chagesCount = 0;
        this.ticksSum = 0;
        this.entityProperty = property;
    }

    public int getLastUpdateTick() {
        return lastUpdateTick;
    }

    public EntityProperty getEntityProperty() {
        return entityProperty;
    }

    public void updateValChanged(int changedOnTick) {
        chagesCount++;
        ticksSum += changedOnTick;
        lastUpdateTick = changedOnTick;
    }

    public float getPropertyConsistency() {
        if(chagesCount == 0)
            return 0;
        return (float) ticksSum / chagesCount;
    }



}
