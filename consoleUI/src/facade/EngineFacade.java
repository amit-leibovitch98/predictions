package facade;

import file.WorldProxy;
import simulation.world.World;

import java.util.List;

public class EngineFacade {
    private WorldProxy worldProxy;
    private World world;

    public void ReadFile(String path) {
        if(path == null) {
            throw new IllegalArgumentException("path is null");
        } else {
            worldProxy = new WorldProxy(path);
            this.world = worldProxy.getWorld();
        }
    }

    public void ShowSimulationDetails() {
        System.out.println("---Printing World Details---");
        List<List<List<String>>> worldDeats = world.getWorldDeatils();
        for (List<List<String>> worldProp : worldDeats) {
            for (List<String> strings : worldProp) {
                for (String string : strings) {
                    System.out.println(string);
                }
            }
        }
        System.out.println("---Printing World Details Finished---");
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
