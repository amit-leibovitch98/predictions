package simulation.world.detail.rule.action;

import simulation.utils.expression.CondExpression;
import simulation.world.World;
import simulation.world.detail.entity.Entity;
import simulation.world.detail.entity.EntityInstance;

import java.util.List;
import java.util.Objects;

public class Proximity extends Action {
    private Entity sourceEntity;
    private Entity targetEntity;
    private CondExpression circleRadius;
    private List<Action> actions;

    public Proximity(Entity sourceEntity, Entity targetEntity, CondExpression circleRadius, List<Action> actions) {
        super(sourceEntity, null);
        this.sourceEntity = sourceEntity;
        this.targetEntity = targetEntity;
        this.circleRadius = circleRadius;
        this.actions = actions;
    }
    @Override
    public boolean doAction(EntityInstance sourceEntityInstance, EntityInstance targetEntityInstance) {
        if(!Objects.equals(sourceEntityInstance.getName(), sourceEntity.getName())
                || !Objects.equals(targetEntityInstance.getName(), targetEntity.getName())){
            throw new IllegalArgumentException("sourceEntityInstance or targetEntityInstance is not of the right type");
        } else {
            //TODO: check if the circleRadius should be calculated from the sourceEntityInstance or the targetEntityInstance
            if(sourceEntityInstance.getLocation().getCircle(targetEntityInstance.getLocation()) <= (float) circleRadius.resolveExpression(sourceEntityInstance)) {
                for(Action action : actions){
                    if(Objects.equals(action.getEntity().getName(), sourceEntity.getName())){
                        action.doAction(sourceEntityInstance);
                    } else if (Objects.equals(action.getEntity().getName(), targetEntity.getName())){
                        action.doAction(targetEntityInstance);
                    } else {
                        throw new IllegalArgumentException("The action doesn't except this entity: " + action.getEntity().getName());
                    }
                }
                return true;
            } else {
                return false;
            }
        }

    }
    @Override
    public boolean doAction(EntityInstance entityInstance) {
        throw new UnsupportedOperationException("Set action doesn't support doAction with two entity instances");
    }
}
