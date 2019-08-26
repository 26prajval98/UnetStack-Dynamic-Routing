import org.arl.unet.sim.channels.*
import org.arl.fjage.*
import org.arl.unet.net.*

import org.arl.unet.shell.*

platform = RealTimePlatform
channel.model = ProtocolChannelModel

channel.soundSpeed = 1500.mps          // c
channel.communicationRange = 100.m     // Rc

// run the simulation infinately
simulate  {
    // Destination node
    node '1', remote: 1101, address: 1, location: [ 0.m, 0.m, 0.m], shell: 5101, stack: { container ->
        container.add 'new_routing_agent', new new_routing_agent();
        container.add 'routing', new Router();
        container.add 'rdp', new RouteDiscoveryProtocol();
    }

    node '2', remote: 1102, address: 2, location: [ 0.m, 0.m, -75.m], shell: 5102, stack: { container ->
        container.add 'new_routing_agent', new new_routing_agent();
        container.add 'routing', new Router();
        container.add 'rdp', new RouteDiscoveryProtocol();
    }

    // neighbor node for node 5, and will be a next node for node 5 during routing
    node '3', remote: 1103, address: 3, location: [0.m, 0.m, -90.m], shell: 5103, stack: { container ->
        container.add 'new_routing_agent', new new_routing_agent();
        container.add 'routing', new Router();
        container.add 'rdp', new RouteDiscoveryProtocol();
    }

//Neighbor node for node 5 ,but not a next node for node 5
    node '4', remote: 1104, address: 4, location: [0.m, 0.m, -150.m], shell: 5104, stack: {container ->
        container.add 'new_routing_agent', new new_routing_agent();
        container.add 'routing', new Router();
        container.add 'rdp', new RouteDiscoveryProtocol();
    }

// Source node
    node '5', remote: 1105, address: 5, location: [0.m, 0.m, -160.m], shell: true, stack: {container ->
        container.add 'new_routing_agent', new new_routing_agent();
        container.add 'routing', new Router();
        container.add 'rdp', new RouteDiscoveryProtocol();
    }
}
