import org.arl.unet.sim.channels.*
import org.arl.unet.link.ReliableLink
import org.arl.fjage.*
import org.arl.unet.net.*

import org.arl.unet.shell.*

platform = RealTimePlatform
channel.model = ProtocolChannelModel

channel.soundSpeed = 1500.mps          // c
channel.communicationRange = 100.m     // Rc
channel.detectionRange = 110.m
channel.interferenceRange = 200.m

// run the simulation infinately
simulate  {
    // Destination node
    node '1', remote: 1101, address: 1, location: [ 0.m, 0.m, 0.m], shell: true, stack: { container ->
        container.add 'new_routing_agent', new new_routing_agent();
        container.add 'link', new ReliableLink()
        container.add 'router', new Router()
        container.add 'rdp', new RouteDiscoveryProtocol()
        container.shell.addInitrc "${script.parent}/../etc/fshrc.groovy"
    }

    node '2', remote: 1102, address: 2, location: [ 0.m, 0.m, -75.m], shell: 5102, stack: { container ->
        container.add 'new_routing_agent', new new_routing_agent();
        container.add 'link', new ReliableLink()
        container.add 'router', new Router()
        container.add 'rdp', new RouteDiscoveryProtocol()
    }
}