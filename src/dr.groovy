import org.arl.fjage.*
import org.arl.unet.*
import org.arl.unet.net.*

class new_routing_agent extends UnetAgent {
    def router;
    int addr;

    void addroute(int to, int via) {
        def router = agentForService Services.ROUTING
        router.send new RouteDiscoveryNtf(to: to, nextHop: via)
    }

    void startup() {

        def phy = agentForService Services.PHYSICAL;
        subscribe topic(phy);

        router = agentForService Services.ROUTING;
        subscribe topic(router);

        def nodeInfo = agentForService Services.NODE_INFO;
        addr = nodeInfo.address; // obtain the address of the node

        def rdp = agentForService org.arl.unet.Services.ROUTE_MAINTENANCE
        def rsp = rdp << new RouteDiscoveryReq(1)      // discover routes to node 1

    }

    void processMessage(Message msg) {
        if (msg instanceof RouteDiscoveryNtf) {
            if (addr == 5)
                addroute 1, 3;

            else if (addr == 4)
                addroute 1, 2;

            else
                addroute 1, 1;
        }
    }
}