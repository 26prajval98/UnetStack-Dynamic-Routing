import org.arl.fjage.*
import org.arl.unet.*
import org.arl.unet.net.*

class new_routing_agent extends UnetAgent {
    def router
    int addr
    def flag

    final static int PING_PROTOCOL = Protocol.USER

    void addroute(int to, int via) {
        router.send new RouteDiscoveryNtf(to: to, nextHop: via)
    }

    void delroutes() {
        router.removeRoute = -1
    }

    void routeDynamically(addr){
        while(1){
            delroutes()
            switch (addr){
                case 4:
                    if(flag){
                        addroute 1, 1
                        addroute 2, 2
                        addroute 5, 2
                    }
                    else{
                        addroute 1, 1
                        addroute 3, 3
                        addroute 5, 3
                    }
                    break
                case 5:
                    if(flag){
                        addroute 1, 2
                        addroute 2, 2
                        addroute 4, 2
                    }
                    else{
                        addroute 1, 3
                        addroute 3, 3
                        addroute 4, 3
                    }
                    break
                default:
                    addroute 1, 1
                    break
            }
            flag = !flag
            sleep(30000)
        }
    }

    void startup() {
        flag = false
        def phy = agentForService Services.PHYSICAL
        subscribe topic(phy)

        router = agentForService Services.ROUTING
        subscribe topic(router)

        def nodeInfo = agentForService Services.NODE_INFO
        addr = nodeInfo.address

        switch (addr){
            case 1:
                addroute 2, 4
                addroute 3, 4
                addroute 4, 4
                addroute 5, 4
                break
            case 2:
                addroute 1, 4
                addroute 4, 4
                addroute 5, 5
                break
            case 3:
                addroute 1, 4
                addroute 4, 4
                addroute 5, 5
                break
            default:
                routeDynamically(addr)
                break
        }
    }

    void processMessage(Message msg) {
        if (msg instanceof DatagramNtf && msg.protocol == PING_PROTOCOL)
            router.send new DatagramReq(recipient: msg.sender, to: msg.from, protocol: Protocol.DATA)
    }
}