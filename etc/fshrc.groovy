//////// load modem shell closures, if available
try {
  run 'cls://org.arl.modem.shell.fshrc'
} catch (ClassNotFoundException ex) {
  // ignore exception, allowing modem jar to be absent
}
