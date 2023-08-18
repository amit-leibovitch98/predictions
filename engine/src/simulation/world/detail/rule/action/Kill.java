package simulation.world.detail.rule.action;

import simulation.world.detail.entity.Entity;

import java.util.List;

public class Kill extends Action{
    public Kill(Entity entity) {
        super(entity, "");
    }

    @Override
    public void doAction() {
        kill();
    }

    private void kill() {
        //TODO: implement kill
    }
}
