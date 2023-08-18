package simulation.world.detail.rule.action.condition;

public interface ICond {
    public boolean evaluateCond();
    public void activateThen();
    public void activateElse();
}
