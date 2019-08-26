def setupScript = new File('scripts/setup.groovy')
if (setupScript.exists()) {
  run setupScript
  return
}

container.add 'arp',        new org.arl.unet.addr.AddressResolution()
container.add 'ranging',    new org.arl.unet.phy.Ranging()
container.add 'mac',        new org.arl.unet.mac.aloha.AlohaACS()
container.add 'link',       new org.arl.unet.link.ReliableLink()
container.add 'transport',  new org.arl.unet.transport.SWTransport()
container.add 'router',     new org.arl.unet.net.Router()
container.add 'rdp',        new org.arl.unet.net.RouteDiscoveryProtocol()
container.add 'remote',     new org.arl.unet.remote.RemoteControl(scripts:'scripts')
container.add 'bbmon',      new org.arl.unet.bb.BasebandSignalMonitor('logs/signals-0.txt', 64)
container.add 'state',      new org.arl.unet.state.StateManager()
