import org.arl.fjage.*
import org.arl.unet.*
import org.arl.unet.net.*

class new_routing_agent extends UnetAgent {
    def router
    int addr
    def flag

    void addroute(int to, int via) {
        router.send new RouteDiscoveryNtf(to: to, nextHop: via)
    }

    void startup() {
        flag = false
        def phy = agentForService Services.PHYSICAL
        subscribe topic(phy)

        router = agentForService Services.ROUTING
        subscribe topic(router)

        def nodeInfo = agentForService Services.NODE_INFO
        addr = nodeInfo.address

        switch (addr) {
            case 1:
                addroute 3, 2
                break
            case 2:
                addroute 3, 3
                break
            default:
                break
        }
    }

    void processMessage(Message msg) {
        if (msg instanceof DatagramNtf && msg.protocol == PING_PROTOCOL){
            send new DatagramReq(recipient: msg.sender, to: msg.from, protocol: Protocol.DATA)
        }
    }
}