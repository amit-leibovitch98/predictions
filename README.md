# Submitter information: 
  Amit Leibovitch 
  id: 318659745
  email: amitle111@gmail.com
# Desgin review:
  # Module 1: consoleIU contains:
    main
    menu class
    facade to interact with the engine (the backend)
  # Module 2: Engine contains:
    file package- contains classes that read the schema and the xml input file
    schema auto-generated class and a proxy- "translates" the auto-generated filed from the schena to our classes (the ones in simulation.world and simulation.utils)
    the simulation class:
      utils package contains classes that solve and handle general items in the system e.g. range class, expression class, type enum...
      world package- contains all the simulation' world details- entity types (with properties), entity instances(with properties' values), environment variables with their values termination conditions...
      We also have the Simulation class- which runs the simulation
      and the SimulationManager (implemented as a singleton) - which stores past simulations and generates them by histogram/entity
  # General explanation on the system:
    We have 2 main parts in the system- engine and UI.
    The us holds a facade, and only he communicates with the engine by activating the relevant methods.
    The engine only reacts to the facade calls.
    Also, I chose to implement the action as an abstract action that each action (increase, decrease...) extends and as an interface they all implement
    I chose to do both because while the interface gave me the identical behavior I needed, I found myself copy&paste-ing code from one specific action to another.
    The condition is implemented in the same way- we have simpleCond and complexedCondextending conditions and implementing Icond.
      
