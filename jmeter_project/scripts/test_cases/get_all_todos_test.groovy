// Simulate test.step
log.info("Test Step: Getting all todos...");
makeHttpRequest(GET, "/todos");
log.info("Test Step: Finished getting all todos.");