package facade;

import file.WorldProxy;
import simulation.world.World;

public class EngineFacade {
    WorldProxy worldProxy;
    World world;

    public void ReadFile(String path) {
        if(path == null) {
            throw new IllegalArgumentException("path is null");
        } else {
            worldProxy = new WorldProxy(path);
        }
    }

    public void ShowSimulationDetails() {
        this.world = worldProxy.getWorld();
        System.out.println("ShowSimulationDetails");
    }

    public void StartSimulation() {
        //TODO: implement
        System.out.println("StartSimulation");
    }

    public void ShowPastSimulationResult() {
        //TODO: implement
        System.out.println("ShowPastSimulationResult");
    }
}
