function fn() {
  var config = {};
  var options = { type: 'chrome', headless: karate.properties['headless'] === 'true', addOptions: ['--start-maximized'] };
  var executable = karate.properties['chrome.executable'];
  if (executable) options.executable = executable;
  if (karate.properties['browser.noSandbox'] === 'true') options.addOptions.push('--no-sandbox');
  karate.configure('driver', options);
  karate.configure('retry', { count: 30, interval: 500 });
  config.fixtureUrl = Java.type('java.lang.Thread').currentThread().getContextClassLoader().getResource('setup/fixture.html').toExternalForm();
  return config;
}
