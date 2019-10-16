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

    void delroutesto(int x) {
        router.removeRoutesTo = x
    }

    void routeDynamically(){
        while(1){
            delroutesto(1)
            if(flag){
                addroute 1, 2
            }
            else{
                addroute 1,3
            }
            flag = !flag
            sleep(10000)
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
//                addroute 3, 4
                addroute 4, 4
                addroute 5, 4
                break
            case 2:
                addroute 1, 4
                addroute 4, 4
                addroute 5, 4
                break
            case 3:
                addroute 1, 4
                addroute 4, 4
                addroute 5, 4
                break
            case 4:
                addroute 1, 1
                addroute 2, 2
                addroute 5, 2
                break
            case 5:
//                routeDynamically()
                addroute 1, 2
                addroute 2, 2
                addroute 4, 2
                break
            default:
                addroute 1, 1
                break
        }
    }

    void processMessage(Message msg) {

    }
}