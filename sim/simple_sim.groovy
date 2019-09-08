import org.arl.fjage.*
import static org.arl.unet.Services.*
import org.arl.unet.utils.GPlot
import java.awt.Color

trackAuvLocation = {
    def nodeInfo = agentForService NODE_INFO
    trace.moved(nodeInfo.address, nodeInfo.location, null)
    add new TickerBehavior(1000, {
        trace.moved(nodeInfo.address, nodeInfo.location, null)
    })
}
def speed_threshold = 5
speed_threshold = Math.min(speed_threshold,5).mps

def speed_increment = 2.mps
def time_increment = 2.minutes

def speed_initial = 0.mps
def time_initial = 0.minutes
def time_final = 16.minutes

//def const_time = Math.min(10.minutes - (2*speed_threshold).minutes - time_increment, 0)
//println const_time
def motionArray = []

Math.ceil(speed_threshold/speed_increment).times{
    motionArray << [time: time_initial, speed: speed_initial]
    time_initial += time_increment
    speed_initial += speed_increment
}

speed_initial = speed_threshold
motionArray << [time: time_initial, speed: speed_initial]
time_initial += time_increment
speed_initial -= speed_increment

(Math.max(0, Math.ceil((time_final-time_initial)/time_increment))).times{
    motionArray << [time: time_initial, speed: Math.max(speed_initial, 0.mps)]
    time_initial += time_increment
    speed_initial -= speed_increment
}

simulate 10.minutes, {
    def n = node('AUV-1', location: [0.m, 0.m, 0.m], mobility: true)
    n.startup = trackAuvLocation
    n.motionModel = motionArray
}

// read trace.nam to get simulation results
def t = []
def speed = []
def old_distance = 0
def new_distance
n = 0
new File('logs/trace.nam').eachLine { s ->
    if (s.startsWith('# BEGIN SIMULATION ')) n = s.substring(19) as int
    else if (n >= 1 && n <= 4 && s.startsWith('n ')) {
        t << ((s =~ /-t ([\-0-9\.]+)/)[0][1] as double)
        new_distance = ((s =~ /-y ([\-0-9\.]+)/)[0][1] as double)
        speed << new_distance - old_distance
        old_distance = new_distance
    }
}

// plot the paths
new GPlot('AUV Tracks', 600, 600).with {
    xlabel('seconds')
    ylabel('meters / second')
    plot("AUV-1", t as double[], speed as double[], Color.blue)
    axis(0, 600, 0, (speed_threshold/1.mps + 2))
    drawnow()
}