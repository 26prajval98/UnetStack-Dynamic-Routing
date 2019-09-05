import org.arl.fjage.*
import org.arl.unet.sim.MotionModel
import static org.arl.unet.Services.*

trackAuvLocation = {
    def nodeInfo = agentForService NODE_INFO
    trace.moved(nodeInfo.address, nodeInfo.location, null)
    add new TickerBehavior(10000, {
        trace.moved(nodeInfo.address, nodeInfo.location, null)
    })
}
def speed_threshold = 4;
speed_threshold = Math.min(speed_threshold,5).mps

def speed_increment = 2.mps;
def time_increment = 2.minutes;

def speed_initial = 0.mps;
def time_initial = 0.minutes;

def const_time = Math.min(10.minutes - (2*speed_threshold).minutes - time_increment, 0)
println const_time
def motionArray = []

(speed_threshold/speed_increment).times{
    speed_initial += speed_increment
    motionArray << [time: time_initial, speed: Math.min(speed_initial, speed_threshold)]
    time_initial += time_increment
}

motionArray << [time: time_initial, speed: speed_threshold]
time_initial += const_time

(speed_threshold/speed_increment).times{
    motionArray << [time: time_initial, speed: Math.max(speed_initial, 0.mps)]
    time_initial += time_increment
    speed_initial -= speed_increment
}

println motionArray

simulate 10.minutes, {
    def n = node('AUV-1', location: [0.m, 0.m, 0.m], mobility: true)
    n.startup = trackAuvLocation
    n.motionModel = motionArray
}